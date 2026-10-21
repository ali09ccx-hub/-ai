package com.example.ui.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AiModelType
import com.example.data.model.ChatMessage
import com.example.data.model.LinguisticToolType
import com.example.data.model.ModelHyperparameters
import com.example.data.model.SavedLinguisticItem
import com.example.data.model.SystemPersona
import com.example.data.repository.BayanAiRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreenTab(val titleAr: String) {
    CHAT("الحوار الذكي"),
    LAB("المختبر اللغوي"),
    PLAYGROUND("مختبر المعايير"),
    SAVED("المحفوظات")
}

data class BayanUiState(
    val currentTab: AppScreenTab = AppScreenTab.CHAT,
    val messages: List<ChatMessage> = emptyList(),
    val isGenerating: Boolean = false,
    val parameters: ModelHyperparameters = ModelHyperparameters(),
    val selectedTool: LinguisticToolType = LinguisticToolType.TASHKEEL,
    val toolInput: String = "",
    val toolOutput: String = "",
    val isToolLoading: Boolean = false,
    val playgroundPrompt: String = "وضح الفارق الدلالي الدقيق بين كلمتي (الحزن) و(الأسى) و(الترح) في سياق لغة الضاد.",
    val playgroundOutput: String = "",
    val isPlaygroundLoading: Boolean = false,
    val savedItems: List<SavedLinguisticItem> = emptyList(),
    val snackbarMessage: String? = null,
    val isApiKeyConfigured: Boolean = false
)

class BayanAiViewModel(
    private val repository: BayanAiRepository = BayanAiRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        BayanUiState(
            isApiKeyConfigured = repository.isApiKeyConfigured(),
            messages = listOf(
                ChatMessage(
                    role = "model",
                    text = """
                    أهلاً بك في **بيان**، النموذج اللغوي العربي المتطور 🖋️✨
                    
                    أنا صُممتُ لأكون رفيقك في رحاب لغة الضاد وعلومها الشريفة؛ سواءً أردت:
                    • **حواراً فصيحاً واستدلالاً منطقياً عميقاً**
                    • **تشكيلاً تاماً وضبطاً لأواخر الكلم**
                    • **إعراباً تفصيلياً وتحليلاً نحوياً وصرفياً**
                    • **تدقيقاً بلاغياً ورفعاً لمستوى الفصاحة**
                    • **نظماً للأبيات على بحور الخليل العروضية**
                    • **ترجمة دلالية وثقافية فائقة العناية**
                    
                    اختر ما تشاء من الأدوات، أو ابدأ حوارك المباشر معي الآن!
                    """.trimIndent(),
                    modelType = AiModelType.GEMINI_FLASH
                )
            )
        )
    )
    val uiState: StateFlow<BayanUiState> = _uiState.asStateFlow()

    private var activeGenerationJob: Job? = null

    fun selectTab(tab: AppScreenTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun updateModelType(modelType: AiModelType) {
        _uiState.update {
            it.copy(parameters = it.parameters.copy(modelType = modelType))
        }
    }

    fun updateTemperature(value: Float) {
        _uiState.update {
            it.copy(parameters = it.parameters.copy(temperature = value))
        }
    }

    fun updateTopP(value: Float) {
        _uiState.update {
            it.copy(parameters = it.parameters.copy(topP = value))
        }
    }

    fun updateThinkingLevel(level: String) {
        _uiState.update {
            it.copy(parameters = it.parameters.copy(thinkingLevel = level))
        }
    }

    fun updatePersona(persona: SystemPersona) {
        _uiState.update {
            it.copy(parameters = it.parameters.copy(persona = persona))
        }
    }

    fun updateCustomSystemPrompt(prompt: String) {
        _uiState.update {
            it.copy(parameters = it.parameters.copy(customSystemPrompt = prompt))
        }
    }

    fun clearChat() {
        _uiState.update {
            it.copy(
                messages = listOf(
                    ChatMessage(
                        role = "model",
                        text = "تم بدء جلسة حوارية جديدة مع نموذج بيان اللغوي. كيف يمكنني مساندتك اليوم؟",
                        modelType = it.parameters.modelType
                    )
                )
            )
        }
    }

    fun sendChatMessage(promptText: String) {
        val trimmed = promptText.trim()
        if (trimmed.isEmpty() || _uiState.value.isGenerating) return

        val userMsg = ChatMessage(
            role = "user",
            text = trimmed
        )
        val assistantPlaceholderId = UUID.randomUUID().toString()
        val assistantMsg = ChatMessage(
            id = assistantPlaceholderId,
            role = "model",
            text = "",
            isStreaming = true,
            modelType = _uiState.value.parameters.modelType
        )

        val updatedMessages = _uiState.value.messages + userMsg + assistantMsg
        _uiState.update {
            it.copy(
                messages = updatedMessages,
                isGenerating = true
            )
        }

        val startTime = System.currentTimeMillis()
        activeGenerationJob = viewModelScope.launch {
            val accumulatedText = StringBuilder()
            val history = _uiState.value.messages.dropLast(2) // exclude new user and placeholder

            try {
                repository.streamContent(trimmed, history, _uiState.value.parameters)
                    .collect { chunk ->
                        accumulatedText.append(chunk)
                        val elapsed = System.currentTimeMillis() - startTime
                        _uiState.update { state ->
                            val newMsgs = state.messages.map { msg ->
                                if (msg.id == assistantPlaceholderId) {
                                    msg.copy(
                                        text = accumulatedText.toString(),
                                        isStreaming = true,
                                        latencyMs = elapsed
                                    )
                                } else msg
                            }
                            state.copy(messages = newMsgs)
                        }
                    }
            } catch (e: Exception) {
                accumulatedText.append("\n[خطأ: ${e.message}]")
            } finally {
                val totalElapsed = System.currentTimeMillis() - startTime
                val finalText = accumulatedText.toString().ifBlank {
                    "عذراً، لم يتم استلام رد من النموذج. تأكد من تفعيل مفتاح Gemini API."
                }
                _uiState.update { state ->
                    val finalMsgs = state.messages.map { msg ->
                        if (msg.id == assistantPlaceholderId) {
                            msg.copy(
                                text = finalText,
                                isStreaming = false,
                                latencyMs = totalElapsed,
                                tokensEstimate = (finalText.length / 3).coerceAtLeast(1)
                            )
                        } else msg
                    }
                    state.copy(messages = finalMsgs, isGenerating = false)
                }
            }
        }
    }

    fun stopGeneration() {
        activeGenerationJob?.cancel()
        _uiState.update { state ->
            val msgs = state.messages.map { msg ->
                if (msg.isStreaming) msg.copy(isStreaming = false) else msg
            }
            state.copy(messages = msgs, isGenerating = false)
        }
    }

    // Linguistic Lab Tools
    fun selectLinguisticTool(tool: LinguisticToolType) {
        _uiState.update {
            it.copy(
                selectedTool = tool,
                toolOutput = ""
            )
        }
    }

    fun setToolInput(input: String) {
        _uiState.update { it.copy(toolInput = input) }
    }

    fun executeLinguisticTool() {
        val input = _uiState.value.toolInput.trim()
        if (input.isEmpty() || _uiState.value.isToolLoading) return

        val tool = _uiState.value.selectedTool
        val engineeredPrompt = tool.promptTemplate(input)

        _uiState.update {
            it.copy(
                isToolLoading = true,
                toolOutput = ""
            )
        }

        viewModelScope.launch {
            val result = repository.generateContent(
                prompt = engineeredPrompt,
                parameters = _uiState.value.parameters
            )
            result.fold(
                onSuccess = { output ->
                    _uiState.update {
                        it.copy(
                            toolOutput = output,
                            isToolLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            toolOutput = "تعذر إكمال المعالجة اللغوية:\n${error.localizedMessage ?: error.message}",
                            isToolLoading = false
                        )
                    }
                }
            )
        }
    }

    // Playground
    fun setPlaygroundPrompt(prompt: String) {
        _uiState.update { it.copy(playgroundPrompt = prompt) }
    }

    fun executePlayground() {
        val prompt = _uiState.value.playgroundPrompt.trim()
        if (prompt.isEmpty() || _uiState.value.isPlaygroundLoading) return

        _uiState.update { it.copy(isPlaygroundLoading = true, playgroundOutput = "") }

        viewModelScope.launch {
            val result = repository.generateContent(
                prompt = prompt,
                parameters = _uiState.value.parameters
            )
            result.fold(
                onSuccess = { output ->
                    _uiState.update {
                        it.copy(
                            playgroundOutput = output,
                            isPlaygroundLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            playgroundOutput = "حدث خطأ:\n${error.localizedMessage ?: error.message}",
                            isPlaygroundLoading = false
                        )
                    }
                }
            )
        }
    }

    // Saved Items
    fun saveContent(title: String, content: String, toolType: LinguisticToolType? = null) {
        if (content.isBlank()) return
        val newItem = SavedLinguisticItem(
            title = title.ifBlank { "مقتطف لغوي محفوظ" },
            content = content,
            toolType = toolType
        )
        _uiState.update {
            it.copy(
                savedItems = listOf(newItem) + it.savedItems,
                snackbarMessage = "تم حفظ المقتطف في المحفوظات بنجاح"
            )
        }
    }

    fun deleteSavedItem(id: String) {
        _uiState.update {
            it.copy(
                savedItems = it.savedItems.filterNot { item -> item.id == id },
                snackbarMessage = "تم حذف العنصر"
            )
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun copyToClipboard(context: Context, text: String, label: String = "Bayan AI") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard?.setPrimaryClip(clip)
        _uiState.update { it.copy(snackbarMessage = "تم نسخ النص إلى الحافظة") }
    }
}

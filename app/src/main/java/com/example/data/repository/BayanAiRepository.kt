package com.example.data.repository

import com.example.BuildConfig
import com.example.data.api.RetrofitClient
import com.example.data.model.AiModelType
import com.example.data.model.ChatMessage
import com.example.data.model.Content
import com.example.data.model.GenerateContentRequest
import com.example.data.model.GenerationConfig
import com.example.data.model.ModelHyperparameters
import com.example.data.model.Part
import com.example.data.model.SystemPersona
import com.example.data.model.ThinkingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class BayanAiRepository {

    private val json = Json { ignoreUnknownKeys = true }

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    private fun getEffectiveApiKey(): String {
        return BuildConfig.GEMINI_API_KEY
    }

    private fun buildSystemInstruction(parameters: ModelHyperparameters): Content {
        val instructionText = if (parameters.persona == SystemPersona.CUSTOM) {
            parameters.customSystemPrompt.ifBlank { SystemPersona.FASIH.systemPrompt }
        } else {
            parameters.persona.systemPrompt
        }
        return Content(parts = listOf(Part(text = instructionText)))
    }

    private fun buildGenerationConfig(parameters: ModelHyperparameters): GenerationConfig {
        val thinkingConfig = if (parameters.thinkingLevel != "none") {
            ThinkingConfig(thinkingLevel = parameters.thinkingLevel)
        } else null

        return GenerationConfig(
            temperature = parameters.temperature,
            topP = parameters.topP,
            topK = parameters.topK,
            thinkingConfig = thinkingConfig
        )
    }

    suspend fun generateContent(
        prompt: String,
        history: List<ChatMessage> = emptyList(),
        parameters: ModelHyperparameters
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = getEffectiveApiKey()
        if (!isApiKeyConfigured()) {
            return@withContext Result.failure(
                IllegalStateException("لم يتم ضبط مفتاح Gemini API. يرجى إضافته عبر لوحة الأسرار (Secrets Panel) في AI Studio.")
            )
        }

        val contents = mutableListOf<Content>()
        // Add previous history turns
        for (msg in history.takeLast(10)) {
            val role = if (msg.role == "user") "user" else "model"
            contents.add(Content(role = role, parts = listOf(Part(text = msg.text))))
        }
        // Add latest user prompt
        contents.add(Content(role = "user", parts = listOf(Part(text = prompt))))

        val request = GenerateContentRequest(
            contents = contents,
            generationConfig = buildGenerationConfig(parameters),
            systemInstruction = buildSystemInstruction(parameters)
        )

        try {
            val modelId = parameters.modelType.modelId
            val response = RetrofitClient.service.generateContent(modelId, apiKey, request)
            val outputText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text

            if (!outputText.isNullOrBlank()) {
                Result.success(outputText)
            } else {
                Result.failure(Exception("لم يُرجع النموذج أي نص. حاول إعادة صياغة السؤال أو خفض درجة الحرارة."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun streamContent(
        prompt: String,
        history: List<ChatMessage> = emptyList(),
        parameters: ModelHyperparameters
    ): Flow<String> = flow {
        val apiKey = getEffectiveApiKey()
        if (!isApiKeyConfigured()) {
            emit("تنبيه: لم يتم العثور على مفتاح GEMINI_API_KEY صالح في الإعدادات. يرجى تفعيله في لوحة الأسرار (Secrets Panel).")
            return@flow
        }

        val contents = mutableListOf<Content>()
        for (msg in history.takeLast(10)) {
            val role = if (msg.role == "user") "user" else "model"
            contents.add(Content(role = role, parts = listOf(Part(text = msg.text))))
        }
        contents.add(Content(role = "user", parts = listOf(Part(text = prompt))))

        val request = GenerateContentRequest(
            contents = contents,
            generationConfig = buildGenerationConfig(parameters),
            systemInstruction = buildSystemInstruction(parameters)
        )

        try {
            val modelId = parameters.modelType.modelId
            val responseBody = RetrofitClient.service.generateContentStream(modelId, apiKey, request)
            responseBody.byteStream().bufferedReader().use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val rawLine = line?.trim() ?: continue
                    if (!rawLine.startsWith("data:")) continue
                    val jsonPayload = rawLine.removePrefix("data:").trim()
                    if (jsonPayload.isEmpty() || jsonPayload == "[DONE]") continue
                    try {
                        val chunk = json.parseToJsonElement(jsonPayload).jsonObject
                        val text = chunk["candidates"]?.jsonArray
                            ?.getOrNull(0)?.jsonObject
                            ?.get("content")?.jsonObject
                            ?.get("parts")?.jsonArray
                            ?.getOrNull(0)?.jsonObject
                            ?.get("text")?.jsonPrimitive?.content
                        if (!text.isNullOrEmpty()) {
                            emit(text)
                        }
                    } catch (_: Exception) {
                        // ignore chunk parsing anomalies
                    }
                }
            }
        } catch (e: Exception) {
            emit("\n[حدث خطأ أثناء الاتصال بالنموذج اللغوي: ${e.localizedMessage ?: e.message}]")
        }
    }.flowOn(Dispatchers.IO)
}

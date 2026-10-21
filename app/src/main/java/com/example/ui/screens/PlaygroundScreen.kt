package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiModelType
import com.example.data.model.SystemPersona
import com.example.ui.components.MarkdownArabicText
import com.example.ui.theme.BayanCyanAccent
import com.example.ui.theme.BayanEmeraldAccent
import com.example.ui.theme.BayanGold
import com.example.ui.theme.BayanNavyBase
import com.example.ui.theme.BayanNavyBorder
import com.example.ui.theme.BayanNavyDark
import com.example.ui.theme.BayanNavySurface
import com.example.ui.theme.BayanNavySurfaceVariant
import com.example.ui.viewmodel.BayanAiViewModel
import com.example.ui.viewmodel.BayanUiState

@Composable
fun PlaygroundScreen(
    uiState: BayanUiState,
    viewModel: BayanAiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BayanNavyDark)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "مختبر المعايير وهندسة الأوامر",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = BayanGold,
                fontWeight = FontWeight.Bold,
                textDirection = TextDirection.Rtl
            )
        )
        Text(
            text = "اختبر سلوك النموذج اللغوي مع تغير معاملات التفكير والحرية والمقام",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDirection = TextDirection.Rtl
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Model Switcher Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BayanNavySurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BayanNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "محرك الذكاء الاصطناعي الأساسي",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = BayanGold,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AiModelType.entries.forEach { model ->
                        val isSelected = uiState.parameters.modelType == model
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) BayanNavySurfaceVariant else BayanNavyDark,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) BayanGold else BayanNavyBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.updateModelType(model) }
                                .padding(12.dp)
                                .testTag("select_model_${model.name}")
                        ) {
                            Column {
                                Text(
                                    text = model.titleAr,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = if (isSelected) BayanGold else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = model.badge,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) BayanCyanAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hyperparameters Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BayanNavySurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BayanNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ضبط معايير التوليد اللغوي",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = BayanGold,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Temperature
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("درجة الإبداع (Temperature):")
                    Text(
                        String.format("%.2f", uiState.parameters.temperature),
                        color = BayanGold,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = uiState.parameters.temperature,
                    onValueChange = { viewModel.updateTemperature(it) },
                    valueRange = 0.0f..1.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = BayanGold,
                        activeTrackColor = BayanGold,
                        inactiveTrackColor = BayanNavySurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Top-P
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("عتبة الاحتمال (Top-P):")
                    Text(
                        String.format("%.2f", uiState.parameters.topP),
                        color = BayanCyanAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = uiState.parameters.topP,
                    onValueChange = { viewModel.updateTopP(it) },
                    valueRange = 0.1f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = BayanCyanAccent,
                        activeTrackColor = BayanCyanAccent,
                        inactiveTrackColor = BayanNavySurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Thinking Level
                Text(
                    text = "تأمل النموذج والاستدلال (Thinking Level):",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("none" to "مباشر", "low" to "متوسط", "high" to "عميق").forEach { (key, label) ->
                        val isSelected = uiState.parameters.thinkingLevel == key
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) BayanNavySurfaceVariant else BayanNavyDark,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) BayanGold else BayanNavyBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.updateThinkingLevel(key) }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isSelected) BayanGold else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Prompt Execution Box
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BayanNavySurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BayanNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "أمر التجربة والاختبار",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = BayanGold,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.playgroundPrompt,
                    onValueChange = { viewModel.setPlaygroundPrompt(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("playground_input_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BayanGold,
                        unfocusedBorderColor = BayanNavyBorder,
                        focusedContainerColor = BayanNavyDark,
                        unfocusedContainerColor = BayanNavyDark,
                        cursorColor = BayanGold
                    ),
                    minLines = 3,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textDirection = TextDirection.Rtl
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.executePlayground() },
                    enabled = uiState.playgroundPrompt.isNotBlank() && !uiState.isPlaygroundLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BayanGold,
                        contentColor = BayanNavyDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("run_playground_button")
                ) {
                    if (uiState.isPlaygroundLoading) {
                        CircularProgressIndicator(
                            color = BayanNavyDark,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("النموذج ينفذ الأمر...")
                    } else {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تشغيل واختبار المعايير", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Playground Output
        AnimatedVisibility(visible = uiState.playgroundOutput.isNotEmpty() || uiState.isPlaygroundLoading) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BayanNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, BayanGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "استجابة النموذج اللغوي",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = BayanGold,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        if (uiState.playgroundOutput.isNotEmpty()) {
                            Row {
                                IconButton(onClick = { viewModel.copyToClipboard(context, uiState.playgroundOutput) }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = {
                                    viewModel.saveContent(
                                        title = "تجربة مختبر: ${uiState.playgroundPrompt.take(20)}...",
                                        content = uiState.playgroundOutput
                                    )
                                }) {
                                    Icon(Icons.Default.BookmarkAdd, contentDescription = "حفظ", tint = BayanGold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.isPlaygroundLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = BayanGold)
                        }
                    } else {
                        MarkdownArabicText(
                            text = uiState.playgroundOutput,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

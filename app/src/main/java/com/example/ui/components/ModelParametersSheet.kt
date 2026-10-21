package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModelHyperparameters
import com.example.data.model.SystemPersona
import com.example.ui.theme.BayanCyanAccent
import com.example.ui.theme.BayanGold
import com.example.ui.theme.BayanNavyBase
import com.example.ui.theme.BayanNavyBorder
import com.example.ui.theme.BayanNavyDark
import com.example.ui.theme.BayanNavySurface
import com.example.ui.theme.BayanNavySurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelParametersSheet(
    parameters: ModelHyperparameters,
    onTemperatureChange: (Float) -> Unit,
    onTopPChange: (Float) -> Unit,
    onThinkingLevelChange: (String) -> Unit,
    onPersonaChange: (SystemPersona) -> Unit,
    onCustomPromptChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BayanNavySurface,
        modifier = Modifier.testTag("model_parameters_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "معايير النموذج والتوجيه اللغوي",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = BayanGold,
                    fontWeight = FontWeight.Bold,
                    textDirection = TextDirection.Rtl
                )
            )
            Text(
                text = "تحكم في سلوك النموذج، درجة حريته، ومقامه البلاغي والنحوي",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDirection = TextDirection.Rtl
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Temperature Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "درجة الإبداع والحرية (Temperature):",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = String.format("%.2f", parameters.temperature),
                    style = MaterialTheme.typography.bodyMedium.copy(color = BayanGold, fontWeight = FontWeight.Bold)
                )
            }
            Slider(
                value = parameters.temperature,
                onValueChange = onTemperatureChange,
                valueRange = 0.0f..1.5f,
                colors = SliderDefaults.colors(
                    thumbColor = BayanGold,
                    activeTrackColor = BayanGold,
                    inactiveTrackColor = BayanNavySurfaceVariant
                ),
                modifier = Modifier.testTag("temperature_slider")
            )
            Text(
                text = "0.0: دقة لغوية ونحوية صارمة • 0.7: توازن مثالي • 1.2+: إبداع شعري وتخييلي عالٍ",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textDirection = TextDirection.Rtl
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Top-P Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "نطاق العينات الاحتمالية (Top-P):",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = String.format("%.2f", parameters.topP),
                    style = MaterialTheme.typography.bodyMedium.copy(color = BayanCyanAccent, fontWeight = FontWeight.Bold)
                )
            }
            Slider(
                value = parameters.topP,
                onValueChange = onTopPChange,
                valueRange = 0.1f..1.0f,
                colors = SliderDefaults.colors(
                    thumbColor = BayanCyanAccent,
                    activeTrackColor = BayanCyanAccent,
                    inactiveTrackColor = BayanNavySurfaceVariant
                ),
                modifier = Modifier.testTag("top_p_slider")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Thinking Level
            Text(
                text = "مستوى التفكير والتأمل اللغوي (Thinking Level):",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "none" to "مباشر",
                    "low" to "تأمل معتدل",
                    "high" to "استدلال عميق"
                ).forEach { (levelKey, levelLabel) ->
                    val isSelected = parameters.thinkingLevel == levelKey
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
                            .clickable { onThinkingLevelChange(levelKey) }
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = levelLabel,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) BayanGold else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // System Persona
            Text(
                text = "المقام والتوجيه النظامي (Persona):",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SystemPersona.entries.forEach { persona ->
                    val isSelected = parameters.persona == persona
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) BayanNavySurfaceVariant else BayanNavyDark,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) BayanGold else BayanNavyBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onPersonaChange(persona) }
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = persona.titleAr,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = if (isSelected) BayanGold else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    textDirection = TextDirection.Rtl
                                )
                            )
                            Text(
                                text = persona.descriptionAr,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textDirection = TextDirection.Rtl
                                )
                            )
                        }
                    }
                }
            }

            if (parameters.persona == SystemPersona.CUSTOM) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = parameters.customSystemPrompt,
                    onValueChange = onCustomPromptChange,
                    label = { Text("أدخل التوجيه النظامي المخصص للنموذج") },
                    placeholder = { Text("مثال: أنت خبير في نقد الشعر الأندلسي...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_prompt_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BayanGold,
                        unfocusedBorderColor = BayanNavyBorder,
                        focusedContainerColor = BayanNavyDark,
                        unfocusedContainerColor = BayanNavyDark
                    ),
                    minLines = 3
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Subject
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Translate
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LinguisticToolType
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
fun LinguisticLabScreen(
    uiState: BayanUiState,
    viewModel: BayanAiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val toolSamples = mapOf(
        LinguisticToolType.TASHKEEL to "ان العلم نور يهدي العقول ويسمو بالنفوس الى اعلى المراتب",
        LinguisticToolType.EIRAB to "وما نيل المطالب بالتمني ولكن تؤخذ الدنيا غلابا",
        LinguisticToolType.BALAGHA to "نحن نود ان نطلب منكم ان تساعدونا في حل هذه المشكلة الصعبة جدا",
        LinguisticToolType.POETRY to "اكتب قصيدة في الشوق والحنين إلى أرض الوطن على بحر الوافر",
        LinguisticToolType.TRANSLATION to "Wisdom is not a product of schooling but of the lifelong attempt to acquire it.",
        LinguisticToolType.SUMMARIZATION to "اللغة العربية إحدى أكثر اللغات انتشاراً في العالم، وهي ركن من أركان التنوع الثقافي للبشرية، وتزخر بأكثر من اثني عشر مليون كلمة في معجمها، وتمتاز بخصائص فريدة في الاشتقاق والإعراب والتصريف والبلاغة الصوتية."
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BayanNavyDark)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Hero Header
        Text(
            text = "المختبر اللغوي المتخصص",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = BayanGold,
                fontWeight = FontWeight.Bold,
                textDirection = TextDirection.Rtl
            )
        )
        Text(
            text = "أدوات لغوية فائقة التخصص مدعومة بذكاء اصطناعي رصين في علوم الضاد",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDirection = TextDirection.Rtl
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tool Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LinguisticToolType.entries.forEach { tool ->
                val isSelected = uiState.selectedTool == tool
                val icon = when (tool) {
                    LinguisticToolType.TASHKEEL -> Icons.Default.FormatQuote
                    LinguisticToolType.EIRAB -> Icons.Default.AccountTree
                    LinguisticToolType.BALAGHA -> Icons.Default.AutoAwesome
                    LinguisticToolType.POETRY -> Icons.AutoMirrored.Filled.MenuBook
                    LinguisticToolType.TRANSLATION -> Icons.Default.Translate
                    LinguisticToolType.SUMMARIZATION -> Icons.AutoMirrored.Filled.Subject
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) BayanGold else BayanNavySurface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) BayanGold else BayanNavyBorder,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { viewModel.selectLinguisticTool(tool) }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("tool_tab_${tool.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = tool.titleAr,
                            tint = if (isSelected) BayanNavyDark else BayanGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tool.titleAr,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) BayanNavyDark else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Active Tool Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BayanNavySurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BayanNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = uiState.selectedTool.titleAr,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = BayanGold,
                                fontWeight = FontWeight.Bold,
                                textDirection = TextDirection.Rtl
                            )
                        )
                        Text(
                            text = uiState.selectedTool.subtitleAr,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textDirection = TextDirection.Rtl
                            )
                        )
                    }

                    // Sample quick load button
                    val sample = toolSamples[uiState.selectedTool]
                    if (sample != null) {
                        Text(
                            text = "استخدم مثالاً",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = BayanCyanAccent,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { viewModel.setToolInput(sample) }
                                .padding(4.dp)
                                .testTag("use_sample_button")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.toolInput,
                    onValueChange = { viewModel.setToolInput(it) },
                    placeholder = {
                        Text(
                            text = uiState.selectedTool.placeholderAr,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                textDirection = TextDirection.Rtl
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tool_input_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BayanGold,
                        unfocusedBorderColor = BayanNavyBorder,
                        focusedContainerColor = BayanNavyDark,
                        unfocusedContainerColor = BayanNavyDark,
                        cursorColor = BayanGold
                    ),
                    minLines = 4,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textDirection = TextDirection.Rtl
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { viewModel.executeLinguisticTool() },
                    enabled = uiState.toolInput.isNotBlank() && !uiState.isToolLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BayanGold,
                        contentColor = BayanNavyDark,
                        disabledContainerColor = BayanNavySurfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("execute_tool_button")
                ) {
                    if (uiState.isToolLoading) {
                        CircularProgressIndicator(
                            color = BayanNavyDark,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("النموذج يعالج النص لغوياً...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "معالجة وتحليل بواسطة بيان",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Output Card
        AnimatedVisibility(visible = uiState.toolOutput.isNotEmpty() || uiState.isToolLoading) {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(BayanEmeraldAccent)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "النتيجة اللغوية المعتمدة",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = BayanGold,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        if (uiState.toolOutput.isNotEmpty()) {
                            Row {
                                IconButton(
                                    onClick = { viewModel.copyToClipboard(context, uiState.toolOutput) },
                                    modifier = Modifier.testTag("copy_tool_output")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "نسخ النتيجة",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.saveContent(
                                            title = uiState.selectedTool.titleAr,
                                            content = uiState.toolOutput,
                                            toolType = uiState.selectedTool
                                        )
                                    },
                                    modifier = Modifier.testTag("save_tool_output")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkAdd,
                                        contentDescription = "حفظ في المحفوظات",
                                        tint = BayanGold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (uiState.isToolLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = BayanGold)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "جاري التدقيق النحوي والبلاغي...",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = BayanGold)
                                )
                            }
                        }
                    } else {
                        MarkdownArabicText(
                            text = uiState.toolOutput,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

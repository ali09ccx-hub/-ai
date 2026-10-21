package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiModelType
import com.example.ui.theme.BayanCyanAccent
import com.example.ui.theme.BayanGold
import com.example.ui.theme.BayanNavyBase
import com.example.ui.theme.BayanNavyBorder
import com.example.ui.theme.BayanNavyDark
import com.example.ui.theme.BayanNavySurface
import com.example.ui.theme.BayanNavySurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BayanTopAppBar(
    selectedModel: AiModelType,
    onModelSelected: (AiModelType) -> Unit,
    onOpenParameters: () -> Unit,
    onClearChat: () -> Unit,
    showClearOption: Boolean = true,
    isApiKeySet: Boolean = true
) {
    var showModelDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(BayanNavyDark)
                        .border(1.5.dp, BayanGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Bayan AI Logo",
                        tint = BayanGold,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "بيان AI",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BayanGold
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isApiKeySet) BayanCyanAccent.copy(alpha = 0.2f) else BayanGold.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isApiKeySet) "متصل" else "تجهيز",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isApiKeySet) BayanCyanAccent else BayanGold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    // Model Selector Chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BayanNavySurfaceVariant,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showModelDialog = true }
                            .testTag("model_selector_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = selectedModel.badge,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "تغيير النموذج",
                                tint = BayanGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        },
        actions = {
            if (showClearOption) {
                IconButton(
                    onClick = onClearChat,
                    modifier = Modifier.testTag("clear_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "مسح المحادثة",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = onOpenParameters,
                modifier = Modifier.testTag("open_parameters_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "معايير النموذج والتوجيه",
                    tint = BayanGold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BayanNavyBase,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )

    if (showModelDialog) {
        AlertDialog(
            onDismissRequest = { showModelDialog = false },
            title = {
                Text(
                    text = "اختر محرك الذكاء الاصطناعي",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = BayanGold,
                        textDirection = TextDirection.Rtl
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AiModelType.entries.forEach { model ->
                        val isSelected = model == selectedModel
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) BayanNavySurfaceVariant else BayanNavyDark,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) BayanGold else BayanNavyBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onModelSelected(model)
                                    showModelDialog = false
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = model.titleAr,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = if (isSelected) BayanGold else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Bold,
                                            textDirection = TextDirection.Rtl
                                        )
                                    )
                                    Text(
                                        text = model.descriptionAr,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textDirection = TextDirection.Rtl
                                        )
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "محدد",
                                        tint = BayanGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showModelDialog = false }) {
                    Text("إغلاق", color = BayanGold)
                }
            },
            containerColor = BayanNavySurface
        )
    }
}

package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.example.ui.components.MarkdownArabicText
import com.example.ui.theme.BayanCyanAccent
import com.example.ui.theme.BayanGold
import com.example.ui.theme.BayanNavyBorder
import com.example.ui.theme.BayanNavyDark
import com.example.ui.theme.BayanNavySurface
import com.example.ui.theme.BayanNavySurfaceVariant
import com.example.ui.viewmodel.BayanAiViewModel
import com.example.ui.viewmodel.BayanUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedScreen(
    uiState: BayanUiState,
    viewModel: BayanAiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val filteredItems = uiState.savedItems.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BayanNavyDark)
            .padding(16.dp)
    ) {
        Text(
            text = "المحفوظات والذخائر اللغوية",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = BayanGold,
                fontWeight = FontWeight.Bold,
                textDirection = TextDirection.Rtl
            )
        )
        Text(
            text = "مجموعتك الخاصة من القصائد، الإعرابات، والنتائج البلاغية المحفوظة",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDirection = TextDirection.Rtl
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (uiState.savedItems.isNotEmpty()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "ابحث في المحفوظات...",
                        style = MaterialTheme.typography.bodyMedium.copy(textDirection = TextDirection.Rtl)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = BayanGold
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_saved_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BayanGold,
                    unfocusedBorderColor = BayanNavyBorder,
                    focusedContainerColor = BayanNavySurface,
                    unfocusedContainerColor = BayanNavySurface
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = BayanGold.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (uiState.savedItems.isEmpty()) "لم تحفظ أي مقتطفات لغوية بعد" else "لا توجد نتائج مطابقة لبحثك",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = BayanGold,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "عندما يُنتج لك نموذج بيان قصيدة، إعراباً، أو دراسة بلاغية راقتك؛ اضغط على زر الحفظ للرجوع إليها في أي وقت.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            textDirection = TextDirection.Rtl
                        )
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = BayanNavySurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BayanNavyBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = BayanGold,
                                        fontWeight = FontWeight.Bold,
                                        textDirection = TextDirection.Rtl
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                Row {
                                    IconButton(
                                        onClick = { viewModel.copyToClipboard(context, item.content) },
                                        modifier = Modifier.testTag("copy_saved_item")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "نسخ",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteSavedItem(item.id) },
                                        modifier = Modifier.testTag("delete_saved_item")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "حذف",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }

                            val dateFormat = SimpleDateFormat("dd MMMM yyyy - HH:mm", Locale.forLanguageTag("ar"))
                            Text(
                                text = dateFormat.format(Date(item.timestamp)),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BayanCyanAccent.copy(alpha = 0.7f)
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            MarkdownArabicText(
                                text = item.content,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

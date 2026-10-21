package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.theme.BayanCyanAccent
import com.example.ui.theme.BayanGold
import com.example.ui.theme.BayanNavyBase
import com.example.ui.theme.BayanNavyBorder
import com.example.ui.theme.BayanNavyDark
import com.example.ui.theme.BayanNavySurface
import com.example.ui.theme.BayanNavySurfaceVariant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatBubble(
    message: ChatMessage,
    onCopy: (String) -> Unit,
    onSave: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUser = message.role == "user"

    val shape = if (isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
    }

    val bubbleBg = if (isUser) BayanNavySurfaceVariant else BayanNavySurface
    val borderModifier = if (isUser) {
        Modifier.border(1.dp, BayanCyanAccent.copy(alpha = 0.4f), shape)
    } else {
        Modifier.border(1.dp, BayanGold.copy(alpha = 0.3f), shape)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(BayanNavyDark)
                    .border(1.dp, BayanGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Bayan AI",
                    tint = BayanGold,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = shape,
            color = bubbleBg,
            tonalElevation = if (isUser) 4.dp else 2.dp,
            modifier = Modifier
                .weight(1f, fill = false)
                .then(borderModifier)
                .testTag(if (isUser) "user_chat_bubble" else "model_chat_bubble")
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                // Header tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "أنت" else message.modelType.titleAr,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isUser) BayanCyanAccent else BayanGold,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    Text(
                        text = timeFormat.format(Date(message.timestamp)),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    )
                }

                // Body content
                Box(modifier = Modifier.padding(top = 6.dp)) {
                    if (message.text.isEmpty() && message.isStreaming) {
                        StreamingDotsIndicator()
                    } else {
                        MarkdownArabicText(
                            text = message.text,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Latency & tokens info if assistant
                if (!isUser) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (message.isStreaming) {
                                MiniPulsingDot()
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "جارٍ البث الحي...",
                                    style = MaterialTheme.typography.labelSmall.copy(color = BayanGold)
                                )
                            } else if (message.latencyMs != null) {
                                Text(
                                    text = "${message.latencyMs}ms • ~${message.tokensEstimate} رمز",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }

                        // Copy & Save Actions
                        if (message.text.isNotEmpty()) {
                            Row {
                                IconButton(
                                    onClick = { onCopy(message.text) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("copy_bubble_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "نسخ النص",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val snippet = message.text.take(30) + "..."
                                        onSave(snippet, message.text)
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("save_bubble_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkAdd,
                                        contentDescription = "حفظ المقتطف",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(BayanNavyDark)
                    .border(1.dp, BayanCyanAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "المستخدم",
                    tint = BayanCyanAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun MiniPulsingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(BayanGold.copy(alpha = alpha))
    )
}

@Composable
fun StreamingDotsIndicator() {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) {
            MiniPulsingDot()
        }
        Text(
            text = "النموذج اللغوي يفكر ويبث الإجابة...",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = BayanGold,
                textDirection = TextDirection.Rtl
            )
        )
    }
}

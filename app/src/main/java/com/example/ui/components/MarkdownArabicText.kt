package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BayanGold
import com.example.ui.theme.BayanNavySurfaceVariant
import com.example.ui.theme.CairoFontFamily

@Composable
fun MarkdownArabicText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val lines = text.split("\n")

    Column(modifier = modifier.fillMaxWidth()) {
        var inCodeBlock = false
        val codeBlockLines = mutableListOf<String>()

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("```")) {
                if (inCodeBlock) {
                    // Flush code block
                    CodeBlockView(codeBlockLines.joinToString("\n"))
                    codeBlockLines.clear()
                    inCodeBlock = false
                } else {
                    inCodeBlock = true
                }
                continue
            }

            if (inCodeBlock) {
                codeBlockLines.add(line)
                continue
            }

            if (trimmed.startsWith("### ")) {
                Text(
                    text = trimmed.removePrefix("### "),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = BayanGold,
                        fontWeight = FontWeight.Bold,
                        textDirection = TextDirection.Rtl
                    ),
                    modifier = Modifier.padding(vertical = 4.dp),
                    textAlign = TextAlign.Right
                )
            } else if (trimmed.startsWith("## ")) {
                Text(
                    text = trimmed.removePrefix("## "),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = BayanGold,
                        fontWeight = FontWeight.Bold,
                        textDirection = TextDirection.Rtl
                    ),
                    modifier = Modifier.padding(vertical = 6.dp),
                    textAlign = TextAlign.Right
                )
            } else if (trimmed.startsWith("# ")) {
                Text(
                    text = trimmed.removePrefix("# "),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = BayanGold,
                        fontWeight = FontWeight.Bold,
                        textDirection = TextDirection.Rtl
                    ),
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Right
                )
            } else {
                val annotatedString = parseMarkdownInLine(line, color)
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 26.sp,
                        textDirection = TextDirection.Rtl
                    ),
                    modifier = Modifier.padding(vertical = 2.dp),
                    textAlign = TextAlign.Right
                )
            }
        }

        if (inCodeBlock && codeBlockLines.isNotEmpty()) {
            CodeBlockView(codeBlockLines.joinToString("\n"))
        }
    }
}

@Composable
private fun CodeBlockView(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(BayanNavySurfaceVariant)
            .padding(12.dp)
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                color = BayanGold,
                textDirection = TextDirection.Ltr
            )
        )
    }
}

private fun parseMarkdownInLine(line: String, defaultColor: Color) = buildAnnotatedString {
    var i = 0
    while (i < line.length) {
        if (i + 1 < line.length && line[i] == '*' && line[i + 1] == '*') {
            val end = line.indexOf("**", i + 2)
            if (end != -1) {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = BayanGold
                    )
                ) {
                    append(line.substring(i + 2, end))
                }
                i = end + 2
                continue
            }
        } else if (line[i] == '`') {
            val end = line.indexOf('`', i + 1)
            if (end != -1) {
                withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF38BDF8),
                        background = Color(0x331E3A5F)
                    )
                ) {
                    append(line.substring(i + 1, end))
                }
                i = end + 1
                continue
            }
        }
        append(line[i])
        i++
    }
}

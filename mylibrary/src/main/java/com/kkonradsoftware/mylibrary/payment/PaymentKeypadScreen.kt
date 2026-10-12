package com.kkonradsoftware.mylibrary.payment

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ekran wejścia kwoty / PIN-u w stylu klawiatury terminala płatniczego.
 *
 * Samodzielny widok - organizacja przepływek (nawigacja między ekranami)
 * pozostawiamy po stronie aplikacji.
 *
 * @param onConfirm wywoływane po zatwierdzeniu wpisanej wartości
 *                  (cyfry bez separatorów, np. "1234" dla 12,34 zł)
 */
@Composable
fun PaymentKeypadScreen(
    onConfirm: (String) -> Unit,
    onCancel: (() -> Unit)? = null,
    title: String = "Wprowadź kwotę",
    subtitle: String = "",
    currencySymbol: String = "zł",
    maxLength: Int = 7,
    confirmLabel: String = "Zatwierdź",
    initialValue: String = "",
    modifier: Modifier = Modifier,
) {
    var digits by remember { mutableStateOf(initialValue.filter(Char::isDigit).take(maxLength)) }

    fun append(digit: String) {
        if (digits.length < maxLength) digits += digit
    }

    PaymentTerminalTheme {
        val colors = MaterialTheme.colorScheme
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (onCancel != null) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    TextButton(onClick = onCancel) {
                        Text("Anuluj", color = colors.onSurfaceVariant)
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = title,
                color = colors.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subtitle, color = colors.onSurfaceVariant, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .border(1.dp, colors.outline, CircleShape)
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = formatAmount(digits, currencySymbol),
                    color = if (digits.isEmpty()) colors.onSurfaceVariant else colors.primary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            KeypadGrid(
                onDigit = ::append,
                onClear = { digits = "" },
                onBackspace = { if (digits.isNotEmpty()) digits = digits.dropLast(1) },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onConfirm(digits) },
                enabled = digits.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.surfaceVariant,
                    disabledContentColor = colors.onSurfaceVariant,
                ),
                shape = CircleShape,
            ) {
                Text(confirmLabel, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun KeypadGrid(
    onDigit: (String) -> Unit,
    onClear: () -> Unit,
    onBackspace: () -> Unit,
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("C", "0", "<"),
    )
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { key ->
                    Box(modifier = Modifier.weight(1f)) {
                        when (key) {
                            "C" -> KeypadKey(label = "C", onClick = onClear)
                            "<" -> KeypadKey(icon = true, onClick = onBackspace)
                            else -> KeypadKey(label = key, onClick = { onDigit(key) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeypadKey(
    label: String? = null,
    icon: Boolean = false,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.surfaceVariant,
            contentColor = colors.onSurface,
        ),
        shape = CircleShape,
        border = BorderStroke(1.dp, colors.outline),
    ) {
        if (icon) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Backspace,
                contentDescription = "Cofnij",
                modifier = Modifier.size(28.dp),
            )
        } else {
            Text(text = label.orEmpty(), fontSize = 28.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

internal fun formatAmount(digits: String, currencySymbol: String): String {
    if (digits.isEmpty()) return "0,00 $currencySymbol"
    val padded = digits.padStart(3, '0')
    val grosze = padded.takeLast(2)
    val zloty = padded.dropLast(2).ifEmpty { "0" }
    val grouped = zloty.reversed().chunked(3).joinToString(" ").reversed()
    return "$grouped,$grosze $currencySymbol"
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0B0F14,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark",
)
@Composable
private fun PaymentKeypadScreenDarkPreview() {
    PaymentKeypadScreen(onConfirm = {}, onCancel = {})
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF4F6F8,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light",
)
@Composable
private fun PaymentKeypadScreenLightPreview() {
    PaymentKeypadScreen(onConfirm = {}, onCancel = {})
}

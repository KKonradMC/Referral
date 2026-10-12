package com.kkonradsoftware.mylibrary.payment

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ekran potwierdzenia płatności z trzema możliwymi statusami:
 * [PaymentStatus.Success], [PaymentStatus.Error], [PaymentStatus.CardDeclined].
 */
@Composable
fun PaymentResultScreen(
    status: PaymentStatus,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: (() -> Unit)? = null,
    amount: String? = null,
    primaryLabel: String? = null,
    secondaryLabel: String? = null,
    modifier: Modifier = Modifier,
) {
    PaymentTerminalTheme {
        val colors = MaterialTheme.colorScheme
        val accent = status.accentColor()
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = status.icon(),
                    contentDescription = status.accessibilityLabel(),
                    tint = accent,
                    modifier = Modifier.size(72.dp),
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = status.title(),
                color = colors.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = status.description(),
                color = colors.onSurfaceVariant,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )

            if (amount != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = amount,
                    color = accent,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onPrimaryAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = if (accent.luminance() > 0.5f) Color.Black else Color.White,
                ),
                shape = CircleShape,
            ) {
                Text(
                    text = primaryLabel ?: status.defaultPrimaryLabel(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            if (onSecondaryAction != null) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onSecondaryAction,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = secondaryLabel ?: "Zamknij",
                        color = colors.onSurfaceVariant,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

private fun PaymentStatus.icon(): ImageVector = when (this) {
    is PaymentStatus.Success -> Icons.Rounded.CheckCircle
    is PaymentStatus.Error -> Icons.Rounded.Error
    is PaymentStatus.CardDeclined -> Icons.Rounded.Block
}

private fun PaymentStatus.title(): String = when (this) {
    is PaymentStatus.Success -> "Płatność zatwierdzona"
    is PaymentStatus.Error -> "Błąd płatności"
    is PaymentStatus.CardDeclined -> "Karta odrzucona"
}

private fun PaymentStatus.description(): String = when (this) {
    is PaymentStatus.Success -> "Transakcja została zakończona pomyślnie"
    is PaymentStatus.Error -> message ?: "Wystąpił nieoczekiwany błąd. Spróbuj ponownie."
    is PaymentStatus.CardDeclined -> "Bank wydał kartę. Użyj innej karty lub formy płatności."
}

private fun PaymentStatus.defaultPrimaryLabel(): String = when (this) {
    is PaymentStatus.Success -> "Gotowe"
    is PaymentStatus.Error -> "Spróbuj ponownie"
    is PaymentStatus.CardDeclined -> "Zmień kartę"
}

private fun PaymentStatus.accessibilityLabel(): String = when (this) {
    is PaymentStatus.Success -> "Sukces"
    is PaymentStatus.Error -> "Błąd"
    is PaymentStatus.CardDeclined -> "Karta odrzucona"
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0B0F14,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Success",
)
@Composable
private fun PaymentResultSuccessPreview() {
    PaymentResultScreen(
        status = PaymentStatus.Success,
        amount = "12,34 zł",
        onPrimaryAction = {},
        onSecondaryAction = {},
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0B0F14,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Error",
)
@Composable
private fun PaymentResultErrorPreview() {
    PaymentResultScreen(
        status = PaymentStatus.Error("Brak połączenia z terminalem"),
        amount = "12,34 zł",
        onPrimaryAction = {},
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0B0F14,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Card declined",
)
@Composable
private fun PaymentResultDeclinedPreview() {
    PaymentResultScreen(
        status = PaymentStatus.CardDeclined,
        amount = "12,34 zł",
        onPrimaryAction = {},
        onSecondaryAction = {},
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF4F6F8,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Success (light)",
)
@Composable
private fun PaymentResultSuccessLightPreview() {
    PaymentResultScreen(
        status = PaymentStatus.Success,
        amount = "12,34 zł",
        onPrimaryAction = {},
        onSecondaryAction = {},
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF4F6F8,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Card declined (light)",
)
@Composable
private fun PaymentResultDeclinedLightPreview() {
    PaymentResultScreen(
        status = PaymentStatus.CardDeclined,
        amount = "12,34 zł",
        onPrimaryAction = {},
        onSecondaryAction = {},
    )
}

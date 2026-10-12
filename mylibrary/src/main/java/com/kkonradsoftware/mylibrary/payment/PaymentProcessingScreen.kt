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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ekran procesowania płatności.
 * Widok jest czysto deklaratywny - wynik operacji przekazuje zewnętrzna
 * warstwa sterująca (np. zmieniając ten ekran na [PaymentResultScreen]).
 *
 * @param onCancel opcjonalna akcja anulowania; gdy null, przycisk nie jest widoczny
 */
@Composable
fun PaymentProcessingScreen(
    title: String = "Przetwarzanie płatności",
    message: String = "Proszę nie wyjmować karty...",
    amount: String? = null,
    onCancel: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    PaymentTerminalTheme {
        val colors = MaterialTheme.colorScheme
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(72.dp),
                color = colors.primary,
                strokeWidth = 6.dp,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = title,
                color = colors.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                color = colors.onSurfaceVariant,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            )

            if (amount != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = amount,
                    color = colors.primary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                if (onCancel != null) {
                    TextButton(onClick = onCancel) {
                        Text("Anuluj", color = colors.onSurfaceVariant, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0B0F14,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark",
)
@Composable
private fun PaymentProcessingScreenDarkPreview() {
    PaymentProcessingScreen(amount = "12,34 zł", onCancel = {})
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF4F6F8,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light",
)
@Composable
private fun PaymentProcessingScreenLightPreview() {
    PaymentProcessingScreen(amount = "12,34 zł", onCancel = {})
}

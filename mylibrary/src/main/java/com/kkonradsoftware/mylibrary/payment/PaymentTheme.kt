package com.kkonradsoftware.mylibrary.payment

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    background = Color(0xFF0B0F14),
    onBackground = Color(0xFFE6EDF3),
    surface = Color(0xFF151B23),
    onSurface = Color(0xFFE6EDF3),
    surfaceVariant = Color(0xFF1F2732),
    onSurfaceVariant = Color(0xFF8B98A5),
    outline = Color(0xFF2C3644),
    primary = Color(0xFF22C55E),
    onPrimary = Color.Black,
    error = Color(0xFFEF4444),
    onError = Color.White,
)

private val LightColors = lightColorScheme(
    background = Color(0xFFF4F6F8),
    onBackground = Color(0xFF0D1117),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0D1117),
    surfaceVariant = Color(0xFFE6EBF0),
    onSurfaceVariant = Color(0xFF5B6B7B),
    outline = Color(0xFFD0D7DE),
    primary = Color(0xFF16A34A),
    onPrimary = Color.White,
    error = Color(0xFFDC2626),
    onError = Color.White,
)

internal data class PaymentStatusColors(
    val success: Color,
    val error: Color,
    val declined: Color,
)

private val DarkStatusColors = PaymentStatusColors(
    success = Color(0xFF22C55E),
    error = Color(0xFFEF4444),
    declined = Color(0xFFF59E0B),
)

private val LightStatusColors = PaymentStatusColors(
    success = Color(0xFF15803D),
    error = Color(0xFFB91C1C),
    declined = Color(0xFFB45309),
)

internal val LocalPaymentStatusColors = staticCompositionLocalOf { DarkStatusColors }

@Composable
internal fun PaymentTerminalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = {
            CompositionLocalProvider(
                LocalPaymentStatusColors provides if (darkTheme) DarkStatusColors else LightStatusColors,
                content = content,
            )
        },
    )
}

@Composable
internal fun PaymentStatus.accentColor(): Color {
    val colors = LocalPaymentStatusColors.current
    return when (this) {
        is PaymentStatus.Success -> colors.success
        is PaymentStatus.Error -> colors.error
        is PaymentStatus.CardDeclined -> colors.declined
    }
}

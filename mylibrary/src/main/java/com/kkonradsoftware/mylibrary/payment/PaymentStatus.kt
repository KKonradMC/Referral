package com.kkonradsoftware.mylibrary.payment

/**
 * Wynik próby płatności prezentowany na ekranie potwierdzenia.
 */
sealed interface PaymentStatus {

    data object Success : PaymentStatus

    data class Error(val message: String? = null) : PaymentStatus

    data object CardDeclined : PaymentStatus
}

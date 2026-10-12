package com.kkonradsoftware.mylibrary.payment

import org.junit.Assert.assertEquals
import org.junit.Test

class PaymentAmountFormatTest {

    @Test
    fun `empty input shows zero amount`() {
        assertEquals("0,00 zł", formatAmount("", "zł"))
    }

    @Test
    fun `single digit is treated as grosze`() {
        assertEquals("0,05 zł", formatAmount("5", "zł"))
    }

    @Test
    fun `two digits are grosze only`() {
        assertEquals("0,99 zł", formatAmount("99", "zł"))
    }

    @Test
    fun `three digits split into zloty and grosze`() {
        assertEquals("1,23 zł", formatAmount("123", "zł"))
    }

    @Test
    fun `large amounts are grouped with space`() {
        assertEquals("12 345,67 zł", formatAmount("1234567", "zł"))
    }
}

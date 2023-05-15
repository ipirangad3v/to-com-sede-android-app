package com.ipsoft.tocomsede.base.extension

import com.ipsoft.tocomsede.core.extensions.toCurrency
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StringTest {

    @Test
    fun testToCurrencyWithEmptyString() {
        val result = "".toCurrency()
        assertEquals("", result)
    }

    @Test
    fun testToCurrencyWithNonNumericString() {
        val result = "abc".toCurrency()
        assertEquals("", result)
    }

    @Test
    fun testToCurrencyWithIntegerString() {
        val result = "100".toCurrency()
        assertEquals("R$ 100,00", result)
    }

    @Test
    fun testToCurrencyWithDecimalString() {
        val result = "100.50".toCurrency("$")
        assertEquals("$ 100,50", result)
    }

    @Test
    fun testToCurrencyWithLargeDecimalString() {
        val result = "100.1234567".toCurrency("R$")
        assertEquals("R$ 100,12", result)
    }

    @Test
    fun testToCurrencyWithInvalidCurrencySymbol() {
        val result = "100".toCurrency("INVALID")
        assertEquals("INVALID 100,00", result)
    }
}

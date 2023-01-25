package com.renault.parkassist.utility

import androidx.test.filters.SmallTest
import org.junit.Assert.assertEquals
import org.junit.Test

@SmallTest
class ConversionTest {
    @Test
    fun `rangeToPercentage should convert 20 to 100 when min is 5 and max is 20`() {
        assertEquals("", 100, Conversion.rangeToPercentage(20, 5, 20))
    }

    @Test
    fun `rangeToPercentage should convert 5 to 0 when min is 5 and max is 20`() {
        assertEquals("", 0, Conversion.rangeToPercentage(5, 5, 20))
    }

    @Test
    fun `rangeToPercentage should convert 10 to 33 when min is 5 and max is 20`() {
        assertEquals("", 33, Conversion.rangeToPercentage(10, 5, 20))
    }

    @Test
    fun `percentageToRange should convert 100 to 20 when min is 5 and max is 20`() {
        assertEquals("", 20, Conversion.percentageToRange(100, 5, 20))
    }

    @Test
    fun `percentageToRange should convert 0 to 5 when min is 5 and max is 20`() { // ktlint-disable max-line-length
        assertEquals("", 5, Conversion.percentageToRange(0, 5, 20))
    }

    @Test
    fun `percentageToRange should convert 67 to 15 when min is 5 and max is 20`() {
        assertEquals("", 15, Conversion.percentageToRange(67, 5, 20))
    }

    @Test
    fun `rangeToPercentage should convert 1077 to 8 when min is 1000 and max is 2000`() {
        assertEquals("", 8, Conversion.rangeToPercentage(1077, 1000, 2000))
    }

    @Test
    fun `percentageToRange should convert 7 to 1070 when min is 1000 and max is 2000`() {
        assertEquals("", 1070, Conversion.percentageToRange(7, 1000, 2000))
    }

    @Test
    fun `percentageToRange should convert 7 to 11 when min is 10 and max is 20`() {
        assertEquals("", 11, Conversion.percentageToRange(7, 10, 20))
    }

    @Test
    fun `rangeToPercentage should convert 11 to 10 when min is 10 and max is 20`() {
        assertEquals("", 10, Conversion.rangeToPercentage(11, 10, 20))
    }

    @Test
    fun `percentageToRangeToPercentage should convert 7 to 10 when min is 10 and max is 20`() {
        assertEquals("", 10, Conversion.percentageToRangeToPercentage(7, 10, 20))
    }
}
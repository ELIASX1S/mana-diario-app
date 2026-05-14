package com.elias.manadiario

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class StreakMilestoneCalculatorTest {

    @Test
    fun `negative streak is clamped to zero`() {
        val result = StreakMilestoneCalculator.calculate(-5)

        assertEquals(3, result.targetDays)
        assertEquals(3, result.daysRemaining)
        assertEquals(0, result.progressPercent)
        assertFalse(result.completed)
    }

    @Test
    fun `streak under three targets first milestone`() {
        val result = StreakMilestoneCalculator.calculate(2)

        assertEquals(3, result.targetDays)
        assertEquals(1, result.daysRemaining)
        assertEquals(66, result.progressPercent)
        assertFalse(result.completed)
    }

    @Test
    fun `streak at three targets seven milestone`() {
        val result = StreakMilestoneCalculator.calculate(3)

        assertEquals(7, result.targetDays)
        assertEquals(4, result.daysRemaining)
        assertEquals(42, result.progressPercent)
        assertFalse(result.completed)
    }

    @Test
    fun `streak at fourteen targets thirty milestone`() {
        val result = StreakMilestoneCalculator.calculate(14)

        assertEquals(30, result.targetDays)
        assertEquals(16, result.daysRemaining)
        assertEquals(46, result.progressPercent)
        assertFalse(result.completed)
    }

    @Test
    fun `streak above thirty targets next 30 day block`() {
        val result = StreakMilestoneCalculator.calculate(31)

        assertEquals(60, result.targetDays)
        assertEquals(29, result.daysRemaining)
        assertEquals(51, result.progressPercent)
        assertFalse(result.completed)
    }
}

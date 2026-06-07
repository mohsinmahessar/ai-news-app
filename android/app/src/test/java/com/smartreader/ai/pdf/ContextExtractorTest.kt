package com.smartreader.ai.pdf

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the tap-to-context engine — the brain behind the main feature.
 * No Android dependencies, so these run fast on the JVM (`./gradlew test`).
 */
class ContextExtractorTest {

    private val paragraph =
        "The committee met early. He was meticulous in preparing the report. " +
        "Everyone praised his careful work."

    @Test
    fun `extracts tapped word`() {
        val offset = paragraph.indexOf("meticulous")
        val ctx = ContextExtractor.extractAt(paragraph, offset)!!
        assertEquals("meticulous", ctx.selectedWord)
    }

    @Test
    fun `captures the sentence containing the word`() {
        val offset = paragraph.indexOf("meticulous")
        val ctx = ContextExtractor.extractAt(paragraph, offset)!!
        assertTrue(ctx.currentSentence.contains("meticulous"))
        assertTrue(ctx.currentSentence.startsWith("He was meticulous"))
    }

    @Test
    fun `captures previous and next sentences`() {
        val offset = paragraph.indexOf("meticulous")
        val ctx = ContextExtractor.extractAt(paragraph, offset)!!
        assertTrue(ctx.previousSentence.contains("committee"))
        assertTrue(ctx.nextSentence.contains("praised"))
    }

    @Test
    fun `blank text yields null`() {
        assertEquals(null, ContextExtractor.extractAt("   ", 0))
    }
}

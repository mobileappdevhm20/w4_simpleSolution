package edu.hm.cs.w4

import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TimerTest {
    @Test
    fun createTimer_correct() {
        val t = Timer (987, 654, "timer321")
        assertEquals(654, t.seconds)
    }
}

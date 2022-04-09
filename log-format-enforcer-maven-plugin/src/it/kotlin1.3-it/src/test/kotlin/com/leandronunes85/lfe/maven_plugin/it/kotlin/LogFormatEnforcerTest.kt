package com.leandronunes85.lfe.maven_plugin.it.kotlin

import com.leandronunes85.lfe.maven_plugin.it.kotlin.LogFormatEnforcer
import com.leandronunes85.lfe.maven_plugin.it.kotlin.LogFormatEnforcerExtensions.traceEach
import com.leandronunes85.lfe.maven_plugin.it.kotlin.LogFormatEnforcerExtensions.debug

import org.junit.Test
import java.lang.Thread


class LogFormatEnforcerTest {

    private val victim = LogFormatEnforcer.loggerFor<LogFormatEnforcerTest>()


    @Test
    fun `basic syntax test`() {
        victim.info {
            mandatory1("").mandatory2(10)
                .optional1(emptyArray<Any>()).optional2("value")
                .and("other", "").exception(RuntimeException())
        }
    }

    @Test
    fun `extension function syntax`() {
        emptyArray<Int>()
                .traceEach(victim) { number: Int -> mandatory1(number).mandatory2("number is a single element") }
                .debug(victim) { wholeArray: Array<Int> -> mandatory1("").mandatory2("").and("wholeArray", wholeArray) }
    }

    @Test
    fun `log values are not computed if the log is not performed`() {
        fun costlyOperation(): String {
            throw RuntimeException("This method should not have been called!")
        }

        victim.trace {
            mandatory1("").mandatory2(costlyOperation())
        }
    }
}
package com.leandronunes85.lfe.maven_plugin.it.java;

import com.leandronunes85.lfe.maven_plugin.it.java.LogFormatEnforcer;
import org.junit.Test;

import java.util.ArrayList;


public class LogFormatEnforcerTest {

    private final LogFormatEnforcer victim = LogFormatEnforcer.loggerFor(LogFormatEnforcerTest.class);

    @Test
    public void syntaxTest() {
        victim.info(messageBuilder -> messageBuilder.mandatory1("valueMandatory1").mandatory2(100)
                .optional1(new ArrayList()).optional2("valueOptional2")
                .and("other", "otherValue"));
    }
}
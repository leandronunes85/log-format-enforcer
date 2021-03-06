package com.leandronunes85.lfe.maven_plugin.it.java;

import com.leandronunes85.lfe.maven_plugin.it.java.LogFormatEnforcer;
import org.junit.Test;

import java.util.ArrayList;


public class LogFormatEnforcerTest {

    private final LogFormatEnforcer victim = LogFormatEnforcer.loggerFor(LogFormatEnforcerTest.class);

    @Test
    public void basicSyntaxTest() {
        victim.info(messageBuilder -> messageBuilder.mandatory1("valueMandatory1").mandatory2(100)
                .optional1(new ArrayList()).optional2("value")
                .and("other", "otherValue").exception(new RuntimeException()));
    }

    private String costlyOperation() {
        throw new RuntimeException("This method should not have been called!");
    }

    public void logValuesAreNotComputedIfTheLogIsNotPerformed() {
        victim.trace(messageBuilder -> messageBuilder
                .mandatory1("valueMandatory1").mandatory2(costlyOperation())
        );
    }
}
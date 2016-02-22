package com.leandronunes85.lfe;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithoutAnyFieldsTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.package.name";
    private static final List<FieldInfo> EXPECTED_FIELDS = asList();
    private static final String EXPECTED_ENTRY_SEPARATOR = "entry_separator";
    private static final String EXPECTED_VALUE_DELIMITER_PREFIX = "value_delimeter_prefix";
    private static final String EXPECTED_VALUE_DELIMITER_SUFFIX = "value_delimeter_suffix";
    private static final String EXPECTED_KEY_VALUE_SEPARATOR = "key_value_separator";

    private String result;

    @Before
    public void setUp() {
        LogFormatEnforcerCreator victim = new LogFormatEnforcerCreator();
        this.result = victim.createALogFormatEnforcer(
                EXPECTED_PACKAGE_NAME,
                EXPECTED_FIELDS,
                EXPECTED_ENTRY_SEPARATOR,
                EXPECTED_VALUE_DELIMITER_PREFIX,
                EXPECTED_VALUE_DELIMITER_SUFFIX,
                EXPECTED_KEY_VALUE_SEPARATOR
        );
    }

    @Test
    public void createsActualBuilderThatImplementsOnlyTheLastInterface() {
        assertThat(result).contains("private class ActualBuilder implements OtherFields ");
    }

    @Test
    public void buildItInterfaceReceivesAndReturnsLastInterface () {
        assertThat(result).contains("OtherFields buildIt(OtherFields builder);");
    }
}
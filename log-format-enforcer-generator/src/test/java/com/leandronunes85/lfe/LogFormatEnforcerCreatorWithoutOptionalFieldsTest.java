package com.leandronunes85.lfe;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithoutOptionalFieldsTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.package.name";
    private static final List<FieldInfo> EXPECTED_MANDATORY_FIELDS = asList(
            FieldInfo.of("mandatoryField1", "mandatoryField1Text"),
            FieldInfo.of("mandatoryField2", "mandatoryField2Text"),
            FieldInfo.of("mandatoryField3", "mandatoryField3Text")
    );
    private static final List<FieldInfo> EXPECTED_OPTIONAL_FIELDS = asList();
    private static final String EXPECTED_ENTRY_SEPARATOR = "entry_separator";
    private static final String EXPECTED_VALUE_DELIMITER = "value_delimeter";
    private static final String EXPECTED_KEY_VALUE_SEPARATOR = "key_value_separator";

    private String result;

    @Before
    public void setUp() {
        LogFormatEnforcerCreator victim = new LogFormatEnforcerCreator();
        this.result = victim.createALogFormatEnforcer(
                EXPECTED_PACKAGE_NAME,
                EXPECTED_MANDATORY_FIELDS,
                EXPECTED_OPTIONAL_FIELDS,
                EXPECTED_ENTRY_SEPARATOR,
                EXPECTED_VALUE_DELIMITER,
                EXPECTED_KEY_VALUE_SEPARATOR
        );
    }

    @Test
    public void createsClassFromTemplate() {
        assertThat(result).isNotNull();
    }
}
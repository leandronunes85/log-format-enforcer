package com.leandronunes85.lfe;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithoutMandatoryFieldsTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.package.name";
    private static final List<FieldInfo> EXPECTED_MANDATORY_FIELDS = asList();
    private static final List<FieldInfo> EXPECTED_OPTIONAL_FIELDS = asList(
            FieldInfo.of("optionalField1", "optionalField1Text"),
            FieldInfo.of("optionalField2", "optionalField2Text"),
            FieldInfo.of("optionalField3", "optionalField3Text")
    );
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

    @Test
    public void createsActualBuilderCreatesToBuildInterfaceWithCorrectEntryPoint() {
        assertThat(result).contains("OptionalFields buildIt(OptionalFields builder);");
    }

}
package com.leandronunes85.lfe;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithIncorrectPropertiesTest {

    private static final String GOOD_PACKAGE_NAME = "expected.package.name";
    private static final List<FieldInfo> GOOD_MANDATORY_FIELDS = asList(
            FieldInfo.of("mandatoryField1", "mandatoryField1Text"),
            FieldInfo.of("mandatoryField2", "mandatoryField2Text"),
            FieldInfo.of("mandatoryField3", "mandatoryField3Text")
    );
    private static final List<FieldInfo> GOOD_OPTIONAL_FIELDS = asList(
            FieldInfo.of("optionalField1", "optionalField1Text"),
            FieldInfo.of("optionalField2", "optionalField2Text"),
            FieldInfo.of("optionalField3", "optionalField3Text")
    );
    private static final String GOOD_ENTRY_SEPARATOR = "entry_separator";
    private static final String GOOD_VALUE_DELIMITER = "value_delimeter";
    private static final String GOOD_KEY_VALUE_SEPARATOR = "key_value_separator";

    private LogFormatEnforcerCreator victim;

    @Before
    public void setUp() {
        victim = new LogFormatEnforcerCreator();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksPackageNameIsNotNull() {
        victim.createALogFormatEnforcer(
                null,
                GOOD_MANDATORY_FIELDS,
                GOOD_OPTIONAL_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksPackageNameIsNotEmpty() {
        victim.createALogFormatEnforcer(
                "",
                GOOD_MANDATORY_FIELDS,
                GOOD_OPTIONAL_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksPackageNameDoesNotHaveSpaces() {
        victim.createALogFormatEnforcer(
                "a.in val id.package",
                GOOD_MANDATORY_FIELDS,
                GOOD_OPTIONAL_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksPackageNameDoesNotHaveOtherInvalidCharacters() {
        victim.createALogFormatEnforcer(
                "um.package.inválido",
                GOOD_MANDATORY_FIELDS,
                GOOD_OPTIONAL_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksMandatoryFieldsAreNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                null,
                GOOD_OPTIONAL_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksOptionalFieldsAreNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                GOOD_MANDATORY_FIELDS,
                null,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksEntrySeparatorIsNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                GOOD_MANDATORY_FIELDS,
                GOOD_OPTIONAL_FIELDS,
                null,
                GOOD_VALUE_DELIMITER,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksValueDelimiterIsNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                GOOD_MANDATORY_FIELDS,
                GOOD_OPTIONAL_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                null,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksKeyValueSeparatorIsNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                GOOD_MANDATORY_FIELDS,
                GOOD_OPTIONAL_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER,
                null
        );
    }
}
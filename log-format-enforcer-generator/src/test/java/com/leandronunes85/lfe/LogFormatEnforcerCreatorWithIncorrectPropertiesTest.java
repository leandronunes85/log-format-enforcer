package com.leandronunes85.lfe;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithIncorrectPropertiesTest {

    private static final String GOOD_PACKAGE_NAME = "expected.package.name";
    private static final List<FieldInfo> GOOD_FIELDS = asList(
            FieldInfo.mandatory("mandatoryField1", "mandatoryField1Text"),
            FieldInfo.mandatory("mandatoryField2", "mandatoryField2Text"),
            FieldInfo.mandatory("mandatoryField3", "mandatoryField3Text"),

            FieldInfo.optional("optionalField1", "optionalField1Text"),
            FieldInfo.optional("optionalField2", "optionalField2Text"),
            FieldInfo.optional("optionalField3", "optionalField3Text")
    );
    private static final String GOOD_ENTRY_SEPARATOR = "entry_separator";
    private static final String GOOD_VALUE_DELIMITER_PREFIX = "value_delimeter_prefix";
    private static final String GOOD_VALUE_DELIMITER_SUFFIX = "value_delimeter_suffix";
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
                GOOD_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER_PREFIX,
                GOOD_VALUE_DELIMITER_SUFFIX,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksPackageNameIsNotEmpty() {
        victim.createALogFormatEnforcer(
                "",
                GOOD_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER_PREFIX,
                GOOD_VALUE_DELIMITER_SUFFIX,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksPackageNameDoesNotHaveSpaces() {
        victim.createALogFormatEnforcer(
                "a.in val id.package",
                GOOD_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER_PREFIX,
                GOOD_VALUE_DELIMITER_SUFFIX,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksPackageNameDoesNotHaveOtherInvalidCharacters() {
        victim.createALogFormatEnforcer(
                "um.package.inválido",
                GOOD_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER_PREFIX,
                GOOD_VALUE_DELIMITER_SUFFIX,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksFieldsAreNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                null,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER_PREFIX,
                GOOD_VALUE_DELIMITER_SUFFIX,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksEntrySeparatorIsNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                GOOD_FIELDS,
                null,
                GOOD_VALUE_DELIMITER_PREFIX,
                GOOD_VALUE_DELIMITER_SUFFIX,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksValueDelimiterPrefixIsNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                GOOD_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                null,
                GOOD_VALUE_DELIMITER_SUFFIX,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksValueDelimiterSuffixIsNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                GOOD_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER_PREFIX,
                null,
                GOOD_KEY_VALUE_SEPARATOR
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void checksKeyValueSeparatorIsNotNull() {
        victim.createALogFormatEnforcer(
                GOOD_PACKAGE_NAME,
                GOOD_FIELDS,
                GOOD_ENTRY_SEPARATOR,
                GOOD_VALUE_DELIMITER_PREFIX,
                GOOD_VALUE_DELIMITER_SUFFIX,
                null
        );
    }
}
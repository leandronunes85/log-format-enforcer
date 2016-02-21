package com.leandronunes85.lfe;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithCorrectPropertiesTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.package.name";
    private static final List<FieldInfo> EXPECTED_MANDATORY_FIELDS = asList(
            FieldInfo.of("mandatoryField1", "mandatoryField1Text"),
            FieldInfo.of("mandatoryField2", "mandatoryField2Text"),
            FieldInfo.of("mandatoryField3", "mandatoryField3Text")
    );
    private static final List<FieldInfo> EXPECTED_OPTIONAL_FIELDS = asList(
            FieldInfo.of("optionalField1", "optionalField1Text"),
            FieldInfo.of("optionalField2", "optionalField2Text"),
            FieldInfo.of("optionalField3", "optionalField3Text")
    );
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
                EXPECTED_MANDATORY_FIELDS,
                EXPECTED_OPTIONAL_FIELDS,
                EXPECTED_ENTRY_SEPARATOR,
                EXPECTED_VALUE_DELIMITER_PREFIX,
                EXPECTED_VALUE_DELIMITER_SUFFIX,
                EXPECTED_KEY_VALUE_SEPARATOR
        );
    }

    @Test
    public void createsClassInTheCorrectPackage() {
        assertThat(result).startsWith(format("package %s;", EXPECTED_PACKAGE_NAME));
    }

    @Test
    public void createsClassWithInterfaceForFirstMandatoryField() {
        assertThat(result).contains("interface MandatoryField1 ");
        assertThat(result).contains("MandatoryField2 mandatoryField1(Object mandatoryField1)");
    }

    @Test
    public void createsClassWithInterfaceForSecondMandatoryField() {
        assertThat(result).contains("interface MandatoryField2 ");
        assertThat(result).contains("MandatoryField3 mandatoryField2(Object mandatoryField2)");
    }

    @Test
    public void createsClassWithInterfaceForLastMandatoryField() {
        assertThat(result).contains("interface MandatoryField3 ");
        assertThat(result).contains("OptionalFields mandatoryField3(Object mandatoryField3)");
    }

    @Test
    public void createsClassWithOptionalFieldsInterface() {
        assertThat(result).contains("interface OptionalFields ");

        assertThat(result).contains("OptionalFields optionalField1(Object optionalField1);");
        assertThat(result).contains("OptionalFields optionalField2(Object optionalField2);");
        assertThat(result).contains("OptionalFields optionalField3(Object optionalField3);");
    }

    @Test
    public void createsActualBuilderClassThatImplementsAllInterfaces() {
        assertThat(result).contains("private class ActualBuilder implements MandatoryField1, MandatoryField2, MandatoryField3, OptionalFields");
    }

    @Test
    public void createsActualBuilderClassThatContainsSetterForAllMandatoryFields() {
        assertThat(result).contains("public MandatoryField2 mandatoryField1(Object mandatoryField1)");
        assertThat(result).contains("return newField(\"mandatoryField1Text\", mandatoryField1);");

        assertThat(result).contains("public MandatoryField3 mandatoryField2(Object mandatoryField2)");
        assertThat(result).contains("return newField(\"mandatoryField2Text\", mandatoryField2);");

        assertThat(result).contains("public OptionalFields mandatoryField3(Object mandatoryField3)");
        assertThat(result).contains("return newField(\"mandatoryField3Text\", mandatoryField3);");
    }

    @Test
    public void createsActualBuilderClassThatContainsSetterForAllOptionalFields() {
        assertThat(result).contains("public OptionalFields optionalField1(Object optionalField1)");
        assertThat(result).contains("return newField(\"optionalField1Text\", optionalField1);");

        assertThat(result).contains("public OptionalFields optionalField2(Object optionalField2)");
        assertThat(result).contains("return newField(\"optionalField2Text\", optionalField2);");

        assertThat(result).contains("public OptionalFields optionalField3(Object optionalField3)");
        assertThat(result).contains("return newField(\"optionalField3Text\", optionalField3);");
    }

    @Test
    public void createsActualBuilderUsesConfiguredSeparator() {
        assertThat(result).contains(format("joining(\"%s\")", EXPECTED_ENTRY_SEPARATOR));
    }

    @Test
    public void createsActualBuilderUsesConfiguredValueDelimiter() {
        assertThat(result).contains(format("%s%s{}%s\"",
                EXPECTED_KEY_VALUE_SEPARATOR, EXPECTED_VALUE_DELIMITER_PREFIX, EXPECTED_VALUE_DELIMITER_SUFFIX));
    }

    @Test
    public void createsActualBuilderCreatesToBuildInterfaceWithCorrectEntryPoint() {
        assertThat(result).contains("OptionalFields buildIt(MandatoryField1 builder);");
    }
}
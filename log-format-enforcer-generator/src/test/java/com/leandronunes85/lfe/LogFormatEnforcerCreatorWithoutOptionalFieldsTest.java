package com.leandronunes85.lfe;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithoutOptionalFieldsTest extends AbstractTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.name";
    private static final List<FieldInfo> EXPECTED_FIELDS = asList(
            FieldInfo.mandatory("mandatoryField1", "mandatoryField1Text"),
            FieldInfo.mandatory("mandatoryField2", "mandatoryField2Text"),
            FieldInfo.mandatory("mandatoryField3", "mandatoryField3Text")
    );
    private static final String EXPECTED_ENTRY_SEPARATOR = "entry_separator";
    private static final String EXPECTED_VALUE_DELIMITER_PREFIX = "value_delimeter_prefix";
    private static final String EXPECTED_VALUE_DELIMITER_SUFFIX = "value_delimeter_suffix";
    private static final String EXPECTED_KEY_VALUE_SEPARATOR = "key_value_separator";

    @Before
    public void setUp() throws Exception {
        createFile(
                EXPECTED_PACKAGE_NAME,
                EXPECTED_FIELDS,
                EXPECTED_ENTRY_SEPARATOR,
                EXPECTED_VALUE_DELIMITER_PREFIX,
                EXPECTED_VALUE_DELIMITER_SUFFIX,
                EXPECTED_KEY_VALUE_SEPARATOR
        );
    }

    @Test
    public void createsInterfaceForLastMandatoryField() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("MandatoryField3");
        MethodDeclaration method = getMethodByName(classOrInterface, "mandatoryField3");

        assertThat(classOrInterface.getExtends()).isNullOrEmpty();
        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("MoreFields mandatoryField3(Object)");
    }
}
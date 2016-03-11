package com.leandronunes85.lfe;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithoutMandatoryFieldsTest extends AbstractTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.name";
    private static final List<FieldInfo> EXPECTED_FIELDS = asList(
            FieldInfo.optional("optionalField1", "optionalField1Text"),
            FieldInfo.optional("optionalField2", "optionalField2Text"),
            FieldInfo.optional("optionalField3", "optionalField3Text")
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
    public void toBuildInterfaceHasCorrectEntryPoint() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ToBuild");
        MethodDeclaration methodDeclaration = getMethodByName(classOrInterface, "buildIt");

        assertThat(methodDeclaration.getDeclarationAsString(true, true, false))
                .isEqualTo("NoMoreFields buildIt(OptionalField1)");
    }

}
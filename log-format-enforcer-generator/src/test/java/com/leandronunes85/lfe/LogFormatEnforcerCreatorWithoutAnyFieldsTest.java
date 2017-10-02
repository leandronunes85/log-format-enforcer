package com.leandronunes85.lfe;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithoutAnyFieldsTest extends AbstractTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.name";
    private static final List<FieldInfo> EXPECTED_FIELDS = asList();
    private static final String EXPECTED_ENTRY_SEPARATOR = "entry_separator";
    private static final String EXPECTED_VALUE_DELIMITER_PREFIX = "value_delimeter_prefix";
    private static final String EXPECTED_VALUE_DELIMITER_SUFFIX = "value_delimeter_suffix";
    private static final String EXPECTED_KEY_VALUE_SEPARATOR = "key_value_separator";

    @Before
    public void setUp() throws Exception {
        createFile(EXPECTED_PACKAGE_NAME,
                EXPECTED_FIELDS,
                EXPECTED_ENTRY_SEPARATOR,
                EXPECTED_VALUE_DELIMITER_PREFIX,
                EXPECTED_VALUE_DELIMITER_SUFFIX,
                EXPECTED_KEY_VALUE_SEPARATOR
        );
    }

    @Test
    public void createsActualBuilderThatImplementsOnlyTheLastInterface() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        Stream<String> implementationNames = classOrInterface.getImplements().stream().map(ClassOrInterfaceType::getName);

        assertThat(implementationNames).containsExactly("MoreFields", "NoMoreFields");
    }

    @Test
    public void buildItInterfaceReceivesAndReturnsLastInterface () {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ToBuild");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "buildIt");

        assertThat(methodDeclaration.getDeclarationAsString(true, true, false))
                .isEqualTo("NoMoreFields buildIt(MoreFields)");
    }
}
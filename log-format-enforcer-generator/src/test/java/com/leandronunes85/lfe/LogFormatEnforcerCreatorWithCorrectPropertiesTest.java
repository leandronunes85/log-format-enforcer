package com.leandronunes85.lfe;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerCreatorWithCorrectPropertiesTest extends AbstractTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.name";
    private static final List<FieldInfo> EXPECTED_FIELDS = asList(
            FieldInfo.mandatory("mandatoryField1", "mandatoryField1Text"),
            FieldInfo.mandatory("mandatoryField2", "mandatoryField2Text"),
            FieldInfo.mandatory("mandatoryField3", "mandatoryField3Text"),

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
        createFile(EXPECTED_PACKAGE_NAME,
                EXPECTED_FIELDS,
                EXPECTED_ENTRY_SEPARATOR,
                EXPECTED_VALUE_DELIMITER_PREFIX,
                EXPECTED_VALUE_DELIMITER_SUFFIX,
                EXPECTED_KEY_VALUE_SEPARATOR
        );
    }

    @Test
    public void createsInterfaceForFirstMandatoryField() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("MandatoryField1");
        MethodDeclaration method = getFirstMethodByName(classOrInterface, "mandatoryField1");

        assertThat(classOrInterface.getExtends()).isNullOrEmpty();
        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("MandatoryField2 mandatoryField1(Object)");
    }

    @Test
    public void createsInterfaceForSecondMandatoryField() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("MandatoryField2");
        MethodDeclaration method = getFirstMethodByName(classOrInterface, "mandatoryField2");

        assertThat(classOrInterface.getExtends()).isNullOrEmpty();
        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("MandatoryField3 mandatoryField2(Object)");
    }

    @Test
    public void createsInterfaceForLastMandatoryField() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("MandatoryField3");
        MethodDeclaration method = getFirstMethodByName(classOrInterface, "mandatoryField3");

        assertThat(classOrInterface.getExtends()).isNullOrEmpty();
        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("OptionalField1 mandatoryField3(Object)");
    }

    @Test
    public void createsInterfaceForFirstOptionalField() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("OptionalField1");
        MethodDeclaration method = getFirstMethodByName(classOrInterface, "optionalField1");

        assertThat(classOrInterface.getExtends()).hasSize(1);
        assertThat(classOrInterface.getExtends().get(0).getName()).isEqualTo("OptionalField2");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("OptionalField2 optionalField1(Object)");
    }

    @Test
    public void createsInterfaceForSecondOptionalField() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("OptionalField2");
        MethodDeclaration method = getFirstMethodByName(classOrInterface, "optionalField2");

        assertThat(classOrInterface.getExtends()).hasSize(1);
        assertThat(classOrInterface.getExtends().get(0).getName()).isEqualTo("OptionalField3");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("OptionalField3 optionalField2(Object)");
    }

    @Test
    public void createsInterfaceForLastOptionalField() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("OptionalField3");
        MethodDeclaration method = getFirstMethodByName(classOrInterface, "optionalField3");

        assertThat(classOrInterface.getExtends()).hasSize(1);
        assertThat(classOrInterface.getExtends().get(0).getName()).isEqualTo("MoreFields");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("MoreFields optionalField3(Object)");
    }

    @Test
    public void createsMoreFieldsInterface() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("MoreFields");
        MethodDeclaration andMethod = getFirstMethodByName(classOrInterface, "and");
        MethodDeclaration exceptionMethod = getFirstMethodByName(classOrInterface, "exception");

        assertThat(andMethod.getDeclarationAsString(true, true, false))
                .isEqualTo("MoreFields and(String, Object)");
        assertThat(exceptionMethod.getDeclarationAsString(true, true, false))
                .isEqualTo("NoMoreFields exception(Throwable)");
    }

    @Test
    public void createsActualBuilderClassThatImplementsAllInterfaces() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        Stream<String> implementationNames = classOrInterface.getImplements().stream().map(ClassOrInterfaceType::getName);

        assertThat(implementationNames).containsExactly("MandatoryField1", "MandatoryField2", "MandatoryField3",
                "OptionalField1", "OptionalField2", "OptionalField3", "MoreFields", "NoMoreFields");
    }

    @Test
    public void actualBuilderClassContainsSetterForMandatoryField1() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "mandatoryField1");

        assertThat(methodDeclaration.getDeclarationAsString())
                .isEqualTo("public MandatoryField2 mandatoryField1(Object mandatoryField1)");
        assertThat(methodDeclaration.getBody().getStmts().get(0).toString())
                .isEqualTo("return newField(\"mandatoryField1Text\", mandatoryField1);");
    }

    @Test
    public void actualBuilderClassContainsSetterForMandatoryField2() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "mandatoryField2");

        assertThat(methodDeclaration.getDeclarationAsString())
                .isEqualTo("public MandatoryField3 mandatoryField2(Object mandatoryField2)");
        assertThat(methodDeclaration.getBody().getStmts().get(0).toString())
                .isEqualTo("return newField(\"mandatoryField2Text\", mandatoryField2);");

    }

    @Test
    public void actualBuilderClassContainsSetterForMandatoryField3() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "mandatoryField3");

        assertThat(methodDeclaration.getDeclarationAsString())
                .isEqualTo("public OptionalField1 mandatoryField3(Object mandatoryField3)");
        assertThat(methodDeclaration.getBody().getStmts().get(0).toString())
                .isEqualTo("return newField(\"mandatoryField3Text\", mandatoryField3);");

    }

    @Test
    public void actualBuilderClassContainsSetterForOptionalField1() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "optionalField1");

        assertThat(methodDeclaration.getDeclarationAsString())
                .isEqualTo("public OptionalField2 optionalField1(Object optionalField1)");
        assertThat(methodDeclaration.getBody().getStmts().get(0).toString())
                .isEqualTo("return newField(\"optionalField1Text\", optionalField1);");

    }

    @Test
    public void actualBuilderClassContainsSetterForOptionalField2() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "optionalField2");

        assertThat(methodDeclaration.getDeclarationAsString())
                .isEqualTo("public OptionalField3 optionalField2(Object optionalField2)");
        assertThat(methodDeclaration.getBody().getStmts().get(0).toString())
                .isEqualTo("return newField(\"optionalField2Text\", optionalField2);");
    }

    @Test
    public void actualBuilderClassContainsSetterForOptionalField3() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "optionalField3");

        assertThat(methodDeclaration.getDeclarationAsString())
                .isEqualTo("public MoreFields optionalField3(Object optionalField3)");
        assertThat(methodDeclaration.getBody().getStmts().get(0).toString())
                .isEqualTo("return newField(\"optionalField3Text\", optionalField3);");

    }

    @Test
    public void actualBuilderClassUsesConfiguredSeparator() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "getMessage");

        assertThat(methodDeclaration.getBody().getStmts().get(0).toString())
                .contains(format("joining(\"%s\")", EXPECTED_ENTRY_SEPARATOR));
    }

    @Test
    public void actualBuilderClassUsesConfiguredValueSeparatorAndDelimiters() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ActualBuilder");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "newField");

        assertThat(methodDeclaration.getBody().getStmts().get(0).toString())
                .contains(format("%s%s{}%s\"",
                        EXPECTED_KEY_VALUE_SEPARATOR, EXPECTED_VALUE_DELIMITER_PREFIX, EXPECTED_VALUE_DELIMITER_SUFFIX));
    }

    @Test
    public void toBuildInterfaceContainsCorrectEntryPoint() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("ToBuild");
        MethodDeclaration methodDeclaration = getFirstMethodByName(classOrInterface, "buildIt");

        assertThat(methodDeclaration.getDeclarationAsString(true, true, false))
                .isEqualTo("NoMoreFields buildIt(MandatoryField1)");
    }
}
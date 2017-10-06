package com.leandronunes85.lfe;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerStaticContentTest extends AbstractTest {

    private static final String EXPECTED_PACKAGE_NAME = "expected.name";

    @Before
    public void setUp() throws Exception {
        createFile(EXPECTED_PACKAGE_NAME,
                singletonList(FieldInfo.mandatory("mandatoryField1", "mandatoryField1Text")),
                "entrySeparator",
                "valueDelimiterPrefix",
                "valueDelimiterSuffix",
                "keyValueSeparator"
        );
    }

    @Test
    public void createsClassInTheCorrectPackage() {
        assertThat(compilationUnit.getPackage().getName().toString()).isEqualTo(EXPECTED_PACKAGE_NAME);
    }

    @Test
    public void createsMethodForLoggingErrors() {
        MethodDeclaration method = getMethodByName("error");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void error(MessageBuilder)");
    }

    @Test
    public void createsMethodForLoggingWarnings() {
        MethodDeclaration method = getMethodByName("warn");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void warn(MessageBuilder)");
    }

    @Test
    public void createsMethodForLoggingInformations() {
        MethodDeclaration method = getMethodByName("info");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void info(MessageBuilder)");
    }

    @Test
    public void createsMethodForLoggingDebugs() {
        MethodDeclaration method = getMethodByName("debug");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void debug(MessageBuilder)");
    }

    @Test
    public void createsMethodForLoggingTraces() {
        MethodDeclaration method = getMethodByName("trace");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void trace(MessageBuilder)");
    }

    @Test
    public void mandatoryField1InterfaceContainsSetterForSupplierOfMandatoryField1() {
        ClassOrInterfaceDeclaration classOrInterface = getClassOrInterfaceByName("MandatoryField1");
        List<String> methods = getAllMethodsByName(classOrInterface, "mandatoryField1").stream()
                .map(m -> m.getDeclarationAsString(true, true, false))
                .collect(toList());

        assertThat(methods).containsOnlyOnce("MoreFields mandatoryField1(Supplier<Object>)");
    }

}

package com.leandronunes85.lfe;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFormatEnforcerStaticContentTest extends AbstractTest {
    private static final String EXPECTED_PACKAGE_NAME = "expected.name";

    @Before
    public void setUp() throws Exception {
        createFile(EXPECTED_PACKAGE_NAME,
                EMPTY_LIST,
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
                .isEqualTo("public void error(ToBuild)");
    }

    @Test
    public void createsMethodForLoggingWarnings() {
        MethodDeclaration method = getMethodByName("warn");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void warn(ToBuild)");
    }

    @Test
    public void createsMethodForLoggingInformations() {
        MethodDeclaration method = getMethodByName("info");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void info(ToBuild)");
    }

    @Test
    public void createsMethodForLoggingDebugs() {
        MethodDeclaration method = getMethodByName("debug");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void debug(ToBuild)");
    }

    @Test
    public void createsMethodForLoggingTraces() {
        MethodDeclaration method = getMethodByName("trace");

        assertThat(method.getDeclarationAsString(true, true, false))
                .isEqualTo("public void trace(ToBuild)");
    }

}

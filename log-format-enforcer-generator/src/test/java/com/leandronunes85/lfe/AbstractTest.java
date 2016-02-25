package com.leandronunes85.lfe;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractTest {

    private static final <T> Stream<T> filter(Collection<?> collection, Class<T> clazz) {
        return collection.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    private LogFormatEnforcerCreator victim = new LogFormatEnforcerCreator();

    protected CompilationUnit compilationUnit;

    protected void createFile(String withPackageName,
                              List<FieldInfo> withFields,
                              String withEntrySeparator,
                              String withValueDelimiterPrefix,
                              String withValueDelimiterSuffix,
                              String withKeyValueSeparator) throws Exception {

        String fileContents = victim.createALogFormatEnforcer(
                withPackageName,
                withFields,
                withEntrySeparator,
                withValueDelimiterPrefix,
                withValueDelimiterSuffix,
                withKeyValueSeparator
        );

        try (ByteArrayInputStream in = new ByteArrayInputStream(fileContents.getBytes())) {
            compilationUnit = JavaParser.parse(in);
        }
    }

    protected ClassOrInterfaceDeclaration getInterfaceByName(String interfaceName) {
        List<ClassOrInterfaceDeclaration> matchingInterfaces =
                filter(compilationUnit.getTypes().get(0).getMembers(), ClassOrInterfaceDeclaration.class)
                        .filter(i -> interfaceName.equals(i.getName()))
                        .collect(Collectors.toList());

        assertThat(matchingInterfaces).hasSize(1);
        return matchingInterfaces.get(0);
    }

    protected MethodDeclaration getMethodByName(ClassOrInterfaceDeclaration classOrInterface,
                                                          String methodName) {
        List<MethodDeclaration> matchingMethods =
                filter(classOrInterface.getMembers(), MethodDeclaration.class)
                        .filter(i -> methodName.equals(i.getName()))
                        .collect(Collectors.toList());

        assertThat(matchingMethods).hasSize(1);
        return matchingMethods.get(0);
    }
}

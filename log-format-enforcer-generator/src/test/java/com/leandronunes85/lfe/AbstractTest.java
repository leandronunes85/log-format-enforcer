package com.leandronunes85.lfe;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractTest {

    private final LogFormatEnforcerCreator victim = new LogFormatEnforcerCreator();

    protected CompilationUnit compilationUnit;

    protected void createFile(String withPackageName,
                              List<FieldInfo> withFields,
                              String withEntrySeparator,
                              String withValueDelimiterPrefix,
                              String withValueDelimiterSuffix,
                              String withKeyValueSeparator) throws Exception {

        String fileContents = victim.createALogFormatEnforcer(
                Language.JAVA8,
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

    protected ClassOrInterfaceDeclaration getClassOrInterfaceByName(String classOrInterfaceName) {
        List<ClassOrInterfaceDeclaration> matchingInterfaces =
                filterInstancesOfClass(compilationUnit.getTypes().get(0).getMembers(), ClassOrInterfaceDeclaration.class)
                        .filter(i -> classOrInterfaceName.equals(i.getName()))
                        .collect(Collectors.toList());

        assertThat(matchingInterfaces)
                .describedAs("Class or interface '%s' not found", classOrInterfaceName)
                .hasSize(1);
        return matchingInterfaces.get(0);
    }

    protected MethodDeclaration getMethodByName(String methodName) {
        return getFirstMethodByName(compilationUnit.getTypes().get(0), methodName);
    }

    protected MethodDeclaration getFirstMethodByName(TypeDeclaration classOrInterface,
                                                     String methodName) {
        List<MethodDeclaration> matchingMethods =
                getAllMethodsByName(classOrInterface, methodName);

        assertThat(matchingMethods.size())
                .describedAs("Method '%s' not found under '%s'", methodName, classOrInterface.getName())
                .isGreaterThan(0);
        return matchingMethods.get(0);
    }

    protected List<MethodDeclaration> getAllMethodsByName(TypeDeclaration classOrInterface,
                                                          String methodName) {
        return filterInstancesOfClass(classOrInterface.getMembers(), MethodDeclaration.class)
                .filter(i -> methodName.equals(i.getName()))
                .collect(Collectors.toList());
    }

    private static <T> Stream<T> filterInstancesOfClass(Collection<?> collection, Class<T> clazz) {
        return collection.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }
}

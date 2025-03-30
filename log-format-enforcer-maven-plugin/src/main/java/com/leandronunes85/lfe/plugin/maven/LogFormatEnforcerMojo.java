package com.leandronunes85.lfe.plugin.maven;

import com.leandronunes85.lfe.FieldInfo;
import com.leandronunes85.lfe.Language;
import com.leandronunes85.lfe.LogFormatEnforcerCreator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Mojo(name = "create", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class LogFormatEnforcerMojo extends AbstractMojo {

    private static final String FILE_NAME = "LogFormatEnforcer";

    private final LogFormatEnforcerCreator lfeCreator = new LogFormatEnforcerCreator();

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter
    private LinkedList<Field> fields = new LinkedList<>();

    @Parameter(defaultValue = "java-11")
    private String language;

    @Parameter(required = true)
    private String packageName;

    @Parameter(defaultValue = ", ")
    private String entrySeparator;

    @Parameter(defaultValue = "'")
    private String valueDelimiterPrefix;

    @Parameter(defaultValue = "'")
    private String valueDelimiterSuffix;

    @Parameter(defaultValue = "=")
    private String keyValueSeparator;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/log-format-enforcer/", required = true, readonly = true)
    private File outputDirectory;

    private Language languageEnum;

    public void execute() throws MojoExecutionException {
        this.languageEnum = Language.from(language);

        try {
            Path pathToWrite = findWhereToWrite();
            getLog().info("Writing " + pathToWrite);
            String logFormatEnforcerContents = generateClass();
            Files.write(pathToWrite, logFormatEnforcerContents.getBytes());
            getLog().info("File successfully written");
            project.addCompileSourceRoot(languageEnum.outputDir(outputDirectory));
        } catch (IOException e) {
            throw new MojoExecutionException("Error writing " + FILE_NAME, e);
        }
    }

    private String generateClass() {
        String logFormatEnforcerContents = lfeCreator.createALogFormatEnforcer(
                languageEnum,
                packageName,
                getFieldInfoList(),
                entrySeparator,
                valueDelimiterPrefix,
                valueDelimiterSuffix,
                keyValueSeparator);
        getLog().debug("logFormatEnforcerContents:");
        getLog().debug(logFormatEnforcerContents);
        return logFormatEnforcerContents;
    }

    private List<FieldInfo> getFieldInfoList() {
        return fields.stream()
                .map(it -> new FieldInfo(it.name, it.text, !it.mandatory))
                .collect(toList());
    }

    private Path findWhereToWrite() throws IOException {
        Path path = Paths.get(languageEnum.outputDir(outputDirectory), packageName.split("\\."));
        Files.createDirectories(path);
        return path.resolve(FILE_NAME + languageEnum.getFileExtension());
    }
}

package com.leandronunes85.lfe.maven_plugin;

import com.leandronunes85.lfe.FieldInfo;
import com.leandronunes85.lfe.Language;
import com.leandronunes85.lfe.LogFormatEnforcerCreator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@Mojo(name = "create", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class LogFormatEnforcerMojo extends AbstractMojo {

    private static final String FILE_NAME = "LogFormatEnforcer";

    private final LogFormatEnforcerCreator lfeCreator = new LogFormatEnforcerCreator();

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter
    private LinkedHashMap<String, String> mandatoryFields = new LinkedHashMap<>();

    @Parameter
    private LinkedHashMap<String, String> optionalFields = new LinkedHashMap<>();

    @Parameter(defaultValue = "java-8")
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
        logParameters();

        this.languageEnum = Language.from(language);

        try {
            Path pathToWrite = findWhereToWrite();
            getLog().info("Writing " + pathToWrite.toString());
            String logFormatEnforcerContents = generateClass();
            Files.write(pathToWrite, logFormatEnforcerContents.getBytes());
            project.addCompileSourceRoot(languageEnum.outputDir(outputDirectory));
            getLog().info("File successfully written");
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

        Stream<FieldInfo> mandatory = mandatoryFields.entrySet().stream()
                .map(FieldInfo::mandatory);
        Stream<FieldInfo> optional = optionalFields.entrySet().stream()
                .map(FieldInfo::optional);

        return concat(mandatory, optional).collect(toList());
    }

    private Path findWhereToWrite() throws IOException {
        Path path = Paths.get(languageEnum.outputDir(outputDirectory), packageName.split("\\."));
        Files.createDirectories(path);
        return path.resolve(FILE_NAME + languageEnum.getFileExtension());
    }

    private void logParameters() {
        Log log = getLog();

        if (log.isDebugEnabled()) {
            log.debug("mandatoryFields:");
            for (Map.Entry<String, String> field : mandatoryFields.entrySet()) {
                log.debug(format("\t\"%s\" -> \"%s\"", field.getKey(), field.getValue()));
            }
            log.debug("optionalFields:");
            for (Map.Entry<String, String> field : optionalFields.entrySet()) {
                log.debug(format("\t\"%s\" -> \"%s\"", field.getKey(), field.getValue()));
            }
            log.debug(format("language -> \"%s\"", language));
            log.debug(format("packageName -> \"%s\"", packageName));
            log.debug(format("entrySeparator -> \"%s\"", entrySeparator));
            log.debug(format("valueDelimiterPrefix -> \"%s\"", valueDelimiterPrefix));
            log.debug(format("valueDelimiterSuffix -> \"%s\"", valueDelimiterSuffix));
            log.debug(format("keyValueSeparator -> \"%s\"", keyValueSeparator));
            log.debug(format("outputDirectory -> \"%s\"", outputDirectory));
        }
    }
}

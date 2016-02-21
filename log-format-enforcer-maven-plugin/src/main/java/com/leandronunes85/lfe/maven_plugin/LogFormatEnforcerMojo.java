package com.leandronunes85.lfe.maven_plugin;

import com.leandronunes85.lfe.FieldInfo;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Mojo(name = "create", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class LogFormatEnforcerMojo extends AbstractMojo {

    private static final String FILE_NAME = "LogFormatEnforcer.java";

    private static List<FieldInfo> toFieldInfoList(Map<String, String> input) {
        return input.entrySet().stream()
                .map(e -> FieldInfo.of(e.getKey(), firstNonNull(e.getValue(), e.getKey())))
                .collect(toList());
    }

    private final LogFormatEnforcerCreator lfeCreator = new LogFormatEnforcerCreator();

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter
    private Map<String, String> mandatoryFields = new HashMap<>();

    @Parameter
    private Map<String, String> optionalFields = new HashMap<>();

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

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/log-format-enforcer-maven-plugin/", required = true)
    private File outputDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        logParameters();

        Path pathToWrite = findWhereToWrite();
        getLog().info("Writing " + pathToWrite.toString());

        pathToWrite.getParent().toFile().mkdirs();
        String logFormatEnforcerContents = lfeCreator.createALogFormatEnforcer(packageName,
                toFieldInfoList(mandatoryFields), toFieldInfoList(optionalFields),
                entrySeparator, valueDelimiterPrefix, valueDelimiterSuffix, keyValueSeparator);
        try {
            Files.write(pathToWrite, logFormatEnforcerContents.getBytes());
            project.addCompileSourceRoot(outputDirectory.getPath());
            getLog().info("File successfully written");
        } catch (IOException e) {
            throw new MojoExecutionException("Error writing LogFormatEnforcer.java", e);
        }
    }

    private Path findWhereToWrite() {
        return Paths
                .get(outputDirectory.getAbsolutePath(), packageName.split("\\."))
                .resolve(FILE_NAME);
    }

    private void logParameters() {
        Log log = getLog();

        log.debug("mandatoryFields:");
        for (Map.Entry<String, String> field : mandatoryFields.entrySet()) {
            log.debug(format("\t\"%s\" -> \"%s\"", field.getKey(), field.getValue()));
        }
        log.debug("optionalFields:");
        for (Map.Entry<String, String> field : optionalFields.entrySet()) {
            log.debug(format("\t\"%s\" -> \"%s\"", field.getKey(), field.getValue()));
        }
        log.debug(format("packageName -> \"%s\"", packageName));
        log.debug(format("entrySeparator -> \"%s\"", entrySeparator));
        log.debug(format("valueDelimiterPrefix -> \"%s\"", valueDelimiterPrefix));
        log.debug(format("valueDelimiterSuffix -> \"%s\"", valueDelimiterSuffix));
        log.debug(format("keyValueSeparator -> \"%s\"", keyValueSeparator));
        log.debug(format("outputDirectory -> \"%s\"", outputDirectory));
    }
}

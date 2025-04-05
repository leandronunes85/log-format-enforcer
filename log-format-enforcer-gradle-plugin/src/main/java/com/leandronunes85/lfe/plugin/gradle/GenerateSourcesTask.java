package com.leandronunes85.lfe.plugin.gradle;

import com.leandronunes85.lfe.FieldInfo;
import com.leandronunes85.lfe.Language;
import com.leandronunes85.lfe.LogFormatEnforcerCreator;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class GenerateSourcesTask extends DefaultTask {
    private final Logger logger = getLogger();

    public Property<String> packageName;
    public Property<Language> language;
    public ListProperty<FieldInfo> fields;
    public Property<String> entrySeparator;
    public Property<String> valueDelimiterPrefix;
    public Property<String> valueDelimiterSuffix;
    public Property<String> keyValueSeparator;

    public Provider<Directory> basePath;

    @TaskAction
    public void generateSources() throws IOException {

        String lfe = new LogFormatEnforcerCreator().createALogFormatEnforcer(
                language.get(),
                packageName.get(),
                fields.get(),
                entrySeparator.get(),
                valueDelimiterPrefix.get(),
                valueDelimiterSuffix.get(),
                keyValueSeparator.get()
        );

        File basePathFile = basePath.get().getAsFile();

        if (basePathFile.exists()) {
            try (Stream<Path> paths = Files.walk(basePathFile.toPath())) {
                paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        }

        File generatedSourcesDir = new File(
                basePathFile,
                packageName.get().replace('.', '/')
        );

        logger.debug("generated sources directory: {}", generatedSourcesDir);
        if (!generatedSourcesDir.mkdirs()) {
            throw new RuntimeException("Failed to create directory " + generatedSourcesDir.getAbsolutePath());
        }
        File file = new File(generatedSourcesDir, "LogFormatEnforcer" + language.get().getFileExtension());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(lfe);
        }
    }
}

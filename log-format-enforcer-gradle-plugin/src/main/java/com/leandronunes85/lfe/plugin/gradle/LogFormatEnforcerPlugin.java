package com.leandronunes85.lfe.plugin.gradle;

import com.leandronunes85.lfe.Language;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSetContainer;

public class LogFormatEnforcerPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Provider<Directory> basePath = project.getLayout().getBuildDirectory()
                .dir("generated/sources/logFormatEnforcer");
        var extension = project.getExtensions().create("logFormatEnforcer", LogFormatEnforcerPluginExtension.class);

        var generateSourcesTask = project.getTasks().create("generateSources", GenerateSourcesTask.class, task -> {
            task.packageName = extension.getPackageName();
            task.fields = extension.getFields();
            task.language = extension.getLanguage().convention(Language.JAVA11);
            task.entrySeparator = extension.getEntrySeparator().convention(", ");
            task.valueDelimiterPrefix = extension.getValueDelimiterPrefix().convention("'");
            task.valueDelimiterSuffix = extension.getValueDelimiterSuffix().convention("'");
            task.keyValueSeparator = extension.getKeyValueSeparator().convention(": ");
            task.basePath = basePath;
        });

        SourceSetContainer sourceSetContainer = (SourceSetContainer) project.property("sourceSets");
        sourceSetContainer.getByName("main").getJava().srcDir(basePath);

        project.getTasks()
                .stream()
                .filter(it -> it.getName().startsWith("compile"))
                .forEach(it -> it.dependsOn(generateSourcesTask));
    }
}

package com.leandronunes85.lfe;

import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum Language {
    JAVA11("java-11", "templates/Java11LogFormatEnforcer.stg", ".java", "java"),
    KOTLIN13("kotlin-1.3", "templates/Kotlin13LogFormatEnforcer.stg", ".kt", "kotlin"),
    KOTLIN15("kotlin-1.5", "templates/Kotlin15LogFormatEnforcer.stg", ".kt", "kotlin"),
    KOTLIN("kotlin", "templates/KotlinLogFormatEnforcer.stg", ".kt", "kotlin");

    public static Language from(String language) {
        return Arrays.stream(Language.values())
                .filter((l) -> l.externalName.equalsIgnoreCase(language))
                .findFirst()
                .orElseThrow(() -> {
                            String supportedValues = Arrays.stream(Language.values())
                                    .map((l) -> "'" + l.externalName + "'")
                                    .collect(Collectors.joining(", "));
                            return new IllegalArgumentException("Language '" + language + "' not supported. It must be one of " + supportedValues + ".");
                        }
                );
    }

    private final String externalName;
    private final STGroupFile template;
    private final String fileExtension;
    private final String outputDirSuffix;

    Language(String externalName, String template, String fileExtension, String outputDirSuffix) {
        this.externalName = externalName;
        this.template = new STGroupFile(template);
        this.fileExtension = fileExtension;
        this.outputDirSuffix = outputDirSuffix;
    }

    public String outputDir(File outputDir) {
        return Paths.get(outputDir.getAbsolutePath(), outputDirSuffix).toString();
    }

    public STGroupFile getTemplate() {
        return template;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}

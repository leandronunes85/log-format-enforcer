package com.leandronunes85.lfe;

import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.nio.file.Paths;

public enum Language {
    JAVA8("templates/Java8LogFormatEnforcer.stg", ".java", "java"),
    KOTLIN13("templates/Kotlin13LogFormatEnforcer.stg", ".kt", "kotlin");

    public static Language from(String language) {
        switch (language) {
            case "java-8":
                return JAVA8;
            case "kotlin-1.3":
                return KOTLIN13;
            default:
                throw new IllegalArgumentException("Language '" + language + "' not supported. It must be either 'java-8' or 'kotlin-1.3'");
        }
    }

    private final STGroupFile template;
    private final String fileExtension;
    private final String outputDirSuffix;

    Language(String template, String fileExtension, String outputDirSuffix) {
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

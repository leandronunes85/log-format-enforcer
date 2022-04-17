package com.leandronunes85.lfe;

import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.nio.file.Paths;

public enum Language {
    JAVA11("templates/Java11LogFormatEnforcer.stg", ".java", "java"),
    KOTLIN13("templates/Kotlin13LogFormatEnforcer.stg", ".kt", "kotlin"),
    KOTLIN15("templates/Kotlin15LogFormatEnforcer.stg", ".kt", "kotlin");

    public static Language from(String language) {
        switch (language) {
            case "java-11":
                return JAVA11;
            case "kotlin-1.3":
                return KOTLIN13;
            case "kotlin-1.5":
                return KOTLIN15;
            default:
                throw new IllegalArgumentException("Language '" + language + "' not supported. It must be one of 'java-11' 'kotlin-1.3' or 'kotlin-1.5'");
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

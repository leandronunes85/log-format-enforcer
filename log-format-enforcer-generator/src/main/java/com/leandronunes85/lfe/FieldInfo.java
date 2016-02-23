package com.leandronunes85.lfe;

import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

public class FieldInfo {

    public static FieldInfo mandatory(String fieldName, String fieldText) {
        return new FieldInfo(fieldName, fieldText, false);
    }

    public static FieldInfo mandatory(Map.Entry<String, String> entry) {
        return mandatory(entry.getKey(), firstNonNull(entry.getValue(), entry.getKey()));
    }

    public static FieldInfo optional(String fieldName, String fieldText) {
        return new FieldInfo(fieldName, fieldText, true);
    }

    public static FieldInfo optional(Map.Entry<String, String> entry) {
        return optional(entry.getKey(), firstNonNull(entry.getValue(), entry.getKey()));
    }

    private final String fieldName;
    private final String fieldText;
    private final boolean optional;

    protected FieldInfo(String fieldName, String fieldText, boolean optional) {
        this.fieldName = fieldName;
        this.fieldText = fieldText;
        this.optional = optional;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldText() {
        return fieldText;
    }

    public boolean isOptional() {
        return optional;
    }
}

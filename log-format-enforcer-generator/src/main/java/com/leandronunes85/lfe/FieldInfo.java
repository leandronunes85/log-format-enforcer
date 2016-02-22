package com.leandronunes85.lfe;

public class FieldInfo {

    public static FieldInfo mandatory(String fieldName, String fieldText) {
        return new FieldInfo(fieldName, fieldText, false);
    }

    public static FieldInfo optional(String fieldName, String fieldText) {
        return new FieldInfo(fieldName, fieldText, true);
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

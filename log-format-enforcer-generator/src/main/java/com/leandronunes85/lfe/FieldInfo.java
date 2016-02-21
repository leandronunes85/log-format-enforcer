package com.leandronunes85.lfe;

public class FieldInfo {

    public static FieldInfo of(String fieldName, String fieldText) {
        return new FieldInfo(fieldName, fieldText);
    }

    private final String fieldName;
    private final String fieldText;

    protected FieldInfo(String fieldName, String fieldText) {
        this.fieldName = fieldName;
        this.fieldText = fieldText;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldText() {
        return fieldText;
    }
}

package com.leandronunes85.lfe;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.io.Serializable;

public class FieldInfo implements Serializable {

    public static FieldInfo mandatory(String fieldName, String fieldText) {
        return new FieldInfo(fieldName, fieldText, false);
    }

    public static FieldInfo optional(String fieldName, String fieldText) {
        return new FieldInfo(fieldName, fieldText, true);
    }

    private final String fieldName;
    private final String fieldText;
    private final boolean optional;

    public FieldInfo(String fieldName, String fieldText, boolean optional) {
        Preconditions.checkNotNull(fieldName, "Field requires a name");
        this.fieldName = fieldName;
        this.fieldText = MoreObjects.firstNonNull(fieldText, fieldName);
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

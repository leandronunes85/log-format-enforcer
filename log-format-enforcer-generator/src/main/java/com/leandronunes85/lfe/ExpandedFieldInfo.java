package com.leandronunes85.lfe;

import org.apache.commons.lang3.tuple.Pair;

import static com.leandronunes85.lfe.utils.Utils.capitalizeIt;

public class ExpandedFieldInfo extends FieldInfo {

    public static ExpandedFieldInfo from(FieldInfo fieldInfo, String returnType) {
        return new ExpandedFieldInfo(fieldInfo.getFieldName(), fieldInfo.getFieldText(), fieldInfo.isOptional(), returnType);
    }

    public static ExpandedFieldInfo from(Pair<FieldInfo, String> fieldInfoAndReturnType) {
        return from(fieldInfoAndReturnType.getLeft(), fieldInfoAndReturnType.getRight());
    }

    private final String interfaceName;
    private final String returnType;

    protected ExpandedFieldInfo(String fieldName, String fieldText, boolean optional, String returnType) {
        super(fieldName, fieldText, optional);
        this.interfaceName = capitalizeIt(fieldName);
        this.returnType = capitalizeIt(returnType);
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getReturnType() {
        return returnType;
    }
}

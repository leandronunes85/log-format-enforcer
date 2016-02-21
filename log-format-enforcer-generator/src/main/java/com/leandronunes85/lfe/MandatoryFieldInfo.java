package com.leandronunes85.lfe;

import org.apache.commons.lang3.tuple.Pair;

import static com.leandronunes85.lfe.utils.Utils.capitalizeIt;

public class MandatoryFieldInfo extends FieldInfo {

    public static MandatoryFieldInfo of(FieldInfo fieldInfo, String returnType) {
        return new MandatoryFieldInfo(fieldInfo.getFieldName(), fieldInfo.getFieldText(), returnType);
    }

    public static MandatoryFieldInfo of(Pair<FieldInfo, String> fieldInfoAndReturnType) {
        return of(fieldInfoAndReturnType.getLeft(), fieldInfoAndReturnType.getRight());
    }

    private final String interfaceName;
    private final String returnType;

    protected MandatoryFieldInfo(String fieldName, String fieldText, String returnType) {
        super(fieldName, fieldText);
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

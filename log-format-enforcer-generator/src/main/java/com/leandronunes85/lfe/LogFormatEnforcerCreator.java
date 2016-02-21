package com.leandronunes85.lfe;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.leandronunes85.lfe.utils.Utils.capitalizeIt;
import static com.leandronunes85.lfe.utils.Utils.pairingWithNextElement;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class LogFormatEnforcerCreator {

    private static final Pattern PACKAGE_PATTERN = compile("([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*");
    private static final String GENERIC_INTERFACE_NAME = "OptionalFields";
    private static final STGroupFile TEMPLATE = new STGroupFile("templates/LogFormatEnforcer.stg");


    public String createALogFormatEnforcer(String withPackageName,
                                           List<FieldInfo> withMandatoryFields,
                                           List<FieldInfo> withOptionalFields,
                                           String withEntrySeparator,
                                           String withValueDelimiter,
                                           String withKeyValueSeparator) {

        checkArguments(withPackageName, withMandatoryFields, withOptionalFields,
                withEntrySeparator, withValueDelimiter, withKeyValueSeparator);

        List<MandatoryFieldInfo> mandatoryFieldInfos = getMandatoryFieldInfos(withMandatoryFields);

        String builderEntryPoint = !mandatoryFieldInfos.isEmpty() ?
                mandatoryFieldInfos.get(0).getInterfaceName() : GENERIC_INTERFACE_NAME;

        return TEMPLATE.getInstanceOf("logFormatEnforcer")
                .add("package", withPackageName)
                .add("mandatoryFieldInfos", mandatoryFieldInfos)
                .add("optionalFields", withOptionalFields)
                .add("entrySeparator", withEntrySeparator)
                .add("valueDelimiter", withValueDelimiter)
                .add("keyValueSeparator", withKeyValueSeparator)
                .add("builderEntryPoint", builderEntryPoint)
                .render();
    }

    private void checkArguments(String withPackageName,
                                List<FieldInfo> withMandatoryFields,
                                List<FieldInfo> withOptionalFields,
                                String withEntrySeparator,
                                String withValueDelimiter,
                                String withKeyValueSeparator) {

        checkPackageName(withPackageName);

        checkArgument(withMandatoryFields != null, "mandatoryFields must not be null");

        checkArgument(withOptionalFields != null, "optionalFields must not be null");

        checkArgument(withEntrySeparator != null, "entrySeparator must not be null");

        checkArgument(withValueDelimiter != null, "valueDelimiter must not be null");

        checkArgument(withKeyValueSeparator != null, "keyValueSeparator must not be null");

    }

    private void checkPackageName(String withPackageName) {
        checkArgument(isNotEmpty(withPackageName), "packageName must not be empty");
        Matcher packageNameMatcher = PACKAGE_PATTERN.matcher(withPackageName);
        checkArgument(packageNameMatcher.find(), "packageName doesn't seem to be a valid package name");
        checkArgument(packageNameMatcher.matches(), "packageName doesn't seem to be a valid package name");
    }

    private List<MandatoryFieldInfo> getMandatoryFieldInfos(List<FieldInfo> withMandatoryFields) {
        return pairingWithNextElement(withMandatoryFields, GENERIC_INTERFACE_NAME, fieldInfo -> fieldInfo.getFieldName())
                .map(MandatoryFieldInfo::of)
                .collect(toList());
    }
}


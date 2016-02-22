package com.leandronunes85.lfe;

import org.stringtemplate.v4.STGroupFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.leandronunes85.lfe.utils.Utils.pairingWithNextElement;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class LogFormatEnforcerCreator {

    private static final Pattern PACKAGE_PATTERN = compile("([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*");
    private static final String LAST_INTERFACE_NAME = "OtherFields";
    private static final STGroupFile TEMPLATE = new STGroupFile("templates/LogFormatEnforcer.stg");


    public String createALogFormatEnforcer(String withPackageName,
                                           List<FieldInfo> withFields,
                                           String withEntrySeparator,
                                           String withValueDelimiterPrefix,
                                           String withValueDelimiterSuffix,
                                           String withKeyValueSeparator) {

        checkArguments(withPackageName, withFields, withEntrySeparator,
                withValueDelimiterPrefix, withValueDelimiterSuffix, withKeyValueSeparator);


        List<ExpandedFieldInfo> expandedFieldInfos = getExpandedFieldInfos(withFields, LAST_INTERFACE_NAME);

        String builderEntryPoint = !expandedFieldInfos.isEmpty() ?
                expandedFieldInfos.get(0).getInterfaceName() : LAST_INTERFACE_NAME;

        return TEMPLATE.getInstanceOf("logFormatEnforcer")
                .add("package", withPackageName)
                .add("expandedFieldInfos", expandedFieldInfos)
                .add("entrySeparator", withEntrySeparator)
                .add("valueDelimiterPrefix", withValueDelimiterPrefix)
                .add("valueDelimiterSuffix", withValueDelimiterSuffix)
                .add("keyValueSeparator", withKeyValueSeparator)
                .add("builderEntryPoint", builderEntryPoint)
                .add("lastInterfaceName", LAST_INTERFACE_NAME)
                .render();
    }

    private void checkArguments(String withPackageName,
                                List<FieldInfo> withFields,
                                String withEntrySeparator,
                                String withValueDelimiterPrefix,
                                String withValueDelimiterSuffix,
                                String withKeyValueSeparator) {

        checkPackageName(withPackageName);

        checkArgument(withFields != null, "fields must not be null");

        checkArgument(withEntrySeparator != null, "entrySeparator must not be null");

        checkArgument(withValueDelimiterPrefix != null, "valueDelimiterPrefix must not be null");

        checkArgument(withValueDelimiterSuffix != null, "valueDelimiterSuffix must not be null");

        checkArgument(withKeyValueSeparator != null, "keyValueSeparator must not be null");

    }

    private void checkPackageName(String withPackageName) {
        checkArgument(isNotEmpty(withPackageName), "packageName must not be empty");
        Matcher packageNameMatcher = PACKAGE_PATTERN.matcher(withPackageName);
        checkArgument(packageNameMatcher.find(), "packageName doesn't seem to be a valid package name");
        checkArgument(packageNameMatcher.matches(), "packageName doesn't seem to be a valid package name");
    }

    private List<ExpandedFieldInfo> getExpandedFieldInfos(List<FieldInfo> withMandatoryFields, String pairForLastElement) {
        return pairingWithNextElement(withMandatoryFields, pairForLastElement, fieldInfo -> fieldInfo.getFieldName())
                .map(ExpandedFieldInfo::from)
                .collect(toList());
    }
}


package com.leandronunes85.lfe;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.leandronunes85.lfe.utils.Utils.pairingWithNextElement;
import static com.leandronunes85.lfe.utils.Utils.sanitizeIt;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class LogFormatEnforcerCreator {

    private static final Pattern PACKAGE_PATTERN = compile("([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*");
    private static final String LAST_INTERFACE_NAME = "MoreFields";

    public String createALogFormatEnforcer(Language forLanguage,
                                           String withPackageName,
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

        return forLanguage.getTemplate()
                .getInstanceOf("logFormatEnforcer")
                .add("package", withPackageName)
                .add("expandedFieldInfos", expandedFieldInfos)
                .add("entrySeparator", sanitizeIt(withEntrySeparator))
                .add("valueDelimiterPrefix", sanitizeIt(withValueDelimiterPrefix))
                .add("valueDelimiterSuffix", sanitizeIt(withValueDelimiterSuffix))
                .add("keyValueSeparator", sanitizeIt(withKeyValueSeparator))
                .add("builderEntryPoint", builderEntryPoint)
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
        return pairingWithNextElement(withMandatoryFields, pairForLastElement, FieldInfo::getFieldName)
                .map(ExpandedFieldInfo::from)
                .collect(toList());
    }
}


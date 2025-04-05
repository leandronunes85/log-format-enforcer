package com.leandronunes85.lfe.plugin.gradle;

import com.leandronunes85.lfe.FieldInfo;
import com.leandronunes85.lfe.Language;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public interface LogFormatEnforcerPluginExtension {
    Property<String> getPackageName();

    ListProperty<FieldInfo> getFields();

    Property<Language> getLanguage();

    Property<String> getEntrySeparator();

    Property<String> getValueDelimiterPrefix();

    Property<String> getValueDelimiterSuffix();

    Property<String> getKeyValueSeparator();

    default FieldInfo mandatory(String name) {
        return mandatory(name, name);
    }

    default FieldInfo mandatory(String name, String text) {
        return new FieldInfo(name, text, false);
    }

    default FieldInfo optional(String name) {
        return optional(name, name);
    }

    default FieldInfo optional(String name, String text) {
        return new FieldInfo(name, text, true);
    }
}

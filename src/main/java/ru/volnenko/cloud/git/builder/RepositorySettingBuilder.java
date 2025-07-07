package ru.volnenko.cloud.git.builder;

import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class RepositorySettingBuilder {

    @NonNull
    private final Map<String, Map<String, String>> map = new LinkedHashMap<>();

    public RepositorySettingBuilder() {
        set("core", "repositoryformatversion", 0);
        set("core", "filemode", false);
        set("core", "bare", true);
        set("core", "logallrefupdates", true);
        set("core", "ignorecase", true);
        set("core", "precomposeunicode", true);
    }

    @NonNull
    public Map<String, String> group(@NonNull final String group) {
        if (map.containsKey(group)) return map.get(group);
        @NonNull final Map<String, String> item = new LinkedHashMap<>();
        map.put(group, item);
        return item;
    }

    public void set(@NonNull final String group, @NonNull final String name, Integer value) {
        if (value == null) {
            group(group).remove(name);
            return;
        }
        group(group).put(name, value.toString());
    }

    public void set(@NonNull final String group, @NonNull final String name, Boolean value) {
        if (value == null) {
            group(group).remove(name);
            return;
        }
        group(group).put(name, value.toString());
    }

    public void set(@NonNull final String group, @NonNull final String name, String value) {
        if (value == null) {
            group(group).remove(name);
            return;
        }
        group(group).put(name, value);
    }

    @NonNull
    public Set<String> groups() {
        return map.keySet();
    }

    @Override
    public String toString() {
        @NonNull final StringBuilder builder = new StringBuilder();
        for (@NonNull final String group: groups()) {
            builder.append("[").append(group).append("]").append("\n");
            @NonNull final Map<String,String> map = group(group);
            for (@NonNull final String key: map.keySet()) {
                @NonNull final String value = map.get(key);
                builder.append("\t").append(key).append(" = ").append(value).append("\n");
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(new RepositorySettingBuilder());
    }

}

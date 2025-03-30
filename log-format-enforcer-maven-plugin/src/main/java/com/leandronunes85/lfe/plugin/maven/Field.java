package com.leandronunes85.lfe.plugin.maven;

public class Field {
    public String name;
    public String text = name;
    public boolean mandatory = false;

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", mandatory=" + mandatory +
                '}';
    }
}

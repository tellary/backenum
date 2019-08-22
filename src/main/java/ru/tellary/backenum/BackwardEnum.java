package ru.tellary.backenum;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class BackwardEnum<T extends Enum<T>> {
    public static final String FUTURE = "FUTURE";

    private T value;
    private String name;

    private BackwardEnum () { }
    
    protected BackwardEnum(T value) {
        // Check target enum has FUTURE
        Enum.valueOf(value.getClass(), FUTURE);
        this.value = value;
    }

    protected BackwardEnum(Class<T> enumType, String name) {
        // Check target enum has FUTURE
        Enum.valueOf(enumType, FUTURE);
        try {
            value = Enum.valueOf(enumType, name);
        } catch (IllegalArgumentException ex) {
            this.value = Enum.valueOf(enumType, FUTURE);
            this.name = name;
        }
    }

    public String name() { return name == null ? value.name() : name; }
    public T get() { return value; }
}


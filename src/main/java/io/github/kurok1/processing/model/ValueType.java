package io.github.kurok1.processing.model;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public enum ValueType {

    INTEGER_32("integer", "int32"),
    STRING("string", ""),
    ARRAY("array", "")
    ;

    private final String type;
    private final String format;

    ValueType(String type, String format) {
        this.type = type;
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }
}

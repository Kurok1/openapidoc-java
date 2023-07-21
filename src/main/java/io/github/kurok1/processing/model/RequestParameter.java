package io.github.kurok1.processing.model;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class RequestParameter {

    private final int index;
    private final ParameterPosition in;

    private final boolean required;

    private final ValueType valueType;

    public RequestParameter(int index, ParameterPosition in, boolean required, ValueType valueType) {
        this.index = index;
        this.in = in;
        this.required = required;
        this.valueType = valueType;
    }

    public int getIndex() {
        return index;
    }

    public ParameterPosition getIn() {
        return in;
    }

    public boolean isRequired() {
        return required;
    }

    public String getType() {
        return valueType.getType();
    }


    public String getFormat() {
        return valueType.getFormat();
    }

    public enum ParameterPosition {
        QUERY("query"),
        PATH("path");

        private final String value;

        ParameterPosition(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

}

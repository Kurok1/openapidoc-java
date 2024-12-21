package io.github.kurok1.processing.model.request;

import io.github.kurok1.processing.model.ValueType;

/**
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public abstract class ApiParameter {
    
    
    private final int index;
    private final ParameterPosition in;
    
    private final boolean required;
    private String name;
    
    public ApiParameter(int index, ParameterPosition in, boolean required) {
        this.index = index;
        this.in = in;
        this.required = required;
    }
    
    public abstract ValueType getType();
    
    public int getIndex() {
        return index;
    }
    
    public ParameterPosition getIn() {
        return in;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public enum ParameterPosition {
        QUERY("query"),
        PATH("path"),
        BODY("body");
        
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

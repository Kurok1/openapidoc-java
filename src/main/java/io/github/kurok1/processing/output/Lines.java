package io.github.kurok1.processing.output;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public final class Lines {
    
    public static Struct.Line booleanLine(String name, boolean value) {
        return new BooleanLine(name, value);
    }
    
    public static Struct.Line stringLine(String name, String value) {
        return new StringLine(name, value);
    }
    
    public static Struct.Line integerLine(String name, int value) {
        return new IntegerLine(name, value);
    }
    
    public static Struct.Line numberLine(String name, BigDecimal value) {
        return new NumberLine(name, value);
    }
    
    public static Struct.Line objectLine(String name, Struct value) {
        return new StructLine(name, value);
    }
    
    public static <T> Struct.Line listLine(String name, List<T> value) {
        return new ArrayLine<>(name, value);
    }
    
    public static class BooleanLine implements Struct.Line {
        private final String name;
        private final boolean value;
        
        public BooleanLine(String name, boolean value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public Object getValue() {
            return value;
        }
    }
    
    public static class StringLine implements Struct.Line {
        private final String name;
        private final String value;
        
        public StringLine(String name, String value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public Object getValue() {
            return value;
        }
    }
    
    public static class IntegerLine implements Struct.Line {
        private final String name;
        private final Integer value;
        
        public IntegerLine(String name, Integer value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public Object getValue() {
            return value;
        }
    }
    
    public static class NumberLine implements Struct.Line {
        private final String name;
        private final BigDecimal value;
        
        public NumberLine(String name, BigDecimal value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public Object getValue() {
            return value;
        }
    }
    
    public static class StructLine implements Struct.Line {
        private final String name;
        private final Struct value;
        public StructLine(String name, Struct value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public Struct getValue() {
            return value;
        }
    }
    
    public static class ArrayLine<T> implements Struct.Line {
        private final String name;
        private final List<T> value;
        
        public ArrayLine(String name, List<T> value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public Object getValue() {
            return value;
        }
        
        public List<T> getArray() {
            return value;
        }
    }
    
}

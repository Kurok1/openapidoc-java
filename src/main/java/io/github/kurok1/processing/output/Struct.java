package io.github.kurok1.processing.output;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public interface Struct {
    
    interface Line {
        
        String getName();
        
        Object getValue();
    }
    
    void addProperty(Line line);
    
    default void addBooleanProperty(String name, boolean value) {
        addProperty(Lines.booleanLine(name, value));
    }
    
    default void addNumberProperty(String name, BigDecimal value) {
        addProperty(Lines.numberLine(name, value));
    }
    
    default void addIntegerProperty(String name, int value) {
        addProperty(Lines.integerLine(name, value));
    }
    
    default void addStringProperty(String name, String value) {
        addProperty(Lines.stringLine(name, value));
    }
    
    default void addStructProperty(String name, Struct struct) {
        addProperty(Lines.objectLine(name, struct));
    }
    
    default <T> void addListProperty(String name, List<T> value) {
        addProperty(Lines.listLine(name, value));
    }
    
    List<Line> getLines();
    
    static Struct newInstance() {
        return new SimpleStruct();
    }
}

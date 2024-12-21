package io.github.kurok1.processing.output;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class SimpleStruct implements Struct {
    
    private final List<Line> lines = new ArrayList<>();
    
    @Override
    public void addProperty(Line line) {
        this.lines.add(line);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("{");
        for (Line line : lines) {
            sb.append(line.toString());
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        
        return sb.toString();
    }
    
    public List<Line> getLines() {
        return lines;
    }
}

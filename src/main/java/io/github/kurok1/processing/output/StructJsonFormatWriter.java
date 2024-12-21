package io.github.kurok1.processing.output;

import io.github.kurok1.processing.GlobalContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public final class StructJsonFormatWriter implements StructWriter {
    
    public static final String ENABLED_KEY = "openapi.output.json.enabled";
    
    @Override
    public boolean enabled() {
        return GlobalContext.getPropertyAsBoolean(StructJsonFormatWriter.ENABLED_KEY, true);
    }
    
    @Override
    public void write(String fileName, Struct struct, OutputStream out) throws IOException {
        String json = toJson(struct);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out), 8 * 1024);
        
        writer.write(json);
        writer.flush();
    }
    
    public String toJson(Struct struct) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        
        for (Struct.Line line : struct.getLines()) {
            builder.append(toJsonLine(line));
        }
        if (!struct.getLines().isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        
        builder.append("}");
        return builder.toString();
    }

    
    public String toJsonLine(Struct.Line line) {
        if (line instanceof Lines.StructLine) {
            Lines.StructLine structLine = (Lines.StructLine) line;
            return "\"" + line.getName() + "\": " + toJson(structLine.getValue()) + ",\n";
        }
        if (line instanceof Lines.ArrayLine) {
            Lines.ArrayLine<?> arrayLine = (Lines.ArrayLine<?>) line;
            StringBuilder arrayBuilder = new StringBuilder();
            arrayBuilder.append("\"").append(arrayLine.getName()).append("\": ").append("[\n");
            for (Object item : arrayLine.getArray()) {
                if (item instanceof Struct)
                    arrayBuilder.append(toJson((Struct) item)).append(",\n");
                else arrayBuilder.append("\"").append(item.toString()).append("\",\n");
            }
            if (!arrayLine.getArray().isEmpty()) {
                arrayBuilder.deleteCharAt(arrayBuilder.length()-1);
                arrayBuilder.deleteCharAt(arrayBuilder.length()-1);
            }
            
            arrayBuilder.append("],\n");
            return arrayBuilder.toString();
        }
        
        return "\"" + line.getName() + "\": \"" + line.getValue().toString() + "\",\n";
    }
}

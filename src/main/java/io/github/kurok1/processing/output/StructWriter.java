package io.github.kurok1.processing.output;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public interface StructWriter {
    
    boolean enabled();
    
    void write(String fileName, Struct struct, OutputStream out) throws IOException;
    
}

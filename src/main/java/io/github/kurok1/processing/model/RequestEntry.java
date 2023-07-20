package io.github.kurok1.processing.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class RequestEntry {

    private final String sourceCode;

    private final Set<String> uris;

    public RequestEntry(String sourceCode) {
        this.sourceCode = sourceCode;
        this.uris = new HashSet<>();
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public Set<String> getUris() {
        return uris;
    }

    public void addUri(String uri) {
        this.uris.add(uri);
    }

    public String uri2String() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        if (!uris.isEmpty()) {
            for (String uri : uris)
                buffer.append("\"").append(uri).append("\",");
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]");
        return buffer.toString();
    }
}

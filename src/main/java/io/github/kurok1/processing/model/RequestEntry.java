/*
 * Copyright [2024] [Kurok1]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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

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

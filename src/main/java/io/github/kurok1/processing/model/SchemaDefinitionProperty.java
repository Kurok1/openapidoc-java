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

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 * @see ValueSchemaDefinitionProperty
 * @see ObjectSchemaDefinitionProperty
 * @see ArraySchemaDefinitionProperty
 * @see MapSchemaDefinitionProperty
 */
public interface SchemaDefinitionProperty {
    
    String getId();
    
    ValueType getValueType();
    
    String getName();
    
    default String getDescription() {
        return "";
    }
    
    default String getType() {
        return getValueType().getType();
    }
    
    default String getFormat() {
        return getValueType().getFormat();
    }
    
    class ValueSchemaDefinitionProperty implements SchemaDefinitionProperty {
        private final String id;
        private final ValueType valueType;
        private final String name;
        
        private String description;
        
        public ValueSchemaDefinitionProperty(String id, ValueType valueType, String name) {
            this.id = id;
            this.valueType = valueType;
            this.name = name;
        }
        
        public String getId() {
            return id;
        }
        
        @Override
        public ValueType getValueType() {
            return valueType;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
    }
    
    class ArraySchemaDefinitionProperty implements SchemaDefinitionProperty {
        private final String id;
        private final String name;
        private final SchemaDefinitionProperty item;
        private final ValueType valueType = ValueType.ARRAY;
        
        private String description;
        
        public ArraySchemaDefinitionProperty(String id, String name, SchemaDefinitionProperty item) {
            this.id = id;
            this.name = name;
            this.item = item;
        }
        
        @Override
        public String getId() {
            return id;
        }
        
        @Override
        public ValueType getValueType() {
            return valueType;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return this.description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public SchemaDefinitionProperty getItem() {
            return item;
        }
    }
    
    class ObjectSchemaDefinitionProperty implements SchemaDefinitionProperty {
        private final String id;
        private final String name;
        private final Schema ref;
        private final ValueType valueType = ValueType.OBJECT;
        
        private String description;
        
        public ObjectSchemaDefinitionProperty(String id, String name, Schema ref) {
            this.id = id;
            this.name = name;
            this.ref = ref;
        }
        
        @Override
        public String getId() {
            return id;
        }
        
        @Override
        public ValueType getValueType() {
            return valueType;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return this.description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public Schema getRef() {
            return ref;
        }
    }
    
    class MapSchemaDefinitionProperty implements SchemaDefinitionProperty {
        private final String id;
        private final String name;
        private final SchemaDefinitionProperty additionalProperties;
        private final ValueType valueType = ValueType.OBJECT;
        
        private String description;
        
        public MapSchemaDefinitionProperty(String id, String name, SchemaDefinitionProperty additionalProperties) {
            this.id = id;
            this.name = name;
            this.additionalProperties = additionalProperties;
        }
        
        @Override
        public String getId() {
            return id;
        }
        
        @Override
        public ValueType getValueType() {
            return valueType;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return this.description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public SchemaDefinitionProperty getAdditionalProperties() {
            return additionalProperties;
        }
    }
    
}

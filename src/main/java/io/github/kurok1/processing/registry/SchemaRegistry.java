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

package io.github.kurok1.processing.registry;

import io.github.kurok1.processing.GlobalContext;
import io.github.kurok1.processing.model.Schema;
import io.github.kurok1.processing.model.SchemaDefinition;
import io.github.kurok1.processing.model.SchemaDefinitionProperty;
import io.github.kurok1.processing.model.ValueType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * todo schema解析
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public final class SchemaRegistry {
    
    private final Map<String, SchemaDefinition> definitions = new ConcurrentHashMap<>(128);
    
    public SchemaRegistry() {
        //cannot instantiate
    }
    
    public SchemaDefinition register(VariableElement variableElement, TypeElement element) {
        if (variableElement.asType() instanceof DeclaredType) {
            return register((DeclaredType) variableElement.asType(), element);
        }
        throw new IllegalArgumentException(variableElement.asType().toString());
    }
    
    public String resolveId(TypeMirror typeMirror) {
        return typeMirror.toString();
    }
    
    public SchemaDefinition register(DeclaredType typeMirror, TypeElement element) {
        String id = resolveId(typeMirror);
        if (this.definitions.containsKey(id)) {
            return this.definitions.get(id);
        }
        //泛性映射
        Map<String, TypeMirror> typeMapping = new HashMap<>();
        int typeParameterSize = element.getTypeParameters().size();
        
        for (int i = 0; i < typeParameterSize; i++) {
            String parameterName = element.getTypeParameters().get(i).toString();
            TypeMirror parameterType = typeMirror.getTypeArguments().get(i);
            typeMapping.put(parameterName, parameterType);
        }
        
        SchemaDefinition definition = new SchemaDefinition();
        
        definition.setId(id);
        definition.setType(id);
        for (Element field : element.getEnclosedElements()) {
            if (field.getKind() != ElementKind.FIELD) {
                continue;
            }
            TypeMirror fieldType = field.asType();
            if (fieldType instanceof TypeVariable) {
                fieldType = typeMapping.get(fieldType.toString());
            }
            SchemaDefinitionProperty property = resolveProperty(field, fieldType, typeMapping);
            definition.addProperty(property);
        }
        
        definitions.put(id, definition);
        return definition;
    }
    
    public SchemaDefinitionProperty resolveProperty(Element fieldElement, TypeMirror fieldType, Map<String, TypeMirror> typeMapping) {
        String fieldName = fieldElement.getSimpleName().toString();
        String fieldId = resolveId(fieldType);
        ValueType valueType = ValueType.fromType(fieldType);
        if (valueType == ValueType.OBJECT) {
            if (!definitions.containsKey(fieldId)) {
                register((DeclaredType) fieldType, (TypeElement) GlobalContext.getTypeUtils().asElement(fieldType));
            }
            SchemaDefinition schemaDefinition = definitions.get(fieldId);
            Schema schema = new Schema();
            schema.setRef(schemaDefinition.getId());
            return new SchemaDefinitionProperty.ObjectSchemaDefinitionProperty(fieldId, fieldName, schema);
        } else if (valueType == ValueType.OBJECT_MAP) {
            TypeMirror mapValueType = ((DeclaredType)fieldType).getTypeArguments().get(1);
            return new SchemaDefinitionProperty.MapSchemaDefinitionProperty(fieldId, fieldName, resolveProperty(GlobalContext.getTypeUtils().asElement(mapValueType), mapValueType, typeMapping));
        } else if (valueType == ValueType.ARRAY) {
            TypeMirror componentType = ((ArrayType)fieldType).getComponentType();
            if (componentType instanceof TypeVariable)
                componentType = typeMapping.get(componentType.toString());
            return new SchemaDefinitionProperty.ArraySchemaDefinitionProperty(fieldId, fieldName, resolveProperty(GlobalContext.getTypeUtils().asElement(componentType), componentType, typeMapping));
        } else if (valueType == ValueType.LIST) {
            TypeMirror componentType = ((DeclaredType)fieldType).getTypeArguments().get(0);
            if (componentType instanceof TypeVariable)
                componentType = typeMapping.get(componentType.toString());
            return new SchemaDefinitionProperty.ArraySchemaDefinitionProperty(fieldId, fieldName, resolveProperty(GlobalContext.getTypeUtils().asElement(componentType), componentType, typeMapping));
        } else {
            return new SchemaDefinitionProperty.ValueSchemaDefinitionProperty(fieldName, valueType, fieldName);
        }
    }
    
    
    public Map<String, SchemaDefinition> getDefinitions() {
        return Collections.unmodifiableMap(definitions);
    }
}

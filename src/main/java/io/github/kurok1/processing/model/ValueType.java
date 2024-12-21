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

import io.github.kurok1.processing.GlobalContext;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public enum ValueType {

    VOID("void", ""),
    INTEGER_32("integer", "int32"),
    INTEGER_64("integer", "int64"),
    DECIMAL("NUMBER", "number"),
    BOOLEAN("boolean", ""),
    STRING("string", ""),
    ARRAY("array", ""),
    LIST("array", ""),
    OBJECT("object", ""),
    OBJECT_MAP("object", "")
    ;

    private final String type;
    private final String format;

    ValueType(String type, String format) {
        this.type = type;
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }
    
    public static ValueType fromType(TypeMirror typeMirror) {
        if (typeMirror.getKind() == TypeKind.VOID)
            return VOID;
        
        if (typeMirror instanceof PrimitiveType)
            return fromPrimitiveType((PrimitiveType) typeMirror);
        if (typeMirror instanceof ArrayType)
            return ARRAY;
        
        try {
            PrimitiveType primitiveType = GlobalContext.getTypeUtils().unboxedType(typeMirror);
            return fromPrimitiveType(primitiveType);
        } catch (Exception ignored) {
        
        }
        String typeName = typeMirror.toString();
        
        if (typeName.startsWith("java.lang.String"))
            return STRING;
        if (typeName.startsWith("java.util.List"))
            return LIST;
        if (typeName.startsWith("java.util.Map"))
            return OBJECT_MAP;
        
        return OBJECT;
        
    }
    
    public static ValueType fromPrimitiveType(PrimitiveType primitiveType) {
        switch (primitiveType.getKind()) {
            case BOOLEAN:return BOOLEAN;
            case BYTE:
            case SHORT:
            case INT:return INTEGER_32;
            case LONG:return INTEGER_64;
            case CHAR:return STRING;
            case FLOAT:
            case DOUBLE:return DECIMAL;
        }
        throw new IllegalArgumentException("unsupported primitive type: " + primitiveType);
    }
}

package io.github.kurok1.processing.builder;

import io.github.kurok1.processing.GlobalContext;
import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.model.ApiDefinitionBuilder;
import io.github.kurok1.processing.model.Schema;
import io.github.kurok1.processing.model.SchemaDefinition;
import io.github.kurok1.processing.model.ValueType;
import io.github.kurok1.processing.model.request.ApiParameter;
import io.github.kurok1.processing.model.request.ArrayApiParameter;
import io.github.kurok1.processing.model.request.MapApiParameter;
import io.github.kurok1.processing.model.request.SchemaApiParameter;
import io.github.kurok1.processing.model.request.ValueApiParameter;
import io.github.kurok1.processing.model.response.ApiResponse;
import io.github.kurok1.processing.model.response.ArrayApiResponse;
import io.github.kurok1.processing.model.response.MapApiResponse;
import io.github.kurok1.processing.model.response.ResponseStatus;
import io.github.kurok1.processing.model.response.SchemaApiResponse;
import io.github.kurok1.processing.model.response.ValueApiResponse;
import io.github.kurok1.processing.registry.SchemaRegistry;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public abstract class AbstractApiDefinitionContributor {
    
    protected final OpenApiContract openApiContract;
    
    protected SchemaRegistry schemaRegistry = new SchemaRegistry();
    
    public AbstractApiDefinitionContributor(OpenApiContract openApiContract) {
        this.openApiContract = openApiContract;
    }
    
    public OpenApiContract getOpenApiContract() {
        return openApiContract;
    }
    
    public void setSchemaRegistry(SchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }
    
    public SchemaRegistry getSchemaRegistry() {
        return schemaRegistry;
    }
    
    public abstract void contribute(ApiDefinitionBuilder definition, TypeElement classElement, ExecutableElement methodElement);
    
    protected ApiParameter resolveParameter(TypeElement classElement, ExecutableElement methodElement,
            int index, ApiParameter.ParameterPosition position, VariableElement variableElement, boolean required) {
        TypeMirror variableType = variableElement.asType();
        TypeElement parameter = (TypeElement) GlobalContext.getTypeUtils().asElement(variableType);
        ValueType valueType = ValueType.fromType(variableType);
        if (valueType == ValueType.ARRAY) {
            TypeMirror componentType = ((ArrayType)variableType).getComponentType();
            ValueType componentValueType = ValueType.fromType(componentType);
            if (componentValueType == ValueType.OBJECT) {
                SchemaDefinition parameterDefinition = getSchemaRegistry().register((DeclaredType) componentType, (TypeElement) GlobalContext.getTypeUtils().asElement(componentType));
                Schema schema = new Schema();
                schema.setRef(parameterDefinition.getId());
                return new ArrayApiParameter(index, position, required, new SchemaApiParameter(index, position, required, schema));
            } else {
                return new ArrayApiParameter(index, position, required, new ValueApiParameter(index, position, required, componentValueType));
            }
        } else if (valueType == ValueType.LIST) {
            TypeMirror componentType = ((DeclaredType)variableType).getTypeArguments().get(0);
            ValueType componentValueType = ValueType.fromType(componentType);
            if (componentValueType == ValueType.OBJECT) {
                SchemaDefinition parameterDefinition = getSchemaRegistry().register((DeclaredType) componentType, (TypeElement) GlobalContext.getTypeUtils().asElement(componentType));
                Schema schema = new Schema();
                schema.setRef(parameterDefinition.getId());
                return new ArrayApiParameter(index, position, required, new SchemaApiParameter(index, position, required, schema));
            } else {
                return new ArrayApiParameter(index, position, required, new ValueApiParameter(index, position, required, componentValueType));
            }
        } else if (valueType == ValueType.OBJECT_MAP) {
            TypeMirror mapValueType = ((DeclaredType)variableType).getTypeArguments().get(1);
            ValueType componentValueType = ValueType.fromType(mapValueType);
            if (componentValueType == ValueType.OBJECT) {
                SchemaDefinition parameterDefinition = getSchemaRegistry().register((DeclaredType) mapValueType, (TypeElement) GlobalContext.getTypeUtils().asElement(mapValueType));
                Schema schema = new Schema();
                schema.setRef(parameterDefinition.getId());
                return new MapApiParameter(index, position, required, new SchemaApiParameter(index, position, required, schema));
            } else {
                return new MapApiParameter(index, position, required, new ValueApiParameter(index, position, required, componentValueType));
            }
        } else if (valueType == ValueType.OBJECT) {
            SchemaDefinition parameterDefinition = getSchemaRegistry().register(variableElement, parameter);
            Schema schema = new Schema();
            schema.setRef(parameterDefinition.getId());
            return new SchemaApiParameter(index, position, required, schema);
        } else {
            return new ValueApiParameter(index, position, required, valueType);
        }
    }
    
    protected ApiResponse resolveResponse(ResponseStatus status, ExecutableElement methodElement, DeclaredType returnType) {
        ValueType valueType = ValueType.fromType(returnType);
        
        if (valueType == ValueType.VOID)
            return null;
        
        if (valueType == ValueType.ARRAY) {
            TypeMirror componentType = ((ArrayType)returnType).getComponentType();
            ValueType componentValueType = ValueType.fromType(componentType);
            if (componentValueType == ValueType.OBJECT) {
                SchemaDefinition parameterDefinition = getSchemaRegistry().register((DeclaredType) componentType, (TypeElement) GlobalContext.getTypeUtils().asElement(componentType));
                Schema schema = new Schema();
                schema.setRef(parameterDefinition.getId());
                return new ArrayApiResponse(status, new SchemaApiResponse(status, schema));
            } else {
                return new ArrayApiResponse(status, new ValueApiResponse(status, componentValueType));
            }
        } else if (valueType == ValueType.LIST) {
            TypeMirror componentType = ((DeclaredType)returnType).getTypeArguments().get(0);
            ValueType componentValueType = ValueType.fromType(componentType);
            if (componentValueType == ValueType.OBJECT) {
                SchemaDefinition parameterDefinition = getSchemaRegistry().register((DeclaredType) componentType, (TypeElement) GlobalContext.getTypeUtils().asElement(componentType));
                Schema schema = new Schema();
                schema.setRef(parameterDefinition.getId());
                return new ArrayApiResponse(status, new SchemaApiResponse(status, schema));
            } else {
                return new ArrayApiResponse(status, new ValueApiResponse(status, componentValueType));
            }
        } else if (valueType == ValueType.OBJECT_MAP) {
            TypeMirror mapValueType = ((DeclaredType)returnType).getTypeArguments().get(1);
            ValueType componentValueType = ValueType.fromType(mapValueType);
            if (componentValueType == ValueType.OBJECT) {
                SchemaDefinition parameterDefinition = getSchemaRegistry().register((DeclaredType) mapValueType, (TypeElement) GlobalContext.getTypeUtils().asElement(mapValueType));
                Schema schema = new Schema();
                schema.setRef(parameterDefinition.getId());
                return new MapApiResponse(status, new SchemaApiResponse(status, schema));
            } else {
                return new MapApiResponse(status, new ValueApiResponse(status, componentValueType));
            }
        } else if (valueType == ValueType.OBJECT) {
            SchemaDefinition parameterDefinition = getSchemaRegistry().register((DeclaredType) returnType, (TypeElement) GlobalContext.getTypeUtils().asElement(returnType));
            Schema schema = new Schema();
            schema.setRef(parameterDefinition.getId());
            return new SchemaApiResponse(status, schema);
        } else {
            return new ValueApiResponse(status, valueType);
        }
    }
}

package io.github.kurok1.processing.output.v3_1;

import io.github.kurok1.processing.GlobalContext;
import io.github.kurok1.processing.model.SchemaDefinition;
import io.github.kurok1.processing.model.SchemaDefinitionProperty;
import io.github.kurok1.processing.model.Tag;
import io.github.kurok1.processing.model.request.ApiParameter;
import io.github.kurok1.processing.model.request.ArrayApiParameter;
import io.github.kurok1.processing.model.request.MapApiParameter;
import io.github.kurok1.processing.model.request.SchemaApiParameter;
import io.github.kurok1.processing.model.request.ValueApiParameter;
import io.github.kurok1.processing.model.response.ApiResponse;
import io.github.kurok1.processing.model.response.ArrayApiResponse;
import io.github.kurok1.processing.model.response.MapApiResponse;
import io.github.kurok1.processing.model.response.SchemaApiResponse;
import io.github.kurok1.processing.model.response.ValueApiResponse;
import io.github.kurok1.processing.output.DocumentGeneratorInterceptor;
import io.github.kurok1.processing.output.DocumentGenerator;
import io.github.kurok1.processing.output.Struct;
import io.github.kurok1.processing.model.ApiModelDefinition;
import io.github.kurok1.processing.registry.ApiDefinitionRegistry;
import io.github.kurok1.processing.registry.SchemaRegistry;
import io.github.kurok1.processing.registry.TagRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class OpenApi3_1DocumentGenerator implements DocumentGenerator {
    
    public static final String ENABLED_KEY = "openapi.v3_1.enabled";
    
    /**
     * constants...
     */
    private static final String OPEN_API_VERSION = "3.1.0";
    
    private final List<DocumentGeneratorInterceptor> interceptors = new ArrayList<>();
    
    @Override
    public boolean enabled() {
        return GlobalContext.getPropertyAsBoolean(ENABLED_KEY, true);
    }
    
    @Override
    public Struct write(TagRegistry tagRegistry, SchemaRegistry schemaRegistry,
            ApiDefinitionRegistry apiDefinitionRegistry) {
        Struct struct = Struct.newInstance();
        //write version.
        struct.addStringProperty("openapi", OPEN_API_VERSION);
        //write info.
        Struct info = Struct.newInstance();
        info.addStringProperty("title", GlobalContext.getProperty(GlobalContext.API_INFO_TITLE, "title"));
        info.addStringProperty("version", GlobalContext.getProperty(GlobalContext.API_INFO_VERSION, "1.0.0"));
        struct.addStructProperty("info", info);
        //write servers.
        List<Struct> serverStructs = new ArrayList<>();
        List<String> serverList = GlobalContext.getPropertyAsList(GlobalContext.SERVER_LIST);
        for (String server : serverList) {
            Struct serverStruct = Struct.newInstance();
            serverStruct.addStringProperty("url", server);
            serverStructs.add(serverStruct);
        }
        struct.addListProperty("servers", serverStructs);
        //write tags.
        List<Struct> tagList = new ArrayList<>();
        for (Tag tag : tagRegistry.getTags()) {
            Struct tagStruct = Struct.newInstance();
            tagStruct.addStringProperty("name", tag.getName());
            tagStruct.addStringProperty("description", tag.getDescription());
            tagList.add(tagStruct);
            onTagCollect(tagRegistry, tag);
        }
        info.addListProperty("tags", tagList);
        //write paths.
        List<ApiModelDefinition> definitions = apiDefinitionRegistry.getDefinitions();
        
        Struct paths = Struct.newInstance();
        Map<String, List<ApiModelDefinition>> groupByPath = definitions.stream()
                .collect(Collectors.groupingBy(ApiModelDefinition::getPath));
        
        
        for (Map.Entry<String, List<ApiModelDefinition>> entry : groupByPath.entrySet()) {
            String path = entry.getKey();
            paths.addStructProperty(path, createRootApiStruct(apiDefinitionRegistry, path, entry.getValue()));
        }
        struct.addStructProperty("paths", paths);
        //write components.
        
        Struct components = Struct.newInstance();
        Struct schemas = Struct.newInstance();
        
        for (Map.Entry<String, SchemaDefinition> definitionItem : schemaRegistry.getDefinitions().entrySet()) {
            SchemaDefinition definition = definitionItem.getValue();
            schemas.addStructProperty(definitionItem.getKey(), createSchema(definition));
            onSchemaCollect(schemaRegistry, definition);
        }
        components.addStructProperty("schemas", schemas);
        struct.addStructProperty("components", components);
        return struct;
    }
    
    @Override
    public void registerInterceptor(DocumentGeneratorInterceptor lifecycle) {
        this.interceptors.add(lifecycle);
    }
    
    protected void onTagCollect(TagRegistry tagRegistry, Tag tag) {
        for (DocumentGeneratorInterceptor interceptor : this.interceptors) {
            interceptor.onTagCollect(tagRegistry, tag);
        }
    }
    
    protected void onApiCollect(ApiDefinitionRegistry registry, ApiModelDefinition apiModel) {
        for (DocumentGeneratorInterceptor interceptor : this.interceptors) {
            interceptor.onApiCollect(registry, apiModel);
        }
    }
    
    protected void onSchemaCollect(SchemaRegistry schemaRegistry, SchemaDefinition schema) {
        for (DocumentGeneratorInterceptor interceptor : this.interceptors) {
            interceptor.onSchemaCollect(schemaRegistry, schema);
        }
    }
    
    protected Struct createRootApiStruct(ApiDefinitionRegistry apiDefinitionRegistry, String path, List<ApiModelDefinition> definitions) {
        Struct struct = Struct.newInstance();
        for (ApiModelDefinition definition : definitions) {
            String method = definition.getMethod();
            struct.addStructProperty(method, createApiStruct(definition));
            onApiCollect(apiDefinitionRegistry, definition);
        }
        return struct;
    }
    
    protected Struct createApiStruct(ApiModelDefinition definition) {
        Struct struct = Struct.newInstance();
        //tags
        struct.addListProperty("tags", definition.getTags());
        struct.addStringProperty("summary", definition.getSummary());
        struct.addStringProperty("description", definition.getDescription());
        struct.addStringProperty("operationId", definition.getOperationId());
        ApiParameter requestBody = null;
        List<ApiParameter> requestParameters = new ArrayList<>();
        //resolve request body and request parameter
        for (ApiParameter parameter : definition.getParameters()) {
            if (parameter.getIn() == ApiParameter.ParameterPosition.BODY)
                requestBody = parameter;
            else requestParameters.add(parameter);
        }
        
        
        //set request body
        if (requestBody != null) {
            struct.addStructProperty("requestBody", createRequestBodyStruct(definition, requestBody));
        }
        
        //set request param and path variable
        if (!requestParameters.isEmpty()) {
            List<Struct> parameters = new ArrayList<>();
            for (ApiParameter parameter : requestParameters) {
                parameters.add(createRequestParameters(definition, parameter));
            }
            struct.addListProperty("parameters", parameters);
        }
        
        //set response
        if (!definition.getResponses().isEmpty()) {
            Struct responses = Struct.newInstance();
            for (ApiResponse response : definition.getResponses()) {
                responses.addStructProperty(String.valueOf(response.getStatusCode()), createResponseStruct(definition, response));
            }
            struct.addStructProperty("responses", responses);
        }
        
        return struct;
    }
    
    protected Struct createRequestParameters(ApiModelDefinition definition, ApiParameter parameter) {
        Struct struct = Struct.newInstance();
        
        struct.addStringProperty("name", parameter.getName());
        struct.addStringProperty("in", parameter.getIn().toString());
        struct.addBooleanProperty("required", parameter.isRequired());
        struct.addStructProperty("schema", buildStruct(parameter));
        return struct;
    }
    
    protected Struct createRequestBodyStruct(ApiModelDefinition definition, ApiParameter requestBody) {
        Struct struct = Struct.newInstance();
        struct.addStringProperty("description", "");
        
        
        Struct content = Struct.newInstance();
        for (String consume : definition.getConsumes()) {
            Struct consumeStruct = Struct.newInstance();
            
            consumeStruct.addStructProperty("schema", buildStruct(requestBody));
            content.addStructProperty(consume, consumeStruct);
        }
        struct.addBooleanProperty("required", requestBody.isRequired());
        struct.addStructProperty("content", content);
        return struct;
    }
    
    protected Struct createResponseStruct(ApiModelDefinition definition, ApiResponse apiResponse) {
        Struct struct = Struct.newInstance();
        
        Struct content = Struct.newInstance();
        for (String produce : definition.getProduces()) {
            Struct produceStruct = Struct.newInstance();
            
            produceStruct.addStructProperty("schema", buildStruct(apiResponse));
            content.addStructProperty(produce, produceStruct);
        }
        
        struct.addStringProperty("description", apiResponse.getStatusMessage());
        struct.addStructProperty("content", content);
        
        return struct;
    }
    
    protected Struct buildStruct(ApiParameter apiParameter) {
        Struct struct = Struct.newInstance();
        struct.addStringProperty("type", apiParameter.getType().getType());
        if (apiParameter instanceof ValueApiParameter) {
            struct.addStringProperty("format", apiParameter.getType().getFormat());
        } else if (apiParameter instanceof ArrayApiParameter) {
            ArrayApiParameter arrayApiParameter = (ArrayApiParameter) apiParameter;
            ApiParameter component = arrayApiParameter.getComponent();
            struct.addStructProperty("items", buildStruct(component));
        } else if (apiParameter instanceof MapApiParameter) {
            MapApiParameter mapApiParameter = (MapApiParameter) apiParameter;
            struct.addStructProperty("additionalProperties", buildStruct(mapApiParameter.getAdditionParameter()));
        } else if (apiParameter instanceof SchemaApiParameter) {
            SchemaApiParameter schemaApiParameter = (SchemaApiParameter) apiParameter;
            struct.addStringProperty("$ref", "#/components/schemas/" + schemaApiParameter.getSchema().getRef());
        }
        
        return struct;
    }
    
    protected Struct buildStruct(ApiResponse response) {
        Struct struct = Struct.newInstance();
        struct.addStringProperty("type", response.getType().getType());
        if (response instanceof ValueApiResponse) {
            struct.addStringProperty("format", response.getType().getFormat());
        } else if (response instanceof ArrayApiResponse) {
            ArrayApiResponse arrayApiResponse = (ArrayApiResponse) response;
            ApiResponse component = arrayApiResponse.getComponent();
            struct.addStructProperty("items", buildStruct(component));
        } else if (response instanceof MapApiResponse) {
            MapApiResponse mapApiResponse = (MapApiResponse) response;
            struct.addStructProperty("additionalProperties", buildStruct(mapApiResponse.getAdditionProperty()));
        } else if (response instanceof SchemaApiResponse) {
            SchemaApiResponse schemaApiResponse = (SchemaApiResponse) response;
            struct.addStringProperty("$ref", "#/components/schemas/" + schemaApiResponse.getSchema().getRef());
        }
        
        return struct;
    }
    
    protected Struct buildStruct(SchemaDefinitionProperty property) {
        Struct struct = Struct.newInstance();
        struct.addStringProperty("type", property.getType());
        if (property instanceof SchemaDefinitionProperty.ValueSchemaDefinitionProperty) {
            struct.addStringProperty("format", property.getFormat());
        } else if (property instanceof SchemaDefinitionProperty.ArraySchemaDefinitionProperty) {
            SchemaDefinitionProperty.ArraySchemaDefinitionProperty array = (SchemaDefinitionProperty.ArraySchemaDefinitionProperty) property;
            SchemaDefinitionProperty component = array.getItem();
            struct.addStructProperty("items", buildStruct(component));
        } else if (property instanceof SchemaDefinitionProperty.MapSchemaDefinitionProperty) {
            SchemaDefinitionProperty.MapSchemaDefinitionProperty map = (SchemaDefinitionProperty.MapSchemaDefinitionProperty) property;
            struct.addStructProperty("additionalProperties", buildStruct(map.getAdditionalProperties()));
        } else if (property instanceof SchemaDefinitionProperty.ObjectSchemaDefinitionProperty) {
            SchemaDefinitionProperty.ObjectSchemaDefinitionProperty object = (SchemaDefinitionProperty.ObjectSchemaDefinitionProperty) property;
            struct.addStringProperty("$ref", "#/components/schemas/" + object.getRef().getRef());
        }
        
        return struct;
    }
    
    protected Struct createSchema(SchemaDefinition definition) {
        Struct struct = Struct.newInstance();
        struct.addStringProperty("type", definition.getType());
        Struct properties = Struct.newInstance();
        for (SchemaDefinitionProperty property : definition.getProperties()) {
            properties.addStructProperty(property.getName(), buildStruct(property));
        }
        struct.addStructProperty("properties", properties);
        return struct;
    }
    
    @Override
    public String getDocumentPath() {
        return "META-INF/openapi_v3_1.json";
    }
}

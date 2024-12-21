package io.github.kurok1.processing.model;

import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.model.request.ApiParameter;
import io.github.kurok1.processing.model.response.ApiResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ApiModelDefinition {
    
    private final OpenApiContract.PathMethodInfo pathMethodInfo;
    private final List<ApiParameter> parameters;
    private final List<ApiResponse> responses;
    
    private final String summary;
    private final String description;
    private final List<String> tags;
    
    public ApiModelDefinition(ApiDefinitionBuilder builder, OpenApiContract.PathMethodInfo pathMethodInfo) {
        this.pathMethodInfo = pathMethodInfo;
        this.parameters = builder.getParameters();
        this.responses = builder.getResponses();
        this.summary = builder.getSummary();
        this.description = builder.getDescription();
        this.tags = builder.getTags();
    }
    
    public OpenApiContract.PathMethodInfo getPathMethodInfo() {
        return pathMethodInfo;
    }
    
    public List<String> getProduces() {
        return Collections.unmodifiableList(new ArrayList<>(pathMethodInfo.getProduces()));
    }
    
    public List<String> getConsumes() {
        return Collections.unmodifiableList(new ArrayList<>(pathMethodInfo.getConsumes()));
    }
    
    public List<ApiParameter> getParameters() {
        return parameters;
    }
    
    public List<ApiResponse> getResponses() {
        return responses;
    }
    
    public String getSummary() {
        return this.summary;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }
    
    public String getPath() {
        return getPathMethodInfo().getPath();
    }
    
    public String getOperationId() {
        return getPathMethodInfo().getOperationId();
    }
    
    public String getMethod() {
        return getPathMethodInfo().getMethod();
    }
    
}

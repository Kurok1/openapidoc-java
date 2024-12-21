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

import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.model.request.ApiParameter;
import io.github.kurok1.processing.model.response.ApiResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ApiDefinitionBuilder {
    
    private String operationId;
    
    private String summary = "";
    
    private String method;

    private List<String> tags = new ArrayList<>();

    private String description = "";

    private String path;
    
    private List<String> consumes = Collections.singletonList("*/*");
    private List<String> produces = Collections.singletonList("*/*");

    private final List<OpenApiContract.PathMethodInfo> pathMethods = new ArrayList<>();
    
    private final List<ApiParameter> parameters = new ArrayList<>();

    private final List<ApiResponse> responses = new ArrayList<>();
    
    public String getOperationId() {
        return operationId;
    }
    
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public ApiDefinitionBuilder setMethod(String method) {
        this.method = method;
        return this;
    }
    
    public ApiDefinitionBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public ApiDefinitionBuilder setPath(String path) {
        this.path = path;
        return this;
    }
    
    public ApiDefinitionBuilder addPathMethodInfo(OpenApiContract.PathMethodInfo pathMethodInfo) {
        this.pathMethods.add(pathMethodInfo);
        return this;
    }
    
    public ApiDefinitionBuilder addParameter(ApiParameter parameter) {
        parameters.add(parameter);
        return this;
    }
    
    public ApiDefinitionBuilder addResponse(ApiResponse response) {
        responses.add(response);
        return this;
    }
    
    public List<String> getConsumes() {
        return consumes;
    }
    
    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }
    
    public List<String> getProduces() {
        return produces;
    }
    
    public void setProduces(List<String> produces) {
        this.produces = produces;
    }
    
    public void addTag(String tag) {
        tags.add(tag);
    }
    
    public String getPath() {
        return path;
    }
    
    public String getMethod() {
        return method;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<ApiParameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
    
    public List<ApiResponse> getResponses() {
        return Collections.unmodifiableList(responses);
    }
    
    public List<OpenApiContract.PathMethodInfo> getPathMethods() {
        return Collections.unmodifiableList(pathMethods);
    }
}

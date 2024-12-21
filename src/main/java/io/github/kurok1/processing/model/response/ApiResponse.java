package io.github.kurok1.processing.model.response;

import io.github.kurok1.processing.model.ValueType;

/**
 * 接口响应
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public abstract class ApiResponse {

    private final ResponseStatus status;
    
    public ApiResponse(ResponseStatus status) {
        this.status = status;
    }
    
    public ResponseStatus getStatus() {
        return status;
    }


    public int getStatusCode() {
        return this.status.getCode();
    }
    
    public String getStatusMessage() {
        return this.status.getDescription();
    }
    
    public abstract ValueType getType();
}

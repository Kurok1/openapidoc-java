package io.github.kurok1.processing.model.response;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public enum ResponseStatus {
    OK_200(200, "OK"),
    BAD_REQUEST_400(400, "Bad Request"),
    UN_AUTHORIZED_403(403, "unauthorized"),
    ;
    
    
    private final int code;
    private final String description;
    
    ResponseStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

package io.github.kurok1.processing.model;

/**
 * 接口响应
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ApiResponse {

    private String status;

    private String description;

    private Schema schema;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }
}

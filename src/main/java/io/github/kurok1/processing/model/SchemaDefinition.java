package io.github.kurok1.processing.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0
 */
public class SchemaDefinition {
    
    private String id;
    
    private String title;
    
    private String type = "object";
    
    private List<SchemaDefinitionProperty> properties = new ArrayList<>();
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getType() {
        return type;
    }
    
    public List<SchemaDefinitionProperty> getProperties() {
        return properties;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void addProperty(SchemaDefinitionProperty property) {
        this.properties.add(property);
    }
}

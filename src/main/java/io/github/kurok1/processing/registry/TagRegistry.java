package io.github.kurok1.processing.registry;

import io.github.kurok1.processing.model.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class TagRegistry {
    private final List<Tag> tags = new ArrayList<>();
    
    public TagRegistry() {}
    
    public void addTag(Tag tag) {
        tags.add(tag);
    }
    
    public List<Tag> getTags() {
        return Collections.unmodifiableList(this.tags);
    }
}

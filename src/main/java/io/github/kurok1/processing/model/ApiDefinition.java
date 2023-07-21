package io.github.kurok1.processing.model;

import io.github.kurok1.processing.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ApiDefinition {

    private String method;

    private String[] tags = Constants.EMPTY_ARRAY_STRING;

    private String description;

    private final String methodName;

    private final List<RequestParameter> parameters = new ArrayList<>();

    private final List<ApiResponse> responses = new ArrayList<>();

    public ApiDefinition(String methodName) {
        this.methodName = methodName;
    }
}

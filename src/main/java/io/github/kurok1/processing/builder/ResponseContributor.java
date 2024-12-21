package io.github.kurok1.processing.builder;

import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.model.ApiDefinitionBuilder;
import io.github.kurok1.processing.model.response.ApiResponse;
import io.github.kurok1.processing.model.response.ResponseStatus;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.Optional;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ResponseContributor extends AbstractApiDefinitionContributor {
    
    public ResponseContributor(OpenApiContract openApiContract) {
        super(openApiContract);
    }
    
    @Override
    public void contribute(ApiDefinitionBuilder definition, TypeElement classElement, ExecutableElement methodElement) {
        Optional<OpenApiContract.ParameterInfo> response = super.getOpenApiContract().resolveResponseBody(classElement, methodElement);
        if (response.isPresent()) {
            ApiResponse apiResponse = resolveResponse(ResponseStatus.OK_200, methodElement, (DeclaredType) methodElement.getReturnType());
            if (apiResponse != null)
                definition.addResponse(apiResponse);
        }
        
    }
}

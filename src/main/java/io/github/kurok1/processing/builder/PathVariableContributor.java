package io.github.kurok1.processing.builder;

import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.model.ApiDefinitionBuilder;
import io.github.kurok1.processing.model.request.ApiParameter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Optional;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class PathVariableContributor extends AbstractApiDefinitionContributor {
    
    public PathVariableContributor(OpenApiContract openApiContract) {
        super(openApiContract);
    }
    
    @Override
    public void contribute(ApiDefinitionBuilder definition, TypeElement classElement, ExecutableElement methodElement) {
        int index = 0;
        for (VariableElement variableElement : methodElement.getParameters()) {
            Optional<OpenApiContract.ParameterInfo> pathVariable = super.getOpenApiContract().resolvePathVariableIfEligible(index, methodElement, variableElement);
            if (pathVariable.isPresent()) {
                OpenApiContract.ParameterInfo parameterInfo = pathVariable.get();
                ApiParameter parameter = resolveParameter(classElement, methodElement,
                        index, ApiParameter.ParameterPosition.PATH, variableElement, parameterInfo.required());
                parameter.setName(parameterInfo.getName());
                definition.addParameter(parameter);
            }
            index++;
        }
    }
}
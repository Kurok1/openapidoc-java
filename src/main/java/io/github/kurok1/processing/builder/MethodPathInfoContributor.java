package io.github.kurok1.processing.builder;

import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.model.ApiDefinitionBuilder;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class MethodPathInfoContributor extends AbstractApiDefinitionContributor {
    
    public MethodPathInfoContributor(OpenApiContract openApiContract) {
        super(openApiContract);
    }
    
    @Override
    public void contribute(ApiDefinitionBuilder definition, TypeElement classElement, ExecutableElement methodElement) {
        List<OpenApiContract.PathMethodInfo> methodInfoList = super.getOpenApiContract().resolvePathInfo(classElement, methodElement);
        methodInfoList.forEach(definition::addPathMethodInfo);
    }
}

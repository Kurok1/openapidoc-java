package io.github.kurok1.processing.impl.spring;

import io.github.kurok1.processing.AbstractOpenApiProcessor;
import io.github.kurok1.processing.AnnotationUtils;
import io.github.kurok1.processing.OpenApiContract;
import io.github.kurok1.processing.builder.AbstractApiDefinitionContributor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@SupportedAnnotationTypes(value = {SpringAnnotations.ANNOTATION_CONTROLLER, SpringAnnotations.ANNOTATION_REST_CONTROLLER})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SpringMVCOpenApiProcessor extends AbstractOpenApiProcessor {
    
    
    @Override
    protected boolean elementIsEligible(Element element) {
        String usingControllerAnnotation = AnnotationUtils.determineAnnotationAnyPresent(element, SpringAnnotations.ANNOTATION_REST_CONTROLLER, SpringAnnotations.ANNOTATION_CONTROLLER);
        return !usingControllerAnnotation.isEmpty();
    }
    
    @Override
    protected OpenApiContract createOpenApiContract() {
        return new SpringMVCContract();
    }
    
    @Override
    protected List<AbstractApiDefinitionContributor> loadAdditionalContributors(OpenApiContract contract) {
        return Collections.emptyList();
    }
}

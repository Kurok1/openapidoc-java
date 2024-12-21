/*
 * Copyright [2024] [Kurok1]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.kurok1.processing.impl.spring;

import io.github.kurok1.processing.AnnotationUtils;
import io.github.kurok1.processing.GlobalContext;
import io.github.kurok1.processing.OpenApiContract;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class SpringMVCContract implements OpenApiContract {
    
    private final String[] MAPPINGS_ANNOTATIONS = {
            SpringAnnotations.ANNOTATION_REQUEST_MAPPINGS,
            SpringAnnotations.ANNOTATION_GET_MAPPINGS,
            SpringAnnotations.ANNOTATION_POST_MAPPINGS,
            SpringAnnotations.ANNOTATION_PUT_MAPPINGS,
            SpringAnnotations.ANNOTATION_DELETE_MAPPINGS
    };
    
    private final Map<String, String> annotationMethodMappings = new HashMap<>();
    
    private final Map<String, String> methodMappings = new HashMap<>();
    
    private final String DEFAULT_CONTENT_TYPE = "application/json";
    
    public SpringMVCContract() {
        annotationMethodMappings.put(SpringAnnotations.ANNOTATION_GET_MAPPINGS, "get");
        annotationMethodMappings.put(SpringAnnotations.ANNOTATION_POST_MAPPINGS, "post");
        annotationMethodMappings.put(SpringAnnotations.ANNOTATION_PUT_MAPPINGS, "put");
        annotationMethodMappings.put(SpringAnnotations.ANNOTATION_DELETE_MAPPINGS, "delete");
        
        methodMappings.put("org.springframework.web.bind.annotation.RequestMethod.GET", "get");
        methodMappings.put("org.springframework.web.bind.annotation.RequestMethod.POST", "post");
        methodMappings.put("org.springframework.web.bind.annotation.RequestMethod.PUT", "put");
        methodMappings.put("org.springframework.web.bind.annotation.RequestMethod.DELETE", "delete");
    }
    
    @Override
    public boolean support(TypeElement classElement, ExecutableElement method) {
        String methodAnnotation = AnnotationUtils.determineAnnotationAnyPresent(method, MAPPINGS_ANNOTATIONS);
        return !methodAnnotation.isEmpty();
    }
    
    @Override
    public List<PathMethodInfo> resolvePathInfo(TypeElement classElement, ExecutableElement method) {
        //1.检查类层的@RequestMapping
        String presentAnnotation = AnnotationUtils.determineAnnotationAnyPresent(classElement, MAPPINGS_ANNOTATIONS);
        String globalPath = AnnotationUtils.readAnnotationValue(classElement, presentAnnotation, "value", "path");
        List<String> globalConsumes = AnnotationUtils.readAnnotationValues(classElement, presentAnnotation, "consumes");
        List<String> globalProduces = AnnotationUtils.readAnnotationValues(classElement, presentAnnotation, "produces");
        
        List<String> methodAnnotations = AnnotationUtils.filterAnnotationPresent(method, MAPPINGS_ANNOTATIONS);
        //每个方法注解至少代表一个PathMethodInfo
        List<PathMethodInfo> pathMethodInfos = new ArrayList<>();
        for (String methodAnnotation : methodAnnotations) {
            String path = AnnotationUtils.readAnnotationValue(method, methodAnnotation, "value", "path");
            
            //拼接
            String uri = '/' + GlobalContext.getContextPath() + '/' + globalPath + '/' + path;
            uri = normalize(uri);
            
            List<String> consumes = AnnotationUtils.readAnnotationValues(method, presentAnnotation, "consumes");
            List<String> produces = AnnotationUtils.readAnnotationValues(method, presentAnnotation, "produces");
            Set<String> consumeSet = new HashSet<>(consumes);
            consumeSet.addAll(globalConsumes);
            Set<String> produceSet = new HashSet<>(produces);
            produceSet.addAll(globalProduces);
            
            if (consumeSet.isEmpty())
                consumeSet.add(DEFAULT_CONTENT_TYPE);
            if (produceSet.isEmpty())
                produceSet.add(DEFAULT_CONTENT_TYPE);
            
            if (annotationMethodMappings.containsKey(methodAnnotation)) {
                String requestMethod = annotationMethodMappings.get(methodAnnotation);
                SimplePathMethodInfo pathMethodInfo = new SimplePathMethodInfo(uri, requestMethod);
                pathMethodInfo.addConsumes(consumeSet);
                pathMethodInfo.addProduces(produceSet);
                pathMethodInfos.add(pathMethodInfo);
            } else {
                //使用RequestMapping
                List<String> methods = resolveRequestMappingMethods(method);
                for (String requestMethod : methods) {
                    SimplePathMethodInfo pathMethodInfo = new SimplePathMethodInfo(uri, requestMethod);
                    pathMethodInfo.addConsumes(consumeSet);
                    pathMethodInfo.addProduces(produceSet);
                    pathMethodInfos.add(pathMethodInfo);
                }
                
            }
        }
        return pathMethodInfos;
    }
    
    protected List<String> resolveRequestMappingMethods(ExecutableElement method) {
        AnnotationMirror mirror = method.getAnnotationMirrors() // 返回当前元素的所有注解集合
                .stream()
                .filter(annotation -> Objects.equals(SpringAnnotations.ANNOTATION_REQUEST_MAPPINGS, annotation.getAnnotationType().toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No annotation found for request mapping " + method));
        
        List<String> methods = new ArrayList<>();
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            if ("method".equals(name)) {
                List<Object> values = (List<Object>) entry.getValue().getValue();
                if (values == null)
                    break;
                
                for (Object v : values) {
                    if (methodMappings.containsKey(v.toString()))
                        methods.add(methodMappings.get(v.toString()));
                }
            }
        }
        if (methods.isEmpty())
            methods.add("get");
        return methods;
    }
    
    @Override
    public Optional<ParameterInfo> resolveRequestParameterIfEligible(int parameterIndex, ExecutableElement method,
            VariableElement parameterElement) {
        if (!AnnotationUtils.isAnnotationPresent(parameterElement, SpringAnnotations.ANNOTATION_REQUEST_PARAM))
            return Optional.empty();
        
        String name = AnnotationUtils.readAnnotationValue(parameterElement, SpringAnnotations.ANNOTATION_REQUEST_PARAM, "value", "name");
        if (name.isEmpty())
            name = parameterElement.getSimpleName().toString();
        
        String requiredValue = AnnotationUtils.readAnnotationValue(parameterElement, SpringAnnotations.ANNOTATION_REQUEST_PARAM, "required");
        return Optional.of(new SimpleParameterInfo(name, Boolean.parseBoolean(requiredValue), parameterElement));
    }
    
    @Override
    public Optional<ParameterInfo> resolvePathVariableIfEligible(int parameterIndex, ExecutableElement method,
            VariableElement parameterElement) {
        
        if (!AnnotationUtils.isAnnotationPresent(parameterElement, SpringAnnotations.ANNOTATION_PATH_VARIABLE))
            return Optional.empty();
        
        String name = AnnotationUtils.readAnnotationValue(parameterElement, SpringAnnotations.ANNOTATION_PATH_VARIABLE, "value", "name");
        if (name.isEmpty())
            name = parameterElement.getSimpleName().toString();
        
        String requiredValue = AnnotationUtils.readAnnotationValue(parameterElement, SpringAnnotations.ANNOTATION_PATH_VARIABLE, "required");
        return Optional.of(new SimpleParameterInfo(name, Boolean.parseBoolean(requiredValue), parameterElement));
    }
    
    @Override
    public Optional<ParameterInfo> resolveRequestBody(int parameterIndex, ExecutableElement method, VariableElement parameterElement) {
        if (!AnnotationUtils.isAnnotationPresent(parameterElement, SpringAnnotations.ANNOTATION_REQUEST_BODY))
            return Optional.empty();
        String requiredValue = AnnotationUtils.readAnnotationValue(parameterElement, SpringAnnotations.ANNOTATION_REQUEST_BODY, "required");

        return Optional.of(new SimpleParameterInfo("", Boolean.parseBoolean(requiredValue), parameterElement));
    }
    
    @Override
    public Optional<ParameterInfo> resolveResponseBody(TypeElement classElement, ExecutableElement method) {
        if (AnnotationUtils.isAnnotationPresent(classElement, SpringAnnotations.ANNOTATION_RESPONSE_BODY)
                || AnnotationUtils.isAnnotationPresent(classElement, SpringAnnotations.ANNOTATION_REST_CONTROLLER)
                || AnnotationUtils.isAnnotationPresent(method, SpringAnnotations.ANNOTATION_RESPONSE_BODY)) {
            if (method.getReturnType().getKind() != TypeKind.VOID)
                return Optional.of(new SimpleParameterInfo("", true, (TypeElement) GlobalContext.getTypeUtils().asElement(method.getReturnType())));
            
        }
        return Optional.empty();
    }
}

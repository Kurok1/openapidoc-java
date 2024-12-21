package io.github.kurok1.processing;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * open-api解析协议
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public interface OpenApiContract {
    
    boolean support(TypeElement classElement, ExecutableElement method);
    
    List<PathMethodInfo> resolvePathInfo(TypeElement classElement, ExecutableElement method);
    
    default String normalize(String path) {
        StringBuilder builder = new StringBuilder();
        
        char[] chars = path.toCharArray();
        for (char c : chars) {
            if (c == '/' && builder.length() > 0 && builder.charAt(builder.length() - 1) == '/')
                continue;
            else builder.append(c);
        }
        
        return builder.toString();
    }
    
    Optional<ParameterInfo> resolveRequestParameterIfEligible(int parameterIndex, ExecutableElement method, VariableElement parameterElement);
    
    Optional<ParameterInfo> resolvePathVariableIfEligible(int parameterIndex, ExecutableElement method, VariableElement parameterElement);
    
    Optional<ParameterInfo> resolveRequestBody(int parameterIndex, ExecutableElement method, VariableElement parameterElement);
    
    Optional<ParameterInfo> resolveResponseBody(TypeElement classElement, ExecutableElement method);
    
    interface PathMethodInfo {
        String getPath();
        String getMethod();
        Set<String> getConsumes();
        Set<String> getProduces();
        
        default String getOperationId() {
            return String.format("%s#%s", getPath(), getMethod());
        }
    }
    
    interface ParameterInfo {
        
        TypeElement getParameter();
        
        String getName();
        
        boolean required();
        
    }
    
    class SimplePathMethodInfo implements PathMethodInfo {
        private final String path;
        private final String method;
        private final Set<String> consumes = new HashSet<>();
        private final Set<String> produces = new HashSet<>();
        
        
        public SimplePathMethodInfo(String path, String method) {
            this.path = path;
            this.method = method;
        }
        
        @Override
        public String getPath() {
            return path;
        }
        
        @Override
        public String getMethod() {
            return method;
        }
        
        @Override
        public Set<String> getConsumes() {
            return consumes;
        }
        
        public void addConsumes(Collection<String> consumes) {
            this.consumes.addAll(consumes);
        }
        
        public void addConsume(String consume) {
            this.consumes.add(consume);
        }
        
        @Override
        public Set<String> getProduces() {
            return produces;
        }
        
        public void addProduces(Collection<String> produces) {
            this.produces.addAll(produces);
        }
        
        public void addProduce(String produce) {
            this.produces.add(produce);
        }
    }
    
    class SimpleParameterInfo implements ParameterInfo {
        private final String name;
        private final boolean required;
        private final TypeElement parameterType;
        public SimpleParameterInfo(String name, boolean required, TypeElement parameterType) {
            this.name = name;
            this.required = required;
            this.parameterType = parameterType;
        }
        
        public SimpleParameterInfo(String name, boolean required, VariableElement parameterElement) {
            this.name = name;
            this.required = required;
            this.parameterType = (TypeElement) GlobalContext.getTypeUtils().asElement(parameterElement.asType());
        }
        
        @Override
        public TypeElement getParameter() {
            return this.parameterType;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public boolean required() {
            return this.required;
        }
    }
}

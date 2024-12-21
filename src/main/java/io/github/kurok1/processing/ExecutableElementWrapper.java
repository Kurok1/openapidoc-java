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

package io.github.kurok1.processing;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class ExecutableElementWrapper implements ExecutableElement {

    private final ExecutableElement delegate;

    private final List<ExecutableElement> parents = new ArrayList<>();

    public ExecutableElementWrapper(ExecutableElement delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return delegate.getTypeParameters();
    }

    @Override
    public TypeMirror getReturnType() {
        return delegate.getReturnType();
    }

    @Override
    public List<? extends VariableElement> getParameters() {
        return delegate.getParameters();
    }

    @Override
    public TypeMirror getReceiverType() {
        return delegate.getReceiverType();
    }

    @Override
    public boolean isVarArgs() {
        return delegate.isVarArgs();
    }

    @Override
    public boolean isDefault() {
        return delegate.isDefault();
    }

    @Override
    public List<? extends TypeMirror> getThrownTypes() {
        return delegate.getThrownTypes();
    }

    @Override
    public AnnotationValue getDefaultValue() {
        return delegate.getDefaultValue();
    }

    @Override
    public Name getSimpleName() {
        return delegate.getSimpleName();
    }

    @Override
    public TypeMirror asType() {
        return delegate.asType();
    }

    @Override
    public ElementKind getKind() {
        return delegate.getKind();
    }

    @Override
    public Set<Modifier> getModifiers() {
        return delegate.getModifiers();
    }

    @Override
    public Element getEnclosingElement() {
        return delegate.getEnclosingElement();
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return delegate.getEnclosedElements();
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        if (this.parents.isEmpty())
            return delegate.getAnnotationMirrors();

        List<AnnotationMirror> list = new ArrayList<>(delegate.getAnnotationMirrors());
        for (ExecutableElement item : parents)
            list.addAll(item.getAnnotationMirrors());
        return list;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        if (this.parents.isEmpty())
            return delegate.getAnnotation(annotationType);

        A annotation = delegate.getAnnotation(annotationType);
        if (annotation == null) {
            for (ExecutableElement element : parents)
                if (element.getAnnotation(annotationType) != null)
                    return element.getAnnotation(annotationType);
        }

        return annotation;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        return delegate.accept(v, p);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        if (this.parents.isEmpty())
            return delegate.getAnnotationsByType(annotationType);

        List<A> result = new ArrayList<>(Arrays.asList(delegate.getAnnotationsByType(annotationType)));
        for (ExecutableElement item : parents)
            result.addAll(Arrays.asList(item.getAnnotationsByType(annotationType)));
        return (A[]) result.toArray();
    }

    public void addParent(ExecutableElement parent) {
        this.parents.add(parent);
    }

    public ExecutableElement getDelegate() {
        return this.delegate;
    }
}

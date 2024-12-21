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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class MethodUtils {

    /**
     * 获取某个类对象的所有方法，包含父类方法, 父类方法与子类方法想同取子类方法
     * @param environment 处置上下文
     * @param component 类对象
     * @return 方法集合
     * @see ExecutableElementWrapper
     */
    public static Collection<ExecutableElement> getMergedMethods(ProcessingEnvironment environment, TypeElement component) {
        if (component.getKind() != ElementKind.CLASS)
            return Collections.emptyList();

        Map<String, ExecutableElementWrapper> map = new HashMap<>();
        Elements elements = environment.getElementUtils();

        List<? extends Element> members = elements.getAllMembers(component);
        for (Element element : members) {
            if (element.getKind() == ElementKind.METHOD && element instanceof ExecutableElement) {
                ExecutableElement method = (ExecutableElement) element;
                map.put(generateMethodName(method), new ExecutableElementWrapper(method));
            }
        }
        TypeMirror superType = component.getSuperclass();
        List<? extends TypeMirror> interfaces = component.getInterfaces();
        List<TypeElement> typeElements = new ArrayList<>();
        if (superType.getKind() != TypeKind.NONE)
            typeElements.add(elements.getTypeElement(superType.toString()));
        for (TypeMirror typeMirror : interfaces)
            typeElements.add(elements.getTypeElement(environment.getTypeUtils().erasure(typeMirror).toString()));

        for (TypeElement superElement : typeElements) {
            forEachSuperClassMethod(environment, superElement, method -> {
                String name = generateMethodName(method);
                ExecutableElementWrapper wrapper = map.get(name);
                if (wrapper != null) {
                    ExecutableElement delegate = wrapper.getDelegate();
                    if (elements.overrides(delegate, method, component))
                        wrapper.addParent(method);
                }
            });
        }

        return new ArrayList<>(map.values());
    }

    private static void forEachSuperClassMethod(ProcessingEnvironment env, TypeElement typeElement, Consumer<ExecutableElement> consumer) {
        Elements elements = env.getElementUtils();
        Types types = env.getTypeUtils();
        List<? extends Element> membersOnSuper = env.getElementUtils().getAllMembers(typeElement);
        for (Element element : membersOnSuper) {
            if (element.getKind() == ElementKind.METHOD && element instanceof ExecutableElement) {
                ExecutableElement method = (ExecutableElement) element;
                consumer.accept(method);
            }
        }

        TypeMirror superType = typeElement.getSuperclass();
        List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
        List<TypeElement> typeElements = new ArrayList<>();
        if (superType.getKind() != TypeKind.NONE)
            typeElements.add(elements.getTypeElement(superType.toString()));
        for (TypeMirror typeMirror : interfaces) {
            typeElements.add(elements.getTypeElement(types.erasure(typeMirror).toString()));
        }


        for (TypeElement element : typeElements) {
            forEachSuperClassMethod(env, element, consumer);
        }


    }

    /**
     * 生成方法名称，考虑到重载，需要考虑方法签名
     * @param element 方法
     * @return 唯一方法名称
     */
    public static String generateMethodName(ExecutableElement element) {
        StringBuffer buffer = new StringBuffer();
        String name = element.getSimpleName().toString();
        buffer.append(name).append("#");
        List<? extends VariableElement> parameters = element.getParameters();
        for (VariableElement parameter : parameters) {
            String typeString = parameter.asType().toString();
            buffer.append(typeString).append(",");
        }
        return buffer.toString();
    }

}

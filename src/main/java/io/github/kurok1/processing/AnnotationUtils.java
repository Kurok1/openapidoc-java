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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class AnnotationUtils {

    /**
     * 判断当前元素是否被以下任意一注解标记过
     * @param element 元素，可以是类型或者方法
     * @param annotationName 注解名称
     * @return 被标记的注解名称
     */
    public static String determineAnnotationAnyPresent(Element element, String... annotationName) {
        if (annotationName.length == 0)
            return "";

        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror mirror : annotationMirrors) {
            for (String name : annotationName)
                if (mirror.getAnnotationType().toString().equals(name))
                    return name;
        }
        return "";
    }
    
    /**
     * 过滤当前元素，存在哪些给定的注解
     * @param element 元素，可以是参数或方法
     * @param annotationName 注解列表
     * @return 被哪些注解标记
     */
    public static List<String> filterAnnotationPresent(Element element, String... annotationName) {
        if (annotationName.length == 0)
            return Collections.emptyList();
        List<String> result = new ArrayList<>();
        
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror mirror : annotationMirrors) {
            for (String name : annotationName)
                if (mirror.getAnnotationType().toString().equals(name))
                    result.add(name);
        }
        
        return result;
    }

    public static boolean isAnnotationPresent(Element element, String annotationName) {
        return element.getAnnotationMirrors().stream()
                .anyMatch(mirror -> mirror.getAnnotationType().toString().equals(annotationName));
    }

    public static boolean isAnnotationPresent(TypeMirror typeMirror, String annotationName) {
        return typeMirror.getAnnotationMirrors().stream()
                .anyMatch(mirror -> mirror.getAnnotationType().toString().equals(annotationName));
    }

    public static String readAnnotationValue(Element element, String annotationName, String property, String... aliasProperties) {
        AnnotationMirror mirror = element.getAnnotationMirrors() // 返回当前元素的所有注解集合
                .stream()
                .filter(annotation -> Objects.equals(annotationName, annotation.getAnnotationType().toString()))
                .findFirst()
                .orElse(null);

        if (mirror == null)
            return "";

        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            if (name.equals(property)) {
                String value = entry.getValue().getValue().toString().replace("\"", "");
                if (value.isEmpty()) {
                    AnnotationValue defaultValue = entry.getKey().getDefaultValue();
                    if (defaultValue != null)
                        value = entry.getKey().getDefaultValue().getValue().toString().replace("\"", "");
                }
                if (!value.isEmpty())
                    return value;
            }


            //别名支持，针对@AliasFor
            for (String alias : aliasProperties)
                if (name.equals(alias)) {
                    String value = entry.getValue().getValue().toString().replace("\"", "");
                    if (value.isEmpty()) {
                        AnnotationValue defaultValue = entry.getKey().getDefaultValue();
                        if (defaultValue == null)
                            return "";
                        value = entry.getKey().getDefaultValue().getValue().toString().replace("\"", "");
                    }
                    return value;
                }
        }

        return "";
    }
    
    public static List<String> readAnnotationValues(Element element, String annotationName, String property, String... aliasProperties) {
        AnnotationMirror mirror = element.getAnnotationMirrors() // 返回当前元素的所有注解集合
                .stream()
                .filter(annotation -> Objects.equals(annotationName, annotation.getAnnotationType().toString()))
                .findFirst()
                .orElse(null);
        
        if (mirror == null)
            return Collections.emptyList();
        
        List<String> result = new ArrayList<>();
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            if (name.equals(property)) {
                String value = entry.getValue().getValue().toString().replace("\"", "");
                if (value.isEmpty()) {
                    AnnotationValue defaultValue = entry.getKey().getDefaultValue();
                    if (defaultValue != null)
                        value = entry.getKey().getDefaultValue().getValue().toString().replace("\"", "");
                }
                if (!value.isEmpty())
                    result.add(value);
            }
            
            
            //别名支持，针对@AliasFor
            for (String alias : aliasProperties)
                if (name.equals(alias)) {
                    String value = entry.getValue().getValue().toString().replace("\"", "");
                    if (value.isEmpty()) {
                        AnnotationValue defaultValue = entry.getKey().getDefaultValue();
                        if (defaultValue == null)
                            continue;
                        value = entry.getKey().getDefaultValue().getValue().toString().replace("\"", "");
                    }
                    result.add(value);
                }
        }
        
        return result;
    }


    public static List<Element> getKindElements(Element root, ElementKind kind) {
        if (root == null)
            return Collections.emptyList();
        return root.getEnclosedElements().stream()
                .filter(e -> e.getKind() == kind)
                .collect(Collectors.toList());
    }

}

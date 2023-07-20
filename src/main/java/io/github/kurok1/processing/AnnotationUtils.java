package io.github.kurok1.processing;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
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


    public static List<Element> getKindElements(Element root, ElementKind kind) {
        if (root == null)
            return Collections.emptyList();
        return root.getEnclosedElements().stream()
                .filter(e -> e.getKind() == kind)
                .collect(Collectors.toList());
    }

}

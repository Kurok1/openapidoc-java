package io.github.kurok1.processing;

import io.github.kurok1.processing.annotation.Ignored;
import io.github.kurok1.processing.annotation.SourceCode;
import io.github.kurok1.processing.model.RequestEntry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kurok1.processing.Constants.*;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class RequestContributor {


    private final String[] SUPPORTED_ANNOTATIONS = {
            ANNOTATION_REQUEST_MAPPINGS,
            ANNOTATION_GET_MAPPINGS,
            ANNOTATION_POST_MAPPINGS,
            ANNOTATION_PUT_MAPPINGS,
            ANNOTATION_DELETE_MAPPINGS
    };

    private ProcessingEnvironment environment;

    private final Map<String, RequestEntry> map = new ConcurrentHashMap<>();

    private String defaultProject;

    private String contextPath;

    private String ignoredAnnotation;

    public void initialize(ProcessingEnvironment environment) {
        this.environment = environment;
        this.defaultProject = System.getProperty(KEY_APPLICATION_NAME, "default-project");
        this.contextPath = System.getProperty(KEY_CONTEXT_PATH, "");
        this.ignoredAnnotation = System.getProperty(KEY_IGNORED_ANNOTATION, "");
    }


    public void processAnnotatedElement(Element element) {
        if (!(element instanceof TypeElement))
            return;
        //1.检查@RequestMapping
        String presentAnnotation = AnnotationUtils.determineAnnotationAnyPresent(element, SUPPORTED_ANNOTATIONS);

        String globalPath = AnnotationUtils.readAnnotationValue(element, presentAnnotation, "value", "name");
        String[] sourceCodesOnType = getSourceCode(element);


        //2.扫描每个方法, 包含父类方法，子类继承实现父类方法时，以子类方法为主
        TypeElement typeElement = (TypeElement) element;
        Collection<ExecutableElement> methodElements = MethodUtils.getMergedMethods(this.environment, typeElement);
        if (methodElements.isEmpty())
            return;//没有方法

        if (isIgnored(element)) {

            String uri = IGNORE_TOKEN_URI_PREFIX + defaultProject + '/' + contextPath + globalPath + IGNORE_TOKEN_URI_POSTFIX;
            for (String sourceCode : sourceCodesOnType)
                register(sourceCode, optimize(uri));

            return;
        }

        for (ExecutableElement method : methodElements) {
            if (method.getKind() != ElementKind.METHOD)
                continue;

            //1.检查@RequestMapping
            String methodAnnotation = AnnotationUtils.determineAnnotationAnyPresent(method, SUPPORTED_ANNOTATIONS);
            if (methodAnnotation.isEmpty())
                continue;

            String path = AnnotationUtils.readAnnotationValue(method, methodAnnotation, "value", "name");
            Set<String> sources = getSourceCode(element, method);
            if (isIgnored(method)) {
                String uri = IGNORE_TOKEN_URI_PREFIX + defaultProject + '/' + contextPath + globalPath + '/' + path;
                for (String sourceCode : sources)
                    register(sourceCode, optimize(uri));
            } else {
                String uri = '/' + defaultProject + '/' + contextPath + globalPath + '/' + path;
                for (String sourceCode : sources)
                    register(sourceCode, optimize(uri));
            }

        }

    }


    private boolean isIgnored(Element element) {
        if (this.ignoredAnnotation.isEmpty())
            return element.getAnnotation(Ignored.class) != null;
        else return AnnotationUtils.isAnnotationPresent(element, this.ignoredAnnotation);
    }

    private String[] getSourceCode(Element element) {
        SourceCode sourceCode = element.getAnnotation(SourceCode.class);
        if (sourceCode != null)
            return sourceCode.value();

        return new String[]{defaultProject};
    }

    private Set<String> getSourceCode(Element element, ExecutableElement method) {
        SourceCode sourceCodeOnType = element.getAnnotation(SourceCode.class);
        SourceCode sourceCodeOnMethod = method.getAnnotation(SourceCode.class);

        if (sourceCodeOnType == null && sourceCodeOnMethod == null)
            return Collections.singleton(defaultProject);

        Set<String> sources = new HashSet<>();
        if (sourceCodeOnType != null)
            for (String code : sourceCodeOnType.value())
                sources.add(code);

        if (sourceCodeOnMethod != null)
            for (String code : sourceCodeOnMethod.value())
                sources.add(code);

        if (sources.isEmpty())
            sources.add(defaultProject);

        return sources;
    }

    private void register(String sourceCode, String uri) {
        RequestEntry requestEntry = map.computeIfAbsent(sourceCode, RequestEntry::new);
        if (uri.endsWith("/"))
            uri = uri.substring(0, uri.length() - 1);
        requestEntry.addUri(uri);
    }

    public Collection<RequestEntry> getRequests() {
        return this.map.values();
    }

    private static String optimize(String uri) {
        while (uri.contains("//")) {
            uri = uri.replace("//", "/");
        }
        return uri;
    }

}

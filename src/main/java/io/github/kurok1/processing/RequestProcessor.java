package io.github.kurok1.processing;

import io.github.kurok1.processing.model.RequestEntry;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@SupportedAnnotationTypes(value = {Constants.ANNOTATION_CONTROLLER, Constants.ANNOTATION_REST_CONTROLLER})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RequestProcessor extends AbstractProcessor {

    private static final String DEFAULT_OUTPUT_FILE_PATH = "META-INF/openapi/request.json";
    private final RequestContributor contributor = new RequestContributor();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        readEnvProperties(processingEnv);
        contributor.initialize(processingEnv);
    }

    private void readEnvProperties(ProcessingEnvironment processingEnv) {
        try {
            FileObject fileObject = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/source.properties");
            Properties properties = new Properties();
            properties.load(fileObject.openInputStream());
            printMessage("环境变量读取: ");
            properties.forEach((key, value) -> {
                printMessage(key + "=" + value);
            });
            rewriteSystemProperty(properties, Constants.KEY_APPLICATION_NAME);
            rewriteSystemProperty(properties, Constants.KEY_CONTEXT_PATH);
            rewriteSystemProperty(properties, Constants.KEY_IGNORED_ANNOTATION);
            rewriteSystemProperty(properties, Constants.KEY_OUT_PUT_FILE_PATH);

        } catch (IOException ignored) {

        }

    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    private void rewriteSystemProperty(Properties properties, String key) {
        String value = properties.getProperty(key, "");
        if (!value.isEmpty())
            System.setProperty(key, value);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 第一个阶段：处理阶段
        // 获取所有的编译类
        if (!roundEnv.processingOver()) {
            printMessage("开始扫描uri");
        }
        roundEnv.getRootElements().stream().filter(this::isAnnotationPresent) // 过滤标注
                .forEach(this.contributor::processAnnotatedElement);     // 处理标注

        // 第二个阶段：完成阶段
        if (roundEnv.processingOver()) {
            // 输出到新生成的文件
            try {
                generateRequestMetadata();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private String loadOutputPath() {
        return System.getProperty(Constants.KEY_OUT_PUT_FILE_PATH, DEFAULT_OUTPUT_FILE_PATH);
    }

    private void generateRequestMetadata() throws IOException {
        Filer filer = processingEnv.getFiler();
        Collection<RequestEntry> requestEntries = this.contributor.getRequests();
        final String outputPath = loadOutputPath();
        FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", outputPath);
        try (OutputStream outputStream = fileObject.openOutputStream()) {
            StringBuilder builder = new StringBuilder();
            builder.append("{\n");
            if (!requestEntries.isEmpty()) {
                for (RequestEntry entry : requestEntries) {
                    String name = entry.getSourceCode();
                    builder.append("\t").append("\"").append(name).append("\": ");
                    builder.append(entry.uri2String());
                    builder.append(",\n");
                }
                builder.deleteCharAt(builder.length() - 2);
            }

            builder.append("}");
            String value = builder.toString();
            printMessage(value);
            outputStream.write(value.getBytes(StandardCharsets.UTF_8));
        }
        printMessage("扫描uri完成， 落地文件" + outputPath);
    }

    private boolean isAnnotationPresent(Element element) {
        String usingControllerAnnotation = AnnotationUtils.determineAnnotationAnyPresent(element, Constants.ANNOTATION_REST_CONTROLLER, Constants.ANNOTATION_CONTROLLER);
        return !usingControllerAnnotation.isEmpty();
    }
}

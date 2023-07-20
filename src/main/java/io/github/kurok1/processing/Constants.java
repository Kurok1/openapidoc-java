package io.github.kurok1.processing;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class Constants {

    public static final String ANNOTATION_REST_CONTROLLER = "org.springframework.web.bind.annotation.RestController";
    public static final String ANNOTATION_CONTROLLER = "org.springframework.stereotype.Controller";

    public static final String ANNOTATION_REQUEST_MAPPINGS = "org.springframework.web.bind.annotation.RequestMapping";
    public static final String ANNOTATION_GET_MAPPINGS = "org.springframework.web.bind.annotation.GetMapping";
    public static final String ANNOTATION_POST_MAPPINGS = "org.springframework.web.bind.annotation.PostMapping";
    public static final String ANNOTATION_PUT_MAPPINGS = "org.springframework.web.bind.annotation.PutMapping";
    public static final String ANNOTATION_DELETE_MAPPINGS = "org.springframework.web.bind.annotation.DeleteMapping";


    public static final String IGNORE_TOKEN_URI_PREFIX = "/$IgnoreToken$/";

    public static final String IGNORE_TOKEN_URI_POSTFIX = "/**";

    public static final String KEY_APPLICATION_NAME = "yunlian.project.name";
    public static final String KEY_CONTEXT_PATH = "yunlian.context.path";

    public static final String KEY_IGNORED_ANNOTATION = "yunlian.ignored.annotation";
    public static final String KEY_OUT_PUT_FILE_PATH = "yunlian.output.filePath";


    public static final String[] EMPTY_ARRAY_STRING = new String[]{};

}

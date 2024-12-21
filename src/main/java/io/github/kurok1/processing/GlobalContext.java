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

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class GlobalContext {
    
    public static final String CONFIG_FILE = "META-INF/open-api.properties";
    
    private static Types typeUtils;
    private static Elements elementUtils;
    
    private static final Map<String, String> properties = new ConcurrentHashMap<>();
    
    static {
        ClassLoader classLoader = GlobalContext.class.getClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources(CONFIG_FILE);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                Properties properties = new Properties();
                properties.load(resource.openStream());
                override(properties);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * property keys...
     */
    public static final String CONTEXT_PATH = "context.path";
    public static final String API_INFO_TITLE = "api.title";
    public static final String API_INFO_VERSION = "api.version";
    public static final String SERVER_LIST = "server.list";
    
    public static void override(Properties properties) {
        synchronized (GlobalContext.properties) {
            for (String key : properties.stringPropertyNames()) {
                GlobalContext.properties.put(key, properties.getProperty(key));
            }
        }
        
    }
    
    public static void setTypeUtils(Types typeUtils) {
        GlobalContext.typeUtils = typeUtils;
    }
    
    public static void setElementUtils(Elements elementUtils) {
        GlobalContext.elementUtils = elementUtils;
    }
    
    public static Types getTypeUtils() {
        return typeUtils;
    }
    
    public static Elements getElementUtils() {
        return elementUtils;
    }
    
    public static void setContextPath(String contextPath) {
        properties.put(CONTEXT_PATH, contextPath);
    }
    
    public static String getContextPath() {
        return properties.getOrDefault(CONTEXT_PATH, "");
    }
    
    public static void setProperty(String key, String value) {
        properties.put(key, value);
    }
    
    public static String getProperty(String key) {
        return properties.get(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }
    
    public static List<String> getPropertyAsList(String key) {
        String value = getProperty(key, "");
        String[] split = value.split(",");
        List<String> list = new ArrayList<>(split.length);
        
        for (String s : split) {
            list.add(s);
        }
        
        return list;
    }
    
    public static Boolean getPropertyAsBoolean(String key, boolean defaultValue) {
        String value = getProperty(key, "");
        return Objects.equals(value, "") ? defaultValue : Boolean.parseBoolean(value);
    }
}

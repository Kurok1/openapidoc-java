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

import io.github.kurok1.processing.builder.AbstractApiDefinitionContributor;
import io.github.kurok1.processing.builder.MethodPathInfoContributor;
import io.github.kurok1.processing.builder.PathVariableContributor;
import io.github.kurok1.processing.builder.RequestBodyContributor;
import io.github.kurok1.processing.builder.RequestParameterContributor;
import io.github.kurok1.processing.builder.ResponseContributor;
import io.github.kurok1.processing.model.ApiDefinitionBuilder;
import io.github.kurok1.processing.model.Tag;
import io.github.kurok1.processing.output.DocumentGenerator;
import io.github.kurok1.processing.output.Struct;
import io.github.kurok1.processing.output.StructWriter;
import io.github.kurok1.processing.registry.ApiDefinitionRegistry;
import io.github.kurok1.processing.registry.SchemaRegistry;
import io.github.kurok1.processing.registry.TagRegistry;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public abstract class AbstractOpenApiProcessor extends AbstractProcessor {
    
    private ProcessingEnvironment environment;
    
    private final List<AbstractApiDefinitionContributor> contributors = new ArrayList<>();
    
    private final SchemaRegistry schemaRegistry = new SchemaRegistry();
    private final TagRegistry tagRegistry = new TagRegistry();
    private final ApiDefinitionRegistry apiDefinitionRegistry = new ApiDefinitionRegistry();
    
    protected OpenApiContract openApiContract;
    
    private final List<DocumentGenerator> documentGenerators = new ArrayList<>();
    private final List<StructWriter> structWriters = new ArrayList<>();
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.environment = processingEnv;
        this.openApiContract = createOpenApiContract();
        
        initializeContributors();
        loadDocumentGenerators();
        loadStructWriters();
        
        GlobalContext.setElementUtils(processingEnv.getElementUtils());
        GlobalContext.setTypeUtils(processingEnv.getTypeUtils());
        
        Properties properties = System.getProperties();
        //环境变量覆盖
        GlobalContext.override(properties);
    }
    
    private void initializeContributors() {
        OpenApiContract contract = getOpenApiContract();
        
        contributors.add(new MethodPathInfoContributor(contract));
        contributors.add(new RequestParameterContributor(contract));
        contributors.add(new RequestBodyContributor(contract));
        contributors.add(new PathVariableContributor(contract));
        contributors.add(new ResponseContributor(contract));
        
        List<AbstractApiDefinitionContributor> additionalBuilders = loadAdditionalContributors(contract);
        if (additionalBuilders != null)
            this.contributors.addAll(additionalBuilders);
        
        for (AbstractApiDefinitionContributor contributor : contributors) {
            contributor.setSchemaRegistry(this.schemaRegistry);
        }
    }
    
    protected void loadDocumentGenerators() {
        ServiceLoader<DocumentGenerator> serviceLoader = ServiceLoader.load(DocumentGenerator.class);
        for (DocumentGenerator documentGenerator : serviceLoader) {
            documentGenerators.add(documentGenerator);
        }
    }
    
    protected void loadStructWriters() {
        ServiceLoader<StructWriter> serviceLoader = ServiceLoader.load(StructWriter.class);
        for (StructWriter structWriter : serviceLoader) {
            structWriters.add(structWriter);
        }
    }
    
    private void printMessage(String message) {
        System.out.println(message);
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 第一个阶段：处理阶段
        // 获取所有的编译类
        if (!roundEnv.processingOver()) {
            printMessage("开始扫描uri");
        }
        roundEnv.getRootElements().stream().filter(this::elementIsEligible) // 过滤标注
                .forEach(this::processAnnotatedElement);     // 处理标注
        
        // 第二个阶段：完成阶段
        if (roundEnv.processingOver()) {
            // 输出到新生成的文件
            try {
                generateDocument();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    
    public void processAnnotatedElement(Element element) {
        if (!(element instanceof TypeElement))
            return;
        
        //扫描每个方法, 包含父类方法，子类继承实现父类方法时，以子类方法为主
        TypeElement typeElement = (TypeElement) element;
        Tag tag = createTag(typeElement);
        this.tagRegistry.addTag(tag);
        Collection<ExecutableElement> methodElements = MethodUtils.getMergedMethods(this.environment, typeElement);
        if (methodElements.isEmpty())
            return;//没有方法
        
        OpenApiContract contract = getOpenApiContract();
        for (ExecutableElement method : methodElements) {
            if (!contract.support(typeElement, method))
                continue;
            ApiDefinitionBuilder definition = new ApiDefinitionBuilder();
            contribute(definition, typeElement, method);
            
            definition.addTag(tag.getName());
            
            this.apiDefinitionRegistry.addFromBuilder(definition);
        }
        
    }
    
    private void generateDocument() throws IOException {
        Filer filer = processingEnv.getFiler();
        for (StructWriter structWriter : structWriters) {
            if (!structWriter.enabled())
                continue;
            for (DocumentGenerator generator : documentGenerators) {
                if (!generator.enabled())
                    continue;
                final String outputPath = generator.getDocumentPath();
                FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", outputPath);
                try (OutputStream outputStream = fileObject.openOutputStream()) {
                    Struct struct = generator.write(tagRegistry, schemaRegistry, apiDefinitionRegistry);
                    structWriter.write(fileObject.getName(), struct, outputStream);
                }
            }
        }
    }
    
    protected abstract boolean elementIsEligible(Element element);
    protected abstract OpenApiContract createOpenApiContract();
    protected abstract List<AbstractApiDefinitionContributor> loadAdditionalContributors(OpenApiContract contract);
    
    public OpenApiContract getOpenApiContract() {
        if (this.openApiContract == null)
            throw new NullPointerException("openApiContract is null");
        return openApiContract;
    }
    
    protected ProcessingEnvironment getEnvironment() {
        return this.environment;
    }
    
    protected Tag createTag(TypeElement typeElement) {
        return new Tag(typeElement.getSimpleName().toString(), typeElement.getSimpleName().toString());
    }
    
    protected void contribute(ApiDefinitionBuilder definition, TypeElement classElement, ExecutableElement methodElement) {
        for (AbstractApiDefinitionContributor contributor : contributors) {
            contributor.contribute(definition, classElement, methodElement);
        }
    }
    
    public SchemaRegistry getSchemaRegistry() {
        return schemaRegistry;
    }
    
    public TagRegistry getTagRegistry() {
        return tagRegistry;
    }
    
    public ApiDefinitionRegistry getApiDefinitionRegistry() {
        return apiDefinitionRegistry;
    }
}

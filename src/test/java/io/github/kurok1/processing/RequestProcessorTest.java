package io.github.kurok1.processing;

import io.github.kurok1.processing.compiler.CustomCompiler;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class RequestProcessorTest {


    public static void main(String[] args) throws IOException {
        File sourceDirectory = new File(System.getProperty("user.dir"), "/src/test/java");
        File targetDirectory = getClassOutputDirectory();
        // 基于 Compiler
        CustomCompiler compiler = new CustomCompiler(sourceDirectory, targetDirectory);
        compiler.setProcessors(new RequestProcessor());
        compiler.compile(
                AnoController.class.getName(), MyController.class.getName(), SimpleController.class.getName()
        );
    }

    public static File getClassOutputDirectory() {
        File currentClassPath = new File(findClassPath());
        File targetDirectory = currentClassPath.getParentFile();
        File classOutputDirectory = new File(targetDirectory, "new-classes");
        // 生成类输出目录
        classOutputDirectory.mkdir();
        return classOutputDirectory;
    }

    public static String findClassPath() {
        String classPath = System.getProperty("java.class.path");
        return Stream.of(classPath.split(File.pathSeparator))
                .map(File::new)
                .filter(File::isDirectory)
                .filter(File::canRead)
                .filter(File::canWrite)
                .map(File::getAbsolutePath)
                .findFirst()
                .orElse(null);
    }

}
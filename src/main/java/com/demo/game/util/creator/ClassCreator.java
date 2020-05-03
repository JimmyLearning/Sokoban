package com.demo.game.util.creator;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Jimmy
 * @version EnumCreator.java v1.0, 2020/4/25
 */
public class ClassCreator {

    public static void buildJava(String fileName) throws IOException {
        Map<String, String> attrMap = new HashMap<>();
        Properties properties = new Properties();
        properties.load(new FileReader("template" + File.separator + "java.properties"));
        properties.forEach((k, v) -> attrMap.put(k.toString(), v.toString()));
        javaBuilder("class", fileName, ".java", attrMap);
    }

    public static void buildEnum(String fileName) throws IOException {
        Map<String, String> attrMap = new HashMap<>();
        Properties properties = new Properties();
        properties.load(new FileReader("template" + File.separator + "enum.properties"));
        properties.forEach((k, v) -> {
            if (StringUtils.isNotBlank(k.toString()) && StringUtils.isNotBlank(v.toString())) {
                attrMap.put(k.toString(), v.toString());
            }
        });
        // TODO
        enumBuilder("enum", fileName, ".java", attrMap);
    }

    public static void javaBuilder(String clazz, String fileName, String suffix, Map<String, String> attrMap) throws IOException {
        System.out.println("==================Build Start ====================");
        long startTime = System.currentTimeMillis();
        File templateFile = getPath("template");
        // File frame
        File fileTemplate = getFile(templateFile, "FileTemplate");
        String fileStr = readFile(fileTemplate);
        String classReplacedStr = StringUtils.replace(fileStr, "{class}", clazz);
        String fileReplacedStr = StringUtils.replace(classReplacedStr, "{file}", fileName);

        // Attribute frame
        File attributeTemplate = getFile(templateFile, "AttributeTemplate");
        String attrStr = readFile(attributeTemplate);
        // GetSet frame
        File gsTemplate = getFile(templateFile, "GetSetTemplate");
        String gsStr = readFile(gsTemplate);
        StringBuilder attrResultBuilder = new StringBuilder();
        StringBuilder gsResultBuilder = new StringBuilder();
        attrMap.forEach((k, v) -> {
            String annotateStr = StringUtils.split(v, ":")[1];
            v = StringUtils.split(v, ":")[0];
            String nameReplaced = StringUtils.replace(attrStr, "{name}", k);
            String nameReplaced2 = StringUtils.replace(gsStr, "{name}", k);
            String nameReplaced3 = StringUtils.replace(nameReplaced2, "{Name}", StringUtils.capitalize(k));
            String typeReplaced = StringUtils.replace(nameReplaced, "{type}", v);
            String typeReplaced2 = StringUtils.replace(nameReplaced3, "{type}", v);
            String annotateReplaced = StringUtils.replace(typeReplaced, "{annotate}", annotateStr);
            attrResultBuilder.append(annotateReplaced);
            gsResultBuilder.append(typeReplaced2);
        });
        attrResultBuilder.append(System.getProperty("line.separator"));
        attrResultBuilder.append(gsResultBuilder);

        String resultStr = StringUtils.replace(fileReplacedStr, "{body}", attrResultBuilder.toString());

        File resultPath = new File("template" + File.separator + "result" + File.separator + fileName + suffix);
        if (!resultPath.getParentFile().exists()) {
            resultPath.getParentFile().mkdirs();
        }
        writeFile(resultPath, resultStr);
        long endTime = System.currentTimeMillis();
        System.out.println("==================Build Cost " + (endTime - startTime) + "ms ================");
        System.out.println("==================Build Success===================");
    }

    public static void enumBuilder(String clazz, String fileName, String suffix, Map<String, String> attrMap) throws IOException {
        System.out.println("==================Build Start ====================");
        long startTime = System.currentTimeMillis();
        File templateFile = getPath("template");
        // File frame
        File fileTemplate = getFile(templateFile, "FileTemplate");
        String fileStr = readFile(fileTemplate);
        String classReplacedStr = StringUtils.replace(fileStr, "{class}", clazz);
        String fileReplacedStr = StringUtils.replace(classReplacedStr, "{file}", fileName);

        // Attribute frame
        File attributeTemplate = getFile(templateFile, "AttributeTemplate");
        String attrStr = readFile(attributeTemplate);
        // GetSet frame
        File gsTemplate = getFile(templateFile, "GetSetTemplate");
        String gsStr = readFile(gsTemplate);
        StringBuilder attrResultBuilder = new StringBuilder();
        StringBuilder gsResultBuilder = new StringBuilder();
        attrMap.forEach((k, v) -> {
            String annotateStr = v.split(":")[1];
            v = StringUtils.split(v, ":")[0];
            String nameReplaced = StringUtils.replace(attrStr, "{name}", k);
            String nameReplaced2 = StringUtils.replace(gsStr, "{name}", k);
            String nameReplaced3 = StringUtils.replace(nameReplaced2, "{Name}", StringUtils.capitalize(k));
            String typeReplaced = StringUtils.replace(nameReplaced, "{type}", v);
            String typeReplaced2 = StringUtils.replace(nameReplaced3, "{type}", v);
            String annotateReplaced = StringUtils.replace(typeReplaced, "{annotate}", annotateStr);
            attrResultBuilder.append(annotateReplaced);
            gsResultBuilder.append(typeReplaced2);
        });
        attrResultBuilder.append(System.getProperty("line.separator"));
        if (StringUtils.isNotBlank(gsResultBuilder.toString())) {
            StringBuilder enumBuilder = new StringBuilder();
            attrMap.forEach((k, v) -> {
//                String annotateStr = v.split(":")[1];
//                v = StringUtils.split(v, ":")[0];
//                String enumName = StringUtils.upperCase(v);
//                enumBuilder.append(enumName).append(",");
            });
            attrResultBuilder.append(new StringBuilder("//TODO cons").append(System.getProperty("line.separator")).append(gsResultBuilder));
        } else {
            attrResultBuilder.append(gsResultBuilder);
        }

        String resultStr = StringUtils.replace(fileReplacedStr, "{body}", attrResultBuilder.toString());

        File resultPath = new File("template" + File.separator + "result" + File.separator + fileName + suffix);
        if (!resultPath.getParentFile().exists()) {
            resultPath.getParentFile().mkdirs();
        }
        writeFile(resultPath, resultStr);
        long endTime = System.currentTimeMillis();
        System.out.println("==================Build Cost " + (endTime - startTime) + "ms ================");
        System.out.println("==================Build Success===================");
    }

    private static void writeFile(File t, String content) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(t));
        bw.write(new String(content.getBytes(), StandardCharsets.UTF_8));
        bw.flush();
        bw.close();
    }

    private static String readFile(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.getProperty("line.separator"));
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }

    private static File getFile(File path, String fileName) {
        return new File(path.getAbsolutePath() + File.separator + fileName);
    }

    private static File getPath(String path) {
        return new File(new File("").getAbsolutePath() + File.separator + path);
    }
}

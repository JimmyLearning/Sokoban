package com.demo.game.util.creator;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy
 * @version EnumCreator.java v1.0, 2020/4/25
 */
public class EnumCreator {
    private final String clazz = "enum";
    private String fileName = "MyEnum";
    private List<String> enumList = Lists.newArrayList("A","B","C");

    public static void main(String[] args) throws IOException {
        File templateFile = getPath("template");
        File fileTemplate = getFile(templateFile, "FileTemplate");
        String fileStr = readFile(fileTemplate);
        String classReplacedStr = StringUtils.replace(fileStr, "{class}", "class");
        String fileReplacedStr = StringUtils.replace(classReplacedStr, "{file}", "File");
        System.err.println(fileReplacedStr);

        File attributeTemplate = getFile(templateFile, "AttributeTemplate");
        String attrStr = readFile(attributeTemplate);
        File gsTemplate = getFile(templateFile, "GetSetTemplate");
        String gsStr = readFile(gsTemplate);
        Map<String, String> attrMap = new HashMap<>();
        attrMap.put("code", "String");
        attrMap.put("memo", "String");
        StringBuilder attrResultBuilder = new StringBuilder();
        StringBuilder gsResultBuilder = new StringBuilder();
        attrMap.forEach((k, v) -> {
            String nameReplaced = StringUtils.replace(attrStr, "{name}", k);
            String nameReplaced2 = StringUtils.replace(gsStr, "{name}", k);
            String nameReplaced3 = StringUtils.replace(gsStr, "{Name}", StringUtils.in);
            String typeReplaced = StringUtils.replace(nameReplaced, "{type}", v);
            String typeReplaced2 = StringUtils.replace(nameReplaced3, "{type}", v);
            attrResultBuilder.append(typeReplaced);
            gsResultBuilder.append(typeReplaced2);
        });
        System.err.println(attrResultBuilder.toString());
        System.err.println(gsResultBuilder.toString());


    }

    private static String readFile(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while(line != null){
            sb.append(line);
            sb.append(System.getProperty("line.separator"));
            line = br.readLine();
        }
        return sb.toString();
    }

    private static File getFile(File path, String fileName){
        return new File(path.getAbsolutePath() + File.separator + fileName);
    }

    private static File getPath(String path){
        return new File(new File("").getAbsolutePath() + File.separator + path);
    }
}

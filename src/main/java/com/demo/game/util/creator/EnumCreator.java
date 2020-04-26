package com.demo.game.util.creator;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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

        System.err.println(attrStr);
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

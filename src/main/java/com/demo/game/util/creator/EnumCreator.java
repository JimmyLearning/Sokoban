package com.demo.game.util.creator;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

/**
 * @author Jimmy
 * @version EnumCreator.java v1.0, 2020/4/25
 */
public class EnumCreator {
    private final String clazz = "enum";
    private String fileName = "MyEnum";
    private List<String> enumList = Lists.newArrayList("A","B","C");

    public static void main(String[] args) {
        File templateFile = getPath("template");
        File fileTemplate = getFile(templateFile, "FileTemplate");
    }

    private static File getFile(File path, String fileName){
        return new File(path.getAbsolutePath() + File.separator + fileName);
    }

    private static File getPath(String path){
        return new File(new File("").getAbsolutePath() + File.separator + path);
    }
}

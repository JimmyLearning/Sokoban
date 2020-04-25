package com.demo.game.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DuplicatePom {

    private static final Map<String, String> DEP_MAP = new HashMap<>();

    public static void main(String[] args) {
        DuplicatePom.checkDuplicate();
    }

    private static void checkDuplicate(){
        try {
            Document document = parse("D:\\pom.xml");
            Element rootElement = document.getRootElement();
            Element dependencies = rootElement.element("dependencies");
            for (Iterator it = dependencies.elementIterator("dependency"); it.hasNext(); ) {
                Element e = (Element)it.next();
                String groupId = e.element("groupId").getText();
                String artifactId = e.element("artifactId").getText();
                if(DEP_MAP.containsKey(groupId)){
                    String value = DEP_MAP.get(groupId);
                    if(value.equals(artifactId)){
                        printDuplicate("dependency", groupId, artifactId);
                    }
                }else{
                    DEP_MAP.put(groupId, artifactId);
                }
            }
        }catch (Exception e){
            System.err.println("error is " + e);
        }
    }

    private static Document parse(String path) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(new File(path));
    }

    private static void printDuplicate(String what, String groupId, String artifactId){
        System.err.println("=======duplicate " + what + "========");
        System.err.println("groupId = " + groupId);
        System.err.println("artifactId = " + artifactId);
        System.err.println("===================================");
    }
}
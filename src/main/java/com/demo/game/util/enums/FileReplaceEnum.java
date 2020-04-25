package com.demo.game.util.enums;

/**
 * @author Jimmy
 * @version FileReplaceEnum.java v1.0, 2020/4/25
 */
public enum FileReplaceEnum {
    FILE("file", "File"),
    CLASS("class", ""),
    BODY("body", "");

    private String small;
    private String head;

    FileReplaceEnum(String small, String head) {
        this.small = small;
        this.head = head;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}

package com.demo.game.util.enums;

/**
 * @author Jimmy
 * @version AttributeReplaceEnum.java v1.0, 2020/4/25
 */
public enum AttributeReplaceEnum {
    TYPE("type", "Type"),
    Name("name", "Name");

    private String small;
    private String head;

    AttributeReplaceEnum(String small, String head) {
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

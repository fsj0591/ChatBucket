package com.cb.chat.model.completion.chat;

/**
 * 消息角色枚举类
 */
public enum ChatMessageRole {

    SYSTEM("system"),

    USER("user"),

    ASSISTANT("assistant");

    private final String value;

    ChatMessageRole(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

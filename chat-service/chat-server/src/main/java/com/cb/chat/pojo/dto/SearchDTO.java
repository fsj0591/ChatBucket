package com.cb.chat.pojo.dto;

import lombok.Data;

@Data
public class SearchDTO {

    /**
     * 关键字
     */
    String keyword;

    /**
     * 用户id
     */
    Integer userId;

}

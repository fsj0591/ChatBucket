package com.cb.chat.service;

import com.cb.chat.pojo.dto.SearchDTO;

import java.util.List;

public interface HistorySearchService {

    /**
     * 添加搜索记录
     *
     * @param searchDTO 搜索dto
     * @return boolean
     */
    boolean addSearchRecord(SearchDTO searchDTO);

    /**
     * 删除搜索记录
     *
     * @param searchDTO 搜索dto
     * @return boolean
     */
    boolean deleteSearchRecord(SearchDTO searchDTO);

    /**
     * 获得搜索历史记录
     *
     * @param userId 用户id
     * @return {@link List}<{@link SearchDTO}>
     */
    List<String> getHistorySearchRecord(Integer userId);
}

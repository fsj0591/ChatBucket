package com.cb.chat.controller;

import com.cb.chat.exception.ChatException;
import com.cb.chat.pojo.dto.SearchDTO;
import com.cb.chat.service.HistorySearchService;
import com.cb.common.core.result.RestResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("chat/hs")
public class HistorySearchController {

    @Resource
    HistorySearchService historySearchService;


    /**
     * 添加搜索记录
     *
     * @param searchDTO 搜索dto
     * @return {@link RestResult}<{@link Boolean}>
     */
    @PostMapping("add")
    public RestResult<Boolean> addSearchRecord(SearchDTO searchDTO){
        if(searchDTO.getUserId()==null){
            throw new ChatException("获取用户信息异常");
        }
        if(historySearchService.addSearchRecord(searchDTO)){
            return RestResult.ok(true, "添加成功");
        }
        return RestResult.fail("添加失败");
    }


    /**
     * 删除搜索记录
     *
     * @param searchDTO 搜索dto
     * @return {@link RestResult}<{@link Boolean}>
     */
    @PostMapping("delete")
    public RestResult<Boolean> deleteSearchRecord(SearchDTO searchDTO){
        if(searchDTO.getUserId()==null){
            throw new ChatException("获取用户信息异常");
        }
        if(historySearchService.deleteSearchRecord(searchDTO)){
            return RestResult.ok(true, "删除成功");
        }
        return RestResult.fail("删除失败");
    }

    /**
     * 获得搜索历史记录
     *
     * @param userId 用户id
     * @return {@link RestResult}<{@link List}<{@link SearchDTO}>>
     */
    @GetMapping
    public RestResult<List<String>> getHistorySearchRecord(@RequestHeader Integer userId){
        if(userId==null){
            throw new ChatException("获取用户信息异常");
        }
        return RestResult.ok(historySearchService.getHistorySearchRecord(userId));
    }


}

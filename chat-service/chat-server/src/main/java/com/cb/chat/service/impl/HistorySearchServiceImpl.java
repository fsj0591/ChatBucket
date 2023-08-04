package com.cb.chat.service.impl;

import com.cb.chat.pojo.dto.SearchDTO;
import com.cb.chat.service.HistorySearchService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class HistorySearchServiceImpl implements HistorySearchService {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean addSearchRecord(SearchDTO searchDTO) {
        //最大记录数
        long top = 10;
        //获得redis中数据的分数（若存在则有记录，不存在则为空）
        Double score = redisTemplate.opsForZSet().score(searchDTO.getUserId().toString(), searchDTO.getKeyword());
        if(score != null){
            //存在记录，删除记录
            redisTemplate.opsForZSet().remove(searchDTO.getUserId().toString(), searchDTO.getKeyword());
        }
        //添加搜索记录
        redisTemplate.opsForZSet().add(searchDTO.getUserId().toString(), searchDTO.getKeyword(), System.currentTimeMillis());
        //查询总记录数
        Long num = redisTemplate.opsForZSet().zCard(searchDTO.getUserId().toString());
        if(num>top){
            redisTemplate.opsForZSet().removeRange(searchDTO.getUserId().toString(), 0, num-top-1);
        }
        return true;
    }

    @Override
    public boolean deleteSearchRecord(SearchDTO searchDTO) {
        Long row = redisTemplate.opsForZSet().remove(searchDTO.getUserId().toString(), searchDTO.getKeyword());
        return row>0;
    }

    @Override
    public List<String> getHistorySearchRecord(Integer userId) {
        List<String> searchList = new ArrayList<>();
        long start = 0;
        long end = 9;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(userId.toString(), start, end);
        for (ZSetOperations.TypedTuple<String> next : typedTuples) {
            if (next.getValue() != null) {
                searchList.add(next.getValue());
            }
        }
        return searchList;
    }

}

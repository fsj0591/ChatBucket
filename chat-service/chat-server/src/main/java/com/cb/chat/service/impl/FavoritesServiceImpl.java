package com.cb.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cb.chat.exception.ChatException;
import com.cb.chat.mapper.FavoritesMapper;
import com.cb.chat.mapper.RecordMapper;
import com.cb.chat.pojo.Favorites;
import com.cb.chat.pojo.Record;
import com.cb.chat.service.FavoritesService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites> implements FavoritesService {

    @Resource
    FavoritesMapper favoritesMapper;

    @Resource
    RecordMapper recordMapper;

    @Override
    public boolean addFavorites(Favorites favorites) {
        if(ObjectUtils.isEmpty(favorites)){
            throw new ChatException("参数异常");
        }
        return favoritesMapper.insert(favorites)>0;
    }

    @Override
    public boolean deleteFavorites(Integer userId, Integer id) {
        LambdaQueryWrapper<Favorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorites::getUserId, userId)
                .eq(Favorites::getId, id);
        Favorites favorites = favoritesMapper.selectOne(queryWrapper);
        if(favorites==null){
            throw new ChatException("该收藏夹不存在");
        }
        return favoritesMapper.delete(queryWrapper)>0;
    }

    @Override
    public List<Favorites> getFavoritesList(Integer userId) {
        LambdaQueryWrapper<Favorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorites::getUserId, userId);
        return favoritesMapper.selectList(queryWrapper);
    }

    @Override
    public List<Record> getFavoritesContent(Integer id) {
        LambdaQueryWrapper<Favorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorites::getId, id);
        Favorites favorites = favoritesMapper.selectOne(queryWrapper);
        if(favorites==null){
            throw new ChatException("该收藏夹不存在");
        }
        LambdaQueryWrapper<Record> recordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recordLambdaQueryWrapper.eq(Record::getFavorite, id);
        return recordMapper.selectList(recordLambdaQueryWrapper);
    }

    @Override
    public boolean updateFavorites(Favorites favorites) {
        if(favorites.getFavoritename()==null || favorites.getId()==null){
            throw new ChatException("参数异常");
        }
        return favoritesMapper.updateById(favorites)>0;
    }
}

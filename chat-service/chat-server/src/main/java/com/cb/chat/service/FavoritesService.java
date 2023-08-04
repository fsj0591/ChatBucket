package com.cb.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cb.chat.pojo.Favorites;
import com.cb.chat.pojo.Record;

import java.util.List;

public interface FavoritesService extends IService<Favorites> {
    boolean addFavorites(Favorites favorites);

    boolean deleteFavorites(Integer userId, Integer id);

    List<Favorites> getFavoritesList(Integer userId);

    List<Record> getFavoritesContent(Integer id);

    boolean updateFavorites(Favorites favorites);
}

package com.cb.chat.controller;

import com.cb.chat.exception.ChatException;
import com.cb.chat.pojo.Favorites;
import com.cb.chat.pojo.Record;
import com.cb.chat.service.FavoritesService;
import com.cb.common.core.result.RestResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("chat/favorite")
public class FavoritesController {

    @Resource
    FavoritesService favoritesService;

    /**
     * 添加收藏夹
     *
     * @param favorites 收藏夹
     * @return {@link RestResult}<{@link Boolean}>
     */
    @PostMapping("add")
    public RestResult<Boolean> addFavorites(Favorites favorites){
        if(favoritesService.addFavorites(favorites)){
            return RestResult.ok(true, "添加成功");
        }
        return RestResult.fail("添加失败");
    }

    /**
     * 删除收藏夹
     *
     * @param userId 用户id
     * @param id     id
     * @return {@link RestResult}<{@link Boolean}>
     */
    @PostMapping("delete")
    public RestResult<Boolean> deleteFavorites(@RequestHeader Integer userId, Integer id){
        if(userId==null){
            throw new ChatException("获取用户信息异常");
        }
        if(favoritesService.deleteFavorites(userId, id)){
            return RestResult.ok(true, "删除成功");
        }
        return RestResult.fail("删除失败");
    }

    /**
     * 获得收藏夹列表
     *
     * @param userId 用户id
     * @return {@link RestResult}<{@link List}<{@link Favorites}>>
     */
    @GetMapping("list")
    public RestResult<List<Favorites>> getFavoritesList(@RequestHeader Integer userId){
        if(userId==null){
            throw new ChatException("获取用户信息异常");
        }
        return RestResult.ok(favoritesService.getFavoritesList(userId));
    }

    /**
     * 获得某个收藏夹底下的内容
     *
     * @param id id
     * @return {@link RestResult}<{@link List}<{@link Record}>>
     */
    @GetMapping("one")
    public RestResult<List<Record>> getFavoritesContent(Integer id){
        return RestResult.ok(favoritesService.getFavoritesContent(id));
    }

    /**
     * 修改收藏夹名称
     *
     * @param favorites 最喜欢
     * @return {@link RestResult}<{@link Boolean}>
     */
    @PostMapping("update")
    public RestResult<Boolean> updateFavorites(Favorites favorites){
        if(favoritesService.updateFavorites(favorites)){
            return RestResult.ok(true, "修改成功");
        }
        return RestResult.fail("修改失败");
    }

}

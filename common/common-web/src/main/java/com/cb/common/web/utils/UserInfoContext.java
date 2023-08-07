package com.cb.common.web.utils;

import com.cb.common.web.pojo.UserInfo;

public class UserInfoContext {

    private static final ThreadLocal<UserInfo> userInfo = new ThreadLocal();

    public static UserInfo getUser() {
        return userInfo.get();
    }

    public static void setUser(UserInfo user) {
        userInfo.set(user);
    }

    public static void remove(){
        userInfo.remove();
    }

}

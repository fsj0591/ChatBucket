package com.cb.user.util;

import java.util.Random;

public class SaltUtils {
    public static String getSalt(){
        //生成1-10的随机数
        Random random= new  java.util.Random();
        int n = random.nextInt(10);
        n=n+1;
        char[] chars = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
                "1234567890!@#$%^&*()_+").toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++){
            //Random().nextInt()返回值为[0,n)
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }
}


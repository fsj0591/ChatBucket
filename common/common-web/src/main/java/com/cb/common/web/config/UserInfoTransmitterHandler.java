package com.cb.common.web.config;

import com.cb.common.web.filter.TransmitUserInfoFilter;
import com.cb.common.web.intercepter.TransmitUserInfoFeighClientIntercepter;
import org.springframework.context.annotation.Bean;

public class UserInfoTransmitterHandler {

    @Bean
    public TransmitUserInfoFeighClientIntercepter transmitUserInfoFeighClientIntercepter(){
        return new TransmitUserInfoFeighClientIntercepter();
    }

    @Bean
    public TransmitUserInfoFilter transmitUserInfoFilter(){
        return new TransmitUserInfoFilter();
    }

}

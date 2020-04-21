package com.ibm.esw.leo.spring.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {

    private static final String SIGNING_KEY = "JWT_SIGNING_KEY_123XXX";

    //token存储策略 JWT模式
    @Bean
    public TokenStore tokenStore() {
        //jwt方式生成令牌
        return new JwtTokenStore(accessTokenConvertor());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConvertor(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }

}

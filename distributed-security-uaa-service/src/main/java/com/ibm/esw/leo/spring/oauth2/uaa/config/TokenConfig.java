package com.ibm.esw.leo.spring.oauth2.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class TokenConfig {

    //token存储策略
    @Bean
    public TokenStore tokenStore() {
        //memory方式生成非JWT令牌
        return new InMemoryTokenStore();
    }
}

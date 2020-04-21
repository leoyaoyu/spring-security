package com.ibm.esw.leo.spring.oauth2.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    /**
     * 1. 配置客户端详细信息服务
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("c1") //client id
                .secret(new BCryptPasswordEncoder().encode("secret")) // client password
                .resourceIds("res1") //resource list
                .authorizedGrantTypes(
                        "authorization_code",
                        "implicit",
                        "password",
                        "client_credentials",
                        "refresh_token") //oauth authentication 5 type
                .scopes("all") //允许授权的范围,all, read
                .autoApprove(true) //如果是授权码模式，false 跳转到授权页面 true不跳转，直接发令牌
                .redirectUris("http://www.baidu.com/")  //回调网址
        .and().withClient("c2") //client id
                .secret(new BCryptPasswordEncoder().encode("secret")) // client password
                .resourceIds("res2") //resource list
                .authorizedGrantTypes(
                        "authorization_code",
                        "implicit",
                        "password",
                        "client_credentials",
                        "refresh_token") //oauth authentication 5 type
                .scopes("all") //允许授权的范围,all, read
                .autoApprove(false) //如果是授权码模式，false 跳转到授权页面 true不跳转，直接发令牌
                .redirectUris("http://www.baidu.com/")  //回调网址
        ;
    }

    /*
    @Autowired
    PasswordEncoder passwordEncoder;

    //将客户端信息存储到数据库
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    } */

    /**
     * 2. token访问服务配置
     */
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Bean
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);  //客户端信息服务
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore);              //token存储策略
        service.setAccessTokenValiditySeconds(7200);    //access token valid in 2 hours
        service.setRefreshTokenValiditySeconds(259200); //refresh token default valid in 3 days
        return service;
    }

    /**
     * 3. 暴露token访问endpoint
     */
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    //授权码模式的授权码如何存取，暂时采用内存方式
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }
    //授权码模式的授权码如何存取，暂时采用内存方式
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
//        return new JdbcAuthorizationCodeServices(dataSource);
//    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints){
        endpoints
                .authenticationManager(authenticationManager) //认证：密码模式需要的
                .authorizationCodeServices(authorizationCodeServices) //授权：授权码模式需要的
                .tokenServices(tokenServices())//2. token访问服务配置
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);//endpoint允许POST提交
    }

    //token访问端点的安全策略
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll()")      //资源服务器访问 /oauth/token_key公开 获取token的秘钥
                .checkTokenAccess("permitAll()")    //监测token /oauth/check_token公开 验证token的endpoint
                .allowFormAuthenticationForClients() //允许表单认证
        ;
    }

}

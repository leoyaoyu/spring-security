package com.ibm.esw.leo.spring.oauth2.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.ibm.esw.leo.spring.oauth2.gateway.common.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.*;

public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        /**
         * 1.获取令牌内容
         */
        RequestContext ctx = RequestContext.getCurrentContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 如果不是OAuth2的格式就不作操作了，无token访问网关内资源的情况，目前仅有uua服务直接暴露
        if(!(authentication instanceof OAuth2Authentication)) {
            return null;
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();


        //1. 获取当前用户的身份信息
        String principal = userAuthentication.getName();

        //2. 获取当前用户的权限信息
        List<String> authorities = new ArrayList<>();
        userAuthentication.getAuthorities().stream().forEach( c ->
                authorities.add(((GrantedAuthority) c).getAuthority()));

        //jwt中的其他信息
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String, Object> jsonToken = new HashMap<>(requestParameters);
        if(userAuthentication != null) {
            jsonToken.put("principal", principal);
            jsonToken.put("authorities", authorities);
        }


        //3. 把身份、权限信息放到HTTP header中
        ctx.addZuulRequestHeader("json-token", EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));

        //4. 转发给微服务

        return null;
    }
}

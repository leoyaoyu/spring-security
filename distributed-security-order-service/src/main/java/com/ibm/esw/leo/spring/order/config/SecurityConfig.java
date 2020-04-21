package com.ibm.esw.leo.spring.order.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Slf4j
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                //                .antMatchers("/user/common").hasAnyAuthority("p1","p2")
                //                .antMatchers("/user/admin").hasAuthority("p1")
                //                .antMatchers("/","index","/login","/login-error","/401","/css/**","/js/**").permitAll()
                //                .antMatchers("/user/authentication").permitAll()
                .anyRequest().permitAll()
        ;

    }
}

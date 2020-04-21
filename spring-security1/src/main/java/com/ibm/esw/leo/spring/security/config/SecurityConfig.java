package com.ibm.esw.leo.spring.security.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //定义用户与权限的关系(查询用信息)
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
                //.password("admin123")
                .password("$2a$10$BjJ6ysPYdeo97xUzcm4SOeXcY1ZsvE74/UiEO2yt1Mm4y2cw8rQAu")
                .authorities("p1").build());
        manager.createUser(User.withUsername("user")
                //.password("user123")
                .password("$2a$10$1bCBfbxNprQ3wqnL8dgrLu1bba619p7JnhCY2052pc.vBgZ0qDYZS")
                .authorities("p2").build());
        return manager;
    }

    //密码解码器
    @Bean
    public PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    //定义资源与权限的关系!!最重要的是安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/common").hasAnyAuthority("p1","p2")
                .antMatchers("/user/admin").hasAuthority("p1")
                .antMatchers("/","index","/login","/login-error","/401","/css/**","/js/**").permitAll()
                .antMatchers("/user/authentication").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage( "/login" ).failureUrl( "/login-error" )
                .defaultSuccessUrl("/user/common",true)
                .and()
                .exceptionHandling().accessDeniedPage( "/401" )
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .logout()
                //.logoutSuccessUrl( "/" )
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.info("succeed logged out, clean system cache");
                    }
                }).addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response,
                                       Authentication authentication) {
                        log.info("logged out, clean system cache");
                    }
                })
        ;

    }


}

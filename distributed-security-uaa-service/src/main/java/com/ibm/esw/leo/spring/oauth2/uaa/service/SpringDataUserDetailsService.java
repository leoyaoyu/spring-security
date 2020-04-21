package com.ibm.esw.leo.spring.oauth2.uaa.service;

import com.alibaba.fastjson.JSON;
import com.ibm.esw.leo.spring.oauth2.uaa.dao.UserDao;
import com.ibm.esw.leo.spring.oauth2.uaa.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
public class SpringDataUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    //根据 账号查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //将来连接数据库根据账号查询用户信息
        User user = userDao.getUserByUsername(username);
        if(user == null){
            //如果用户查不到，返回null，由provider来抛出异常
            return null;
        }
        //根据用户的id查询用户的权限
        List<String> permissions = userDao.findPermissionsByUserId(user.getId());
        //将permissions转成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        //将userDto转成json
        String principal = JSON.toJSONString(user);
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.withUsername(principal).password(user.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}

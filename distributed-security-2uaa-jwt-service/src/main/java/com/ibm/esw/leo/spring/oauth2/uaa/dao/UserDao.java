package com.ibm.esw.leo.spring.oauth2.uaa.dao;

import com.ibm.esw.leo.spring.oauth2.uaa.domain.entity.Permission;
import com.ibm.esw.leo.spring.oauth2.uaa.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 **/
@Repository
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    //根据账号查询用户信息
    public User getUserByUsername(String username){
        String sql = "select id,username,password,fullname,mobile from t_user where username = ?";
        //连接数据库查询用户
        List<User> list = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(User.class));
        if(list !=null && list.size()==1){
            return list.get(0);
        }
        return null;
    }

    //根据用户id查询用户权限
    public List<String> findPermissionsByUserId(String userId){
        String sql = "SELECT * FROM t_permission WHERE permission_id IN(\n" +
                "\n" +
                "SELECT permission_id FROM t_role_permission WHERE role_id IN(\n" +
                "  SELECT role_id FROM t_user_role WHERE user_id = ? \n" +
                ")\n" +
                ")\n";

        List<Permission> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(Permission.class));
        List<String> permissions = new ArrayList<>();
        list.forEach(c -> permissions.add(c.getCode()));
        return permissions;
    }
}

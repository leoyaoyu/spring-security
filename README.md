# spring-security学习工程

## 学习顺序
* spring security1
* spring security2
* *spring security3 mybatis
* distributed-security-uaa-service and distributed-security-order-service
* distributed-security-2uaa-service and distributed-security-2order-service
* distributed-security-3order-service | distributed-security-eureka |distributed-security-zuul
* *distributed-security-gateway

---
## 简要介绍
### Spring security 1
最基本的Spring security实现，重点关注WebSecurityConfigurerAdapter的继承类

其中两个方法：
* UserDetailsService 定义用户与权限的关系
* configure 定义资源与权限的关系

实验中定义了
1. 两个用户
* user/user123
* admin/admin123

2. 两个权限
* p1 admin登录后可以获得p1权限
* p2 user登录后可以获得p2权限

3. 两个资源页面
* /user/common 两个p1/p2都可以访问，
* /user/admin 只有p1可以访问

---
### Spring security 2
权限与资源的绑定关系从configure方法中迁移到了Controller的具体APi中

```java
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class MainController {

    @GetMapping("/user/common")
    @PreAuthorize("hasAnyAuthority('p1', 'p2')")
    public String common() {
        return "user/common";
    }

    @GetMapping("/user/admin")
    @PreAuthorize("hasAuthority('p1')")
    public String admin() {
        return "user/admin";
    }
}
```

---

### *Spring security 3
这个是用mybatis的一个其他版本的实现，学习过程中可以暂时忽略

---

### Spring security OAuth2 1
把用户与权限的绑定关系放到了一个OAuth2的认证授权服务中 AuthorizationServerConfigurerAdapter

把资源以及资源需要的权限放到了其他资源服务中
ResourceServerConfigurerAdapter

---
### Spring security OAuth2 2 JWT token
添加了JWT做为token认证的方式，让资源服务可以自己解析token的正确性，不用再去找OAuth服务器去验证token

---

### Spring security OAuth2 Springcloud
添加了注册中心、网关
使用网关进行验证token的工作，并解析token之后分发给资源服务
资源服务只做一般的校验，不用解析JWT token


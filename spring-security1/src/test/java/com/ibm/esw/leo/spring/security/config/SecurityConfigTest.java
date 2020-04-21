package com.ibm.esw.leo.spring.security.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 加密解密方式比较：Bcrypt/Scrypt
 * https://www.cnblogs.com/gm-201705/p/9863918.html
 *
 *
 */
@RunWith(SpringRunner.class)
@Slf4j
public class SecurityConfigTest {

    @Test
    public void testBcrypt() {
        /**
         * user123: $2a$10$aZOkAF3bzYOfVPUsf7mWTec1hFtO5H0Upm/KtFeYKRYniT5khzJDa
         * admin123: $2a$10$6tJecEDcvUHtvKA5/WxRiuakZ2dc0rnbLtGX4aG9vYj4BjmFETgxO
         * secret: $2a$10$DMuq.GWAomIN4EjhxppD2ehj7T/S8NJ0BbpFNlU4We0E4G6Zw6iFq
         */
        String encrpted = BCrypt.hashpw("secret", BCrypt.gensalt());
        log.info("encrpted : {}", encrpted);

        boolean checkPw = BCrypt.checkpw("secret", encrpted);
        log.info("check checkPw : {}", checkPw);
    }


    @Test
    public void testScrypt() {
        String encrpted = new SCryptPasswordEncoder().encode("123");
        log.info("encrpted : {}", encrpted);

        boolean checkPw = new SCryptPasswordEncoder().matches("123", encrpted);
        log.info("check checkPw : {}", checkPw);
    }
}
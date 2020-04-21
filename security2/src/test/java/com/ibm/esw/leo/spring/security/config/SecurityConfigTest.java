package com.ibm.esw.leo.spring.security.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

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
        String encrpted = BCrypt.hashpw("user123", BCrypt.gensalt());
        log.info("encrpted : {}", encrpted);

        boolean checkPw = BCrypt.checkpw("user123", encrpted);
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
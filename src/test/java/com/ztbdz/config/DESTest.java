package com.ztbdz.config;


import com.ztbdz.web.util.DESUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DESTest {


    /**
     * 数据库加密
     */
    @Test
    public void desTest() {
        String temp = "12345";
        // 加密
        String str = DESUtil.getEncryptString(temp);
        System.out.println("加密后：" + str);
        // 解密
        String str_1 = DESUtil.getDecryptString(str);
        System.out.println("解密后：" + str_1);
    }

}

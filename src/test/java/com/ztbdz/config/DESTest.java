package com.ztbdz.config;


import com.ztbdz.web.util.DESUtil;
import com.ztbdz.web.util.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DESTest {


    /**
     * 加密
     */
    @Test
    public void desTest() {
        String temp = "123456";
        // DESC加密
        String str = DESUtil.getEncryptString(temp);
        System.out.println("DESC加密：" + str);
        // DESC解密
        System.out.println("DESC解密：" + DESUtil.getDecryptString(str));
        // MD5加密
        System.out.println("MD5加密：" + MD5.md5String(temp)); // d5205e8e64fd719774ca0039f5e9d119
    }

}

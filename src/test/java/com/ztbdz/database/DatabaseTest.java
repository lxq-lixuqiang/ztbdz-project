package com.ztbdz.database;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTest {

    /**
     * 数据库加密
     */
    @Test
    public void databaseEncoder() {
        //对mysql数据库进行加密
        StandardPBEStringEncryptor standardPBEStringEncryptor =new StandardPBEStringEncryptor();
        /*配置文件中配置如下的算法*/
        standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
        /*配置文件中配置的password*/
        standardPBEStringEncryptor.setPassword("ztbdz");/*比如：LIXUQIANGPASSWORD*/
        /*要加密的文本*/
        String url = standardPBEStringEncryptor.encrypt("jdbc:mysql://localhost:3306/ztbdz?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2b8");
        String name = standardPBEStringEncryptor.encrypt("root");
        String password =standardPBEStringEncryptor.encrypt("root123_");

        /*将加密的文本写到配置文件中*/
        System.out.println("url="+url);
        System.out.println("username="+name);
        System.out.println("password="+password);
    }

}

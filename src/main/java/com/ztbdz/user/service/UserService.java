package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.User;
import com.ztbdz.web.util.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {

    /**
     * 创建用户
     * @param user
     * @param code
     * @return
     */
    Result create(User user,String code);

    /**
     * 新增用户
     * @param user
     * @return
     */
    Integer insert(User user);


    /**
     * 更新用户 密码
     * @param userId
     * @param password
     * @param newPassword
     * @return
     */
    Result updatePassword(Long userId, String password, String newPassword);

    /**
     * 忘记密码
     * @param phone
     * @param newPassword
     * @return
     */
    Result forgetPassword(String phone, String newPassword);

    /**
     * 发送短信
     * @param phone
     * @return
     */
    Result sendSMS(String phone);

    /**
     * 获取用户
     * @param id
     * @return
     */
    User getById(Long id) throws Exception;


    /**
     * 更新用户
     * @param user
     * @return
     */
    Integer updateById(User user) throws Exception;

    /**
     * 删除用户
     * @param id
     * @return
     */
    Integer delete(Long id) throws Exception;


    /**
     * 查询多用户
     * @param page
     * @param size
     * @param user
     * @return
     */
    PageInfo<User> selectList(Integer page, Integer size, User user) throws Exception;

    /**
     * 查询用户信息
     * @param user
     * @return
     * @throws Exception
     */
    User select(User user) throws Exception;

    /**
     * 查询用户数量
     * @param user
     * @return
     * @throws Exception
     */
    Integer count(User user) throws Exception;

    /**
     * 查询用户和人员信息
     * @param id
     * @param username
     * @return
     */
    User selectMember(Long id,String username) throws Exception;

    /**
     * 上传Excel用户文件
     * @param file
     * @return
     */
    Result uploadExcel(MultipartFile file);

}

package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.RetrievePassword;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface RetrievePasswordService {

    /**
     * 创建
     * @param retrievePassword
     * @return
     */
    Integer insert(RetrievePassword retrievePassword) throws Exception;

    /**
     * 删除
     * @param id
     * @return
     */
    Integer delete(Long id) throws Exception;

    /**
     * 更新
     * @param retrievePassword
     * @return
     */
    Integer updateById(RetrievePassword retrievePassword) throws Exception;

    /**
     * id查询
     * @param id
     * @return
     */
    RetrievePassword getById(Long id) throws Exception;


    /**
     * 其他条件查询
     * @param retrievePassword
     * @return
     */
    List<RetrievePassword> selectList(RetrievePassword retrievePassword) throws Exception;


    /**
     * 分页查询
     * @param page
     * @param size
     * @param retrievePassword
     * @return
     */
    PageInfo<RetrievePassword> selectPage(Integer page, Integer size, RetrievePassword retrievePassword) throws Exception;


    /**
     * 查询
     * @param page
     * @param size
     * @param retrievePassword
     * @return
     */
    Result page(Integer page, Integer size,RetrievePassword retrievePassword);


    /**
     * 更新
     * @param retrievePassword
     * @return
     */
    Result createORupdate(RetrievePassword retrievePassword);


    /**
     * id查询
     * @param id
     * @return
     */
    Result selectById(Long id);


    /**
     * 审核
     * @param retrievePassword
     * @return
     */
    Result review(RetrievePassword retrievePassword);


    /**
     * 用户名查询
     * @param username
     * @return
     */
    Result selectByUsername(String username);

    /**
     * 查询带供应商信息
     * @param id
     * @return
     */
    Result selectBidderInfo(Long id);
}

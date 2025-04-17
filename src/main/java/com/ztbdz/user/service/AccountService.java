package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.Account;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.web.util.Result;

public interface AccountService {

    /**
     * 创建企业
     * @param account
     * @return
     */
    Integer insert(Account account) throws Exception;

    /**
     * 删除企业
     * @param id
     * @return
     */
    Integer delete(Long id) throws Exception;

    /**
     * 更新企业
     * @param account
     * @return
     */
    Integer updateById(Account account) throws Exception;

    /**
     * 查询企业
     * @param id
     * @return
     */
    Account getById(Long id) throws Exception;

    /**
     * 查询多企业
     * @param page
     * @param size
     * @param account
     * @return
     */
    PageInfo<Account> selectList(Integer page, Integer size, Account account) throws Exception;

    /**
     * 查询企业信息
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 查询企业列表
     * @param page
     * @param size
     * @param account
     * @return
     */
    Result list(Integer page,Integer size,Account account);


    /**
     * 更新企业
     * @param account
     * @return
     */
    Result update(Account account);
}

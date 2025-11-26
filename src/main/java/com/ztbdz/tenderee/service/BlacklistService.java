package com.ztbdz.tenderee.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.pojo.Blacklist;
import com.ztbdz.web.util.Result;

import java.util.List;


public interface BlacklistService {

    /**
     * 新增或更新黑名单
     * @param blacklist
     * @return
     */
    Integer insertOrUpdate(Blacklist blacklist) throws Exception;

    /**
     * 批量根据id删除黑名单
     * @param blacklistIds
     * @return
     * @throws Exception
     */
    Integer deletes(List<Long> blacklistIds) throws Exception;


    /**
     * 根据id查询黑名单信息
     * @param id
     * @return
     */
    Blacklist selectById(Long id) throws Exception;

    /**
     * 校验
     * @param bidderName
     * @return
     */
    Integer verifyBlacklist(String landlordName,String bidderName,String type) throws Exception;

    /**
     * 分页查询黑名单列表
     * @param page
     * @param size
     * @param blacklist
     * @return
     * @throws Exception
     */
    PageInfo<Blacklist> selectPage(Integer page, Integer size, Blacklist blacklist) throws Exception;

    /**
     * 创建和修改
     * @param blacklist
     * @return
     */
    Result createOrUpdate(Blacklist blacklist);

    /**
     * 删除
     * @param ids
     * @return
     */
    Result deleteList(List<Long> ids);

    /**
     * 分页查询
     * @param page
     * @param size
     * @param blacklist
     * @return
     */
    Result page(Integer page,Integer size,Blacklist blacklist);

    /**
     * 查询单个
     * @param id
     * @return
     */
    Result get(Long id);
}

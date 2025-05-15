package com.ztbdz.tenderee.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.pojo.Tenderee;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface TendereeService {

    /**
     * 新增招标信息
     * @param tenderee
     * @return
     */
    Result create(Tenderee tenderee);


    /**
     * 添加招标信息
     * @param tenderee
     * @return
     */
    Integer insert(Tenderee tenderee) throws Exception;

    /**
     * 根据项目id获取招标信息
     * @param projectId
     * @return
     */
    Tenderee selectByProjectId(Long projectId) throws Exception;


    /**
     * 更新招标信息
     * @param tenderee
     * @return
     */
    Integer updateById(Tenderee tenderee) throws Exception;

    /**
     * 修改招标信息
     * @param tenderee
     * @return
     */
    Result update(Tenderee tenderee);

    /**
     * 删除招标信息
     * @param id
     * @return
     */
    Integer deleteById(Long id) throws Exception;

    /**
     * 删除招标信息
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 查询招标信息和项目信息
     * @param id
     * @return
     * @throws Exception
     */
    Tenderee selectProject(Long id) throws Exception;

    /**
     * 获取中标结果
     * @param id
     * @return
     */
    Result getWinBidResult( Long id);

    /**
     * 查询招标信息
     * @param tenderee
     * @return
     */
    Result page(Integer page, Integer size,Tenderee tenderee);

    /**
     * 查询招标信息
     * @param tenderee
     * @return
     */
    PageInfo<Tenderee> selectList(Integer page, Integer size,Tenderee tenderee) throws Exception;
}

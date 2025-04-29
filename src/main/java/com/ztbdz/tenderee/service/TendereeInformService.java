package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.TendereeInform;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface TendereeInformService {

    /**
     * 根据招标id获取公告信息
     * @param tendereeId
     * @return
     * @throws Exception
     */
    List<TendereeInform> selectListByTendereeId(Long tendereeId) throws  Exception;


    /**
     * 发布公告
     * @param tendereeInform
     * @return
     */
    Result create( TendereeInform tendereeInform);

    /**
     * 添加公告
     * @param tendereeInform
     * @return
     */
    Integer insert(TendereeInform tendereeInform) throws Exception;

    /**
     * 修改公告
     * @param tendereeInform
     * @return
     */
    Result update(TendereeInform tendereeInform);

    /**
     * 更新公告
     * @param tendereeInform
     * @return
     */
    Integer updateById(TendereeInform tendereeInform) throws Exception;

    /**
     * 查询公告列表
     * @param tendereeInform
     * @return
     */
    Result list(TendereeInform tendereeInform);

    /**
     * 查询公告列表
     * @param tendereeInform
     * @return
     */
    List<TendereeInform> select(TendereeInform tendereeInform) throws Exception;


    /**
     * 获取公告
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 根据id获取公告
     * @param id
     * @return
     */
    TendereeInform selectById(Long id) throws Exception;
}

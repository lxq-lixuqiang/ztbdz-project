package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.Tender;

import java.util.List;

public interface TenderService {

    /**
     * 添加标段
     * @param tender
     * @return
     */
    Integer insert(Tender tender) throws Exception;

    /**
     * 根据项目id查询标段
     * @param projectId
     * @return
     * @throws Exception
     */
    List<Tender> selectListByProjectId(Long projectId) throws Exception;

    /**
     * 更加项目id删除标段
     * @param projectId
     * @return
     * @throws Exception
     */
    Integer deleteByProjectId(Long projectId) throws Exception;
}

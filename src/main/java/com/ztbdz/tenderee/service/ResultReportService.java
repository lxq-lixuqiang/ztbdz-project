package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.ResultReport;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface ResultReportService {

    /**
     * 新增废标
     * @param resultReport
     * @return
     */
    Integer insert(ResultReport resultReport) throws Exception;

    /**
     * 新增和更新废标
     * @param resultReport
     * @return
     */
    Result createOrUpdate(ResultReport resultReport);


    /**
     * 更新废标
     * @param resultReport
     * @return
     */
    Integer updateById(ResultReport resultReport) throws Exception;


    /**
     * 根据id查询废标
     * @param id
     * @return
     */
    ResultReport selectById(Long id) throws Exception;

    /**
     * 根据人员查询废标
     * @param projectId
     * @return
     */
    Result selectByProjectId(Long projectId);

    /**
     * 根据条件查询废标
     * @param resultReport
     * @return
     */
    List<ResultReport> select(ResultReport resultReport) throws Exception;

}

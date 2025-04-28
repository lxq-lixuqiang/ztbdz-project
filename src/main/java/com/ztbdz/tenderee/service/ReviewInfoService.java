package com.ztbdz.tenderee.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.pojo.ReviewInfo;
import com.ztbdz.tenderee.pojo.Speciality;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface ReviewInfoService {

    /**
     * 获取评审
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 新增评审
     * @param reviewInfo
     * @return
     */
    Result create(ReviewInfo reviewInfo);

    /**
     * 添加评审
     * @param reviewInfo
     * @return
     */
    Integer insert(ReviewInfo reviewInfo) throws Exception;

    /**
     * 修改评审
     * @param reviewInfo
     * @return
     */
    Result update(ReviewInfo reviewInfo);

    /**
     * 更新评审
     * @param reviewInfo
     * @return
     */
    Integer updateById(ReviewInfo reviewInfo) throws Exception;

    /**
     * 获取评审
     * @param id
     * @return
     * @throws Exception
     */
    ReviewInfo selectById(Long id) throws Exception;

    /**
     * 分页查询评审
     * @param page
     * @param size
     * @param projectName
     * @return
     */
    Result page(Integer page,Integer size, String projectName);

    /**
     * 分页查询评审
     * @param page
     * @param size
     * @param projectName
     * @return
     * @throws Exception
     */
    PageInfo<ReviewInfo> selectPage(Integer page, Integer size, String projectName) throws Exception;

    /**
     * 批量删除
     * @param ids
     * @return
     */
    Result deleteList(List<Long> ids);

    /**
     * 批量删除
     * @param ids
     * @return
     * @throws Exception
     */
    Integer deletes(List<Long> ids) throws Exception;


    /**
     * 专家抽取
     * @param id
     * @param hideExpert
     * @param hideAccount
     * @param specialityList
     * @return
     */
    Result randomExpert(Long id,String hideExpert, String hideAccount, List<Speciality> specialityList);
}

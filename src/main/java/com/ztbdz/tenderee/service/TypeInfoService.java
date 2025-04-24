package com.ztbdz.tenderee.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.pojo.Category;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface TypeInfoService {

    /**
     * 新增类别
     * @param category
     * @return
     */
    Integer insert(Category category) throws Exception;

    /**
     * 查询类别
     * @param id
     * @return
     */
    Category selectById(Long id) throws Exception;

    /**
     * 查询类别
     * @param ids
     * @return
     */
    List<Category> selectByIds(List<Long> ids) throws Exception;

    /**
     * 查询类别
     * @param category
     * @return
     */
    List<Category> selectList(Category category) throws Exception;

    /**
     * 查询类别数量
     * @param id
     * @param typeCode
     * @return
     */
    Integer countByTypeCode(String id,String typeCode) throws Exception;


    /**
     * 删除类别
     * @param ids
     * @return
     */
    Integer deletes(List<Long> ids) throws Exception;

    /**
     * 更新类别
     * @param category
     * @return
     */
    Integer updateById(Category category) throws Exception;

    /**
     * 查询多个类别
     * @param page
     * @param size
     * @param category
     * @return
     */
    PageInfo<Category> selectList(Integer page, Integer size, Category category) throws Exception;

    /**
     *  查询类别
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 查询类别列表
     * @param page
     * @param size
     * @param category
     * @return
     */
    Result list(Integer page,Integer size,Category category);


    /**
     * 创建类别
     * @param category
     * @return
     */
    Result create(Category category);

    /**
     * 更新类别
     * @param category
     * @return
     */
    Result update(Category category);

    /**
     * 批量删除类别
     * @param ids
     * @return
     */
    Result deleteList(List<Long> ids);



}

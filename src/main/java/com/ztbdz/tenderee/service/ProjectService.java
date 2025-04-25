package com.ztbdz.tenderee.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface ProjectService {

    /**
     * 添加项目信息
     * @param project
     * @return
     */
    Integer insert(Project project) throws Exception;

    /**
     * 查询项目列表
     * @param page
     * @param size
     * @param project
     * @return
     */
    Result page( Integer page, Integer size, Project project);

    /**
     * 查询项目列表
     * @param page
     * @param size
     * @param project
     * @return
     * @throws Exception
     */
    PageInfo<Project> selectList(Integer page, Integer size, Project project) throws Exception;

    /**
     * 查询项目
     * @param id
     * @return
     */
    Result get( Long id);

    /**
     * 修改项目
     * @param project
     * @return
     */
    Integer updateById(Project project) throws Exception;

    /**
     * 删除项目
     * @param id
     * @return
     */
    Integer delete(Long id) throws Exception;

    /**
     * 查询项目
     * @param id
     * @return
     */
    Project selectById(Long id) throws Exception;

    /**
     * 查询多项目
     * @param ids
     * @return
     * @throws Exception
     */
    List<Project> selectByIds(List<Long> ids) throws Exception;
}

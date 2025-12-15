package com.ztbdz.tenderee.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.pojo.Project;
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
     * @param states
     * @return
     */
    Result page( Integer page, Integer size, Project project,Integer states);

    /**
     * 查询需要抽取专家项目列表
     * @param page
     * @param size
     * @param project
     * @return
     */
    Result extractProjectList( Integer page, Integer size,Integer state, Project project);

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
     * 查询项目列表
     * @param page
     * @param size
     * @param project
     * @return
     * @throws Exception
     */
    PageInfo<Project> reviewEndProject(Integer page, Integer size, Project project) throws Exception;

    /**
     * 查询项目列表
     * @param page
     * @param size
     * @param project
     * @return
     * @throws Exception
     */
    PageInfo<Project> listAvailable(Integer page, Integer size, Project project,Integer state) throws Exception;

    /**
     * 查询进行中的项目列表
     * @param page
     * @param size
     * @param project
     * @return
     * @throws Exception
     */
    PageInfo<Project> runProject(Integer page, Integer size, Project project,Integer state) throws Exception;

    /**
     * 查询专家进行中的项目列表
     * @param page
     * @param size
     * @param project
     * @return
     * @throws Exception
     */
    PageInfo<Project> expertProject(Integer page, Integer size, Project project,Integer state) throws Exception;

    /**
     * 查询项目
     * @param id
     * @return
     */
    Result get( Long id);

    /**
     * 项目ID获取中标人信息
     * @param projectId
     * @return
     */
    Result winDid(Long projectId);

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

    /**
     * 查询项目
     * @param project
     * @return
     * @throws Exception
     */
    List<Project> select(Project project) throws Exception;

    /**
     * 查询多项目排序
     * @param ids
     * @param sort
     * @return
     * @throws Exception
     */
    List<Project> selectByIds3(List<Long> ids,String sort) throws Exception;

    /**
     * 查询多项目
     * @param ids
     * @param memberId
     * @return
     * @throws Exception
     */
    List<Project> selectByIds(List<Long> ids,Long memberId) throws Exception;
}

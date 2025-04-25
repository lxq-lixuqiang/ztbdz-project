package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface ProjectRegisterService {

    /**
     * 新增项目报名
     * @param projectRegister
     * @return
     */
    Result create(ProjectRegister projectRegister);

    /**
     * 添加项目报名
     * @param projectRegister
     * @return
     */
    Integer insert(ProjectRegister projectRegister) throws Exception;

    /**
     * 查询项目报名
     * @param projectRegister
     * @return
     * @throws Exception
     */
    List<ProjectRegister> selectList(ProjectRegister projectRegister) throws Exception;

    /**
     * 查询项目报名列表
     * @param page
     * @param size
     * @param project
     * @return
     */
    Result page(Integer page,Integer size,Project project);

    /**
     * 查询项目报名列表
     * @return
     */
    List<ProjectRegister> selectByCountProjectId();
}

package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.ProjectRegisterMapper;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import com.ztbdz.tenderee.service.ProjectRegisterService;
import com.ztbdz.tenderee.service.ProjectService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProjectRegisterServiceImpl implements ProjectRegisterService {

    @Autowired
    private ProjectRegisterMapper projectRegisterMapper;
    @Autowired
    private ProjectService projectService;


    @Override
    public Result create(ProjectRegister projectRegister) {
        try{
            Integer num = this.insert(projectRegister);
            if(num<=0) return Result.fail("报名失败");
            return Result.ok("报名成功");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("项目报名异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer insert(ProjectRegister projectRegister) throws Exception {
        return projectRegisterMapper.insert(projectRegister);
    }

    @Override
    public List<ProjectRegister> selectList(ProjectRegister projectRegister) throws Exception {
        QueryWrapper<ProjectRegister> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(projectRegister.getProject())) queryWrapper.eq("project_id",projectRegister.getProject().toString());
        if(!StringUtils.isEmpty(projectRegister.getMember())) queryWrapper.eq("member_id",projectRegister.getMember().toString());
        return projectRegisterMapper.selectList(queryWrapper);
    }

    @Override
    public Result page(Integer page, Integer size, Project project) {
        try{
            PageHelper.startPage(page, size);
            List<ProjectRegister> projectRegisterList = this.selectByCountProjectId();
            List<Long> projectIds = new ArrayList();
            for(ProjectRegister projectRegister : projectRegisterList){
                projectIds.add(projectRegister.getProject().getId());
            }
            List<Project> projectList = new ArrayList();
            if(projectIds.size()>0){
                projectList = projectService.selectByIds(projectIds);
                for(Project project1 : projectList) {
                    for (ProjectRegister projectRegister : projectRegisterList) {
                        if(project1.getId() == projectRegister.getId()){
                            project1.setProjectRegisters(projectRegister);
                            break;
                        }
                    }
                }
            }
            return Result.ok("查询成功！",new PageInfo(projectList));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询项目报名列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<ProjectRegister> selectByCountProjectId() {
        return projectRegisterMapper.selectByCountProjectId();
    }


}

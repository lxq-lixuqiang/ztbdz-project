package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.ProjectMapper;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.Tender;
import com.ztbdz.tenderee.pojo.Tenderee;
import com.ztbdz.tenderee.pojo.TendereeInform;
import com.ztbdz.tenderee.service.ProjectService;
import com.ztbdz.tenderee.service.TenderService;
import com.ztbdz.tenderee.service.TendereeInformService;
import com.ztbdz.tenderee.service.TendereeService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.List;


@Slf4j
@Service
public class ProjectServiceImpl  implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private TenderService tenderService;
    @Autowired
    private TendereeService tendereeService;
    @Autowired
    private TendereeInformService tendereeInformService;


    @Override
    public Integer insert(Project project) throws Exception {
        return projectMapper.insert(project);
    }

    @Override
    public Result page(Integer page, Integer size, Project project) {
        try{
            return Result.ok("查询成功！",selectList(page,size,project));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询项目列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public PageInfo<Project> selectList(Integer page, Integer size, Project project) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Project> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("update_date");

        if(!StringUtils.isEmpty(project.getProjectName())) queryWrapper.like("project_name", project.getProjectName());
        if(!StringUtils.isEmpty(project.getState())) queryWrapper.eq("state", project.getState());
        if(!StringUtils.isEmpty(project.getProjectClassify())) queryWrapper.eq("project_classify", project.getProjectClassify());
        if(!StringUtils.isEmpty(project.getProcurementMethod())) queryWrapper.eq("procurement_method", project.getProcurementMethod());
        return new PageInfo(projectMapper.selectList(queryWrapper));
    }

    @Override
    public Result get(Long id) {
        try{
            Project project = this.selectById(id);
            // 公告附件
            Tenderee tenderee = tendereeService.selectByProjectId(id);
            List<TendereeInform> tendereeInformList =  tendereeInformService.selectListByTendereeId(tenderee.getId());
            // 标段
            List<Tender> tenderList = tenderService.selectListByProjectId(id);

            project.setTenders(tenderList);
            tenderee.setProject(project);
            tenderee.setTendereeInforms(tendereeInformList);
            return Result.ok("查询成功",tenderee);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询项目异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer delete(Long id) throws Exception {
        QueryWrapper<Project> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",id);
        return projectMapper.delete(queryWrapper);
    }

    @Override
    public Integer updateById(Project project) throws Exception {
        return projectMapper.updateById(project);
    }

    @Override
    public Project selectById(Long id) throws Exception {
        return projectMapper.selectById(id);
    }

    @Override
    public List<Project> selectByIds(List<Long> ids) throws Exception {
        QueryWrapper<Project> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return projectMapper.selectList(queryWrapper);
    }

}

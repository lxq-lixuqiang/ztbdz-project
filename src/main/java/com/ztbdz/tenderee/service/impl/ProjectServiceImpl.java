package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.ProjectMapper;
import com.ztbdz.tenderee.pojo.*;
import com.ztbdz.tenderee.service.*;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Common;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    @Autowired
    private MemberService memberService;
    @Autowired
    private ReviewInfoService reviewInfoService;


    @Override
    public Integer insert(Project project) throws Exception {
        project.setMemberId(SystemConfig.getCreateMember().getId());
        return projectMapper.insert(project);
    }

    @Override
    public Result page(Integer page, Integer size, Project project,Integer states) {
        try{
            PageInfo<Project> pageData = null;
            switch (states){
                case 1:
                    pageData = this.selectList(page,size,project);
                    break;
                case 2:
                    pageData = this.listAvailable(page,size,project,-1);
                    break;
                case 3:
                    pageData = this.listAvailable(page,size,project,1);
                    break;
                case 4:
                    pageData = this.listAvailable(page,size,project,0);
                    break;
                case 5:
                    pageData = this.runProject(page,size,project,0);
                    break;
                case 6:
                    pageData = this.runProject(page,size,project,1);
                    break;
                case 7:
                    pageData = this.runProject(page,size,project,2);
                    break;
                case 8:
                    pageData = this.reviewEndProject(page,size,project);
                    break;
                case 9:
                    pageData = this.expertProject(page,size,project,1);
                    break;
                case 10:
                    pageData = this.expertProject(page,size,project,2);
                    break;
                case 11:
                    pageData = this.expertProject(page,size,project,null);
                    break;
            }
            return Result.ok("查询成功！",pageData);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询项目列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result extractProjectList(Integer page, Integer size,Integer state, Project project) {
        try{
            PageHelper.startPage(page, size);
            return Result.ok("查询成功！",new PageInfo(projectMapper.extractProjectList(state,project)));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询需要抽取专家项目列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public PageInfo<Project> selectList(Integer page, Integer size, Project project) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Project> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("update_date");
        if(!StringUtils.isEmpty(project)){
            if(!StringUtils.isEmpty(project.getProjectName())) queryWrapper.like("project_name", project.getProjectName());
            if(!StringUtils.isEmpty(project.getState())) queryWrapper.eq("state", project.getState());
            if(!StringUtils.isEmpty(project.getProjectClassify())) queryWrapper.eq("project_classify", project.getProjectClassify());
            if(!StringUtils.isEmpty(project.getProcurementMethod())) queryWrapper.eq("procurement_method", project.getProcurementMethod());
            if(!StringUtils.isEmpty(project.getMemberId())) queryWrapper.eq("member_id", project.getMemberId());
        }
        return new PageInfo(projectMapper.selectList(queryWrapper));
    }

    @Override
    public PageInfo<Project> reviewEndProject(Integer page, Integer size, Project project) throws Exception {
        PageHelper.startPage(page, size);
        if(project.getProjectRegisters()==null){
            project.setProjectRegisters(new ProjectRegister());
        }
        project.getProjectRegisters().setMember(new Member(SystemConfig.getSession(Common.SESSION_LOGIN_MEMBER_ID)));
        return new PageInfo(projectMapper.reviewEndProject(project));
    }

    @Override
    public PageInfo<Project> listAvailable(Integer page, Integer size, Project project,Integer state) throws Exception {
        PageHelper.startPage(page, size);
        return new PageInfo(projectMapper.listAvailable(project,SystemConfig.getCreateMember().getId(),state));
    }

    @Override
    public PageInfo<Project> runProject(Integer page, Integer size, Project project,Integer state) throws Exception {
        PageHelper.startPage(page, size);
        List<Project> projectList = projectMapper.runProject(project,SystemConfig.getCreateMember().getId(),state);
        for(Project project1 : projectList){
            if(project1.getState() == 1){
                project1.setState(2); // 将可报名项目 更新为 正在进行项目
                this.updateById(project1);
            }
        }
        return new PageInfo(projectList);
    }

    @Override
    public PageInfo<Project> expertProject(Integer page, Integer size, Project project,Integer state) throws Exception {
        PageHelper.startPage(page, size);
        return new PageInfo(projectMapper.expertProject(project,SystemConfig.getCreateMember().getId(),state));
    }

    @Override
    public Result get(Long id) {
        try{
            Project project = this.selectById(id);
            ReviewInfo reviewInfo = reviewInfoService.selectByProjectId(id);
            if(!StringUtils.isEmpty(reviewInfo) && !StringUtils.isEmpty(reviewInfo.getExpertLeader())){
                project.setProjectExpertLeader(String.valueOf(reviewInfo.getExpertLeader()));
            }
            // 公告附件
            Tenderee tenderee = tendereeService.selectByProjectId(id);
            List<TendereeInform> tendereeInformList =  tendereeInformService.selectListByTendereeId(tenderee.getId());
            // 标段
            List<Tender> tenderList = tenderService.selectListByProjectId(id);

            project.setTenders(tenderList);
            project.setMoneyUppercase(SystemConfig.digitUppercase(project.getMoney()));
            tenderee.setProject(project);
            tenderee.setTendereeInforms(tendereeInformList);
            return Result.ok("查询成功",tenderee);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询项目异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result winDid(Long projectId) {
        try{
            ReviewInfo reviewInfo = reviewInfoService.selectByProjectId(projectId);
            if(StringUtils.isEmpty(reviewInfo) || StringUtils.isEmpty(reviewInfo.getSelectExpert())){
                return Result.ok("查询成功",new ArrayList<ReviewInfo>());
            }
            String getSelectExpert = reviewInfo.getSelectExpert();
            String[] selectExperts = getSelectExpert.split(",");
            List<Long> ids = new ArrayList<Long>();
            for(String selectExpert : selectExperts){
                ids.add(Long.valueOf(selectExpert));
            }
            return Result.ok("查询成功",memberService.selectByIds(ids));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("项目ID获取中标人信息异常，原因："+e.getMessage());
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
        return selectByIds(ids,null);
    }

    @Override
    public List<Project> selectByIds(List<Long> ids,Long memberId) throws Exception {
        return projectMapper.selectByIds(ids,memberId);
    }

}

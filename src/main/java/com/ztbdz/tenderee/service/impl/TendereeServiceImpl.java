package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.TendereeMapper;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.Tender;
import com.ztbdz.tenderee.pojo.Tenderee;
import com.ztbdz.tenderee.service.ProjectService;
import com.ztbdz.tenderee.service.TenderService;
import com.ztbdz.tenderee.service.TendereeService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class TendereeServiceImpl implements TendereeService {
    @Autowired
    private TendereeMapper tendereeMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TenderService tenderService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result create(Tenderee tenderee) {
        try{
            // 项目信息
            Project project = tenderee.getProject();
            projectService.insert(project);
            // 标段
            List<Tender> tenders =  project.getTenders();
            for(Tender tender : tenders){
                tender.setProjectId(project.getId());
                tenderService.insert(tender);
            }
            this.insert(tenderee);
            return Result.ok("新增成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("新增招标信息异常，原因："+e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result update(Tenderee tenderee) {
        try{
            //更新项目信息
            Project project = tenderee.getProject();
            projectService.updateById(project);
            //更新标段,可能有新增或删除情况，所以采用先删除再添加
            List<Tender> tenderList = project.getTenders();
            tenderService.deleteByProjectId(project.getId());
            for(Tender tender : tenderList){
                tenderService.insert(tender);
            }
            //更新招标信息
            Integer num = this.updateById(tenderee);
            if(num<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail("更新失败！");
            }
            return Result.ok("更新成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新项目异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer deleteById(Long id) throws Exception {
        return tendereeMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result delete(Long id) {
        try{
            Tenderee tenderee = this.selectProject(id);
            Long projectId = tenderee.getProject().getId();
            //删除标段
            tenderService.deleteByProjectId(projectId);
            //删除项目信息
            projectService.delete(projectId);
            //删除招标信息
            if(this.deleteById(id)<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail("删除失败！");
            }
            return Result.ok("删除成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("删除招标异常，原因："+e.getMessage());
        }
    }

    @Override
    public Tenderee selectProject(Long id) throws Exception {
        return tendereeMapper.selectProject(id);
    }

    @Override
    public Integer insert(Tenderee tenderee) throws Exception {
        return tendereeMapper.insert(tenderee);
    }

    @Override
    public Tenderee selectByProjectId(Long projectId) throws Exception {
        QueryWrapper<Tenderee> queryWrapper = new QueryWrapper();
        queryWrapper.eq("project_id", projectId.toString());
        return tendereeMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(Tenderee tenderee) throws Exception {
        return tendereeMapper.updateById(tenderee);
    }
}

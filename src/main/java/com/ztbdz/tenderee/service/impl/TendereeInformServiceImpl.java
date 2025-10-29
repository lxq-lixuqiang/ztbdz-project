package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.TendereeInformMapper;
import com.ztbdz.tenderee.pojo.TendereeInform;
import com.ztbdz.tenderee.service.TendereeInformService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "tendereeInform")
public class TendereeInformServiceImpl implements TendereeInformService {
    @Autowired
    private TendereeInformMapper tendereeInformMapper;

    @Cacheable
    @Override
    public List<TendereeInform> selectListByTendereeId(Long tendereeId) throws Exception {
        QueryWrapper<TendereeInform> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("type");
        queryWrapper.eq("tenderee_id", tendereeId.toString());
        return tendereeInformMapper.selectList(queryWrapper);
    }

    @Override
    public synchronized Result create(TendereeInform tendereeInform) {
        try{
            if(this.insert(tendereeInform)<=0) return Result.fail("发布失败！");
            return Result.ok("发布成功！",tendereeInform.getId());
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("发布招标公告异常，原因："+e.getMessage());
        }
    }

    @CacheEvict(cacheNames = "tendereeInform",allEntries = true)
    @Override
    public Integer insert(TendereeInform tendereeInform) throws Exception {
        return tendereeInformMapper.insert(tendereeInform);
    }

    @Override
    public Result update(TendereeInform tendereeInform) {
        try{
            if(this.updateById(tendereeInform)<=0) return Result.fail("更新失败！");
            return Result.ok("更新成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新招标公告异常，原因："+e.getMessage());
        }
    }

    @CacheEvict(cacheNames = "tendereeInform",allEntries = true)
    @Override
    public Integer updateById(TendereeInform tendereeInform) throws Exception {
        return tendereeInformMapper.updateById(tendereeInform);
    }

    @Override
    public Result list(TendereeInform tendereeInform) {
        try{
            return Result.ok("查询成功！",this.select(tendereeInform));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询公告列表异常，原因："+e.getMessage());
        }
    }

    @Cacheable
    @Override
    public List<TendereeInform> select(TendereeInform tendereeInform) throws Exception {
        QueryWrapper<TendereeInform> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date");
        queryWrapper.eq("is_public","1");
        if(!StringUtils.isEmpty(tendereeInform.getTitle())) queryWrapper.like("title", tendereeInform.getTitle());
        if(!StringUtils.isEmpty(tendereeInform.getAssociationId())) queryWrapper.like("association_id", tendereeInform.getAssociationId());
        if(!StringUtils.isEmpty(tendereeInform.getTendereeId())) queryWrapper.like("tenderee_id", tendereeInform.getTendereeId());
        if(!StringUtils.isEmpty(tendereeInform.getProjectId())) queryWrapper.like("project_id", tendereeInform.getProjectId());
        if(!StringUtils.isEmpty(tendereeInform.getType())) queryWrapper.like("type", tendereeInform.getType());
        return tendereeInformMapper.selectList(queryWrapper);
    }

    @Override
    public Result get(Long id) {
        try{
            return Result.ok("查询成功！",this.selectById(id));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询公告异常，原因："+e.getMessage());
        }
    }

    @Cacheable
    @Override
    public TendereeInform selectById(Long id) throws Exception {
        return tendereeInformMapper.selectById(id);
    }

    @Override
    public Result page(Integer page, Integer size, TendereeInform tendereeInform) {
        try{
            PageHelper.startPage(page, size);
            return Result.ok("查询成功！",new PageInfo(this.select(tendereeInform)));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询澄清列表异常，原因："+e.getMessage());
        }
    }

}

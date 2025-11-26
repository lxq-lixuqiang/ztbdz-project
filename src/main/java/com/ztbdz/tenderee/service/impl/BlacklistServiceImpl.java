package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.BlacklistMapper;
import com.ztbdz.tenderee.pojo.Blacklist;
import com.ztbdz.tenderee.service.BlacklistService;
import com.ztbdz.web.config.SystemConfig;
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
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Override
    public Integer insertOrUpdate(Blacklist blacklist) throws Exception {
        blacklist.setMemberId(SystemConfig.getCreateMember().getId());
        if(StringUtils.isEmpty(blacklist.getId())){
            return blacklistMapper.insert(blacklist);
        }
        return blacklistMapper.updateById(blacklist);
    }

    @Override
    public Integer deletes(List<Long> blacklistIds) throws Exception {
        QueryWrapper<Blacklist> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",blacklistIds);
        return blacklistMapper.delete(queryWrapper);
    }

    @Override
    public Blacklist selectById(Long id) throws Exception {
        return blacklistMapper.selectById(id);
    }

    @Override
    public Integer verifyBlacklist(String landlordName,String bidderName,String type) throws Exception {
        return blacklistMapper.verifyBlacklist(landlordName,bidderName,type);
    }

    @Override
    public PageInfo<Blacklist> selectPage(Integer page, Integer size, Blacklist blacklist) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Blacklist> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("update_date");

        if(!StringUtils.isEmpty(blacklist.getMemberId())) queryWrapper.eq("member_id", blacklist.getMemberId());
        if(!StringUtils.isEmpty(blacklist.getBidderName())) queryWrapper.eq("bidder_name", blacklist.getBidderName());
        if(!StringUtils.isEmpty(blacklist.getType())) queryWrapper.eq("type", blacklist.getType());
        if(!StringUtils.isEmpty(blacklist.getLandlordName())) queryWrapper.eq("landlord_name", blacklist.getLandlordName());
        return new PageInfo(blacklistMapper.selectList(queryWrapper));
    }

    @Override
    public Result createOrUpdate(Blacklist blacklist) {
        try{
            return Result.ok("新增或更新成功！",this.insertOrUpdate(blacklist));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("新增或更新异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result deleteList(List<Long> ids) {
        try{
            return Result.ok("删除成功！",this.deletes(ids));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("批量删除黑名单列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result page(Integer page, Integer size, Blacklist blacklist) {
        try{
            return Result.ok("查询成功！",this.selectPage(page,size,blacklist));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询黑名单列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result get(Long id) {
        try{
            return Result.ok("查询成功！",this.selectById(id));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询黑名单异常，原因："+e.getMessage());
        }
    }
}

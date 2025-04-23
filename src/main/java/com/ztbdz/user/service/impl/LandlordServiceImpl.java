package com.ztbdz.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.LandlordMapper;
import com.ztbdz.user.pojo.Landlord;
import com.ztbdz.user.service.LandlordService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.MD5;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class LandlordServiceImpl implements LandlordService {

    @Autowired
    private LandlordMapper landlordMapper;

    @Override
    public Landlord select(Landlord landlord) throws Exception {
        QueryWrapper<Landlord> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(landlord.getName())) queryWrapper.eq("name",landlord.getName());
        return landlordMapper.selectOne(queryWrapper);
    }

    @Override
    public Landlord getById(Long id) throws Exception {
        QueryWrapper<Landlord> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        return landlordMapper.selectOne(queryWrapper);
    }

    @Override
    public PageInfo<Landlord> selectList(Integer page, Integer size, Landlord landlord) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Landlord> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date");
        if(!StringUtils.isEmpty(landlord.getContact())) queryWrapper.like("member_id", landlord.getContact());
        return new PageInfo(landlordMapper.selectList(queryWrapper));
    }

    @Override
    public Result get(Long id) {
        try{
            Landlord landlord = getById(id);
            return Result.ok("查询成功！",landlord);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询业主异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result list(Integer page, Integer size, Landlord landlord) {
        try{
            PageInfo<Landlord> landlordPageInfo = selectList(page,size,landlord);
            return Result.ok("查询成功！",landlordPageInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询业主列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer insert(Landlord landlord) throws Exception {
        return landlordMapper.insert(landlord);
    }

    @Override
    public Integer update(Landlord landlord) throws Exception {
        QueryWrapper<Landlord> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", landlord.getId());
        return landlordMapper.update(landlord,queryWrapper);
    }

    @Override
    public Result create(Landlord landlord, String code) {
        try{
            //TODO 对比短信验证码
//            Object codeRedis = redisTemplate.opsForValue().get(user.getMember().getPhone()+SystemConfig.SMS);
//            if(StringUtils.isEmpty(codeRedis)) return Result.fail("验证码已失效，请重新发送！");
//            if(!codeRedis.toString().equals(code))  return Result.fail("验证码错误！");
            landlord.setPassword(MD5.md5String(landlord.getPhone()+SystemConfig.DEFAULT_PASSWORD));
            if(count(landlord)>0) return Result.fail("用户名已存在！");

            Integer num = insert(landlord);
            if(num<=0) return Result.fail("注册失败！");
            return Result.ok("注册成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("注册业主异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result updateById(Landlord landlord) {
        try{
            Integer num = update(landlord);
            if(num<=0) return Result.fail("修改失败！");
            return Result.ok("修改成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("修改业主异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer count(Landlord landlord) throws Exception {
        QueryWrapper<Landlord> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(landlord.getName())) queryWrapper.eq("name",landlord.getName());
        return landlordMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer deletes(List<Long> ids) throws Exception {
        QueryWrapper<Landlord> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return landlordMapper.delete(queryWrapper);
    }

    @Override
    public Result deleteList(List<Long> ids) {
        try{
            if(this.deletes(ids)<=0) return Result.fail("删除失败！");
            return Result.ok("删除成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("删除角色异常，原因："+e.getMessage());
        }
    }
}

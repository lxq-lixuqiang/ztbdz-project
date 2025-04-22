package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.MenuAuthorizeMapper;
import com.ztbdz.user.pojo.MenuAuthorize;
import com.ztbdz.user.service.MenuAuthorizeService;
import com.ztbdz.user.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class MenuAuthorizeServiceImpl implements MenuAuthorizeService {

    @Autowired
    private MenuAuthorizeMapper menuAuthorizeMapper;

    @Override
    public Integer insert(MenuAuthorize menuAuthorize) throws Exception {
        return menuAuthorizeMapper.insert(menuAuthorize);
    }

    @Override
    public Integer delete(Long id) throws Exception {
        QueryWrapper<MenuAuthorize> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        return menuAuthorizeMapper.delete(queryWrapper);
    }

    @Override
    public Integer updateById(MenuAuthorize menuAuthorize) throws Exception {
        QueryWrapper<MenuAuthorize> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", menuAuthorize.getId().toString());
        return menuAuthorizeMapper.update(menuAuthorize,queryWrapper);
    }

    @Override
    public MenuAuthorize select(MenuAuthorize menuAuthorize) throws Exception {
        QueryWrapper<MenuAuthorize> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", menuAuthorize.getId().toString());
        return menuAuthorizeMapper.selectOne(queryWrapper);
    }

    @Override
    public PageInfo<MenuAuthorize> selectList(Integer page, Integer size, MenuAuthorize menuAuthorize) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<MenuAuthorize> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date");

        if(!StringUtils.isEmpty(menuAuthorize.getName())) queryWrapper.like("name", menuAuthorize.getName());
        if(!StringUtils.isEmpty(menuAuthorize.getPath())) queryWrapper.eq("path", menuAuthorize.getPath());
        return new PageInfo(menuAuthorize.selectList(queryWrapper));
    }

    @Override
    public Result get(Long id) {
        try{
            MenuAuthorize meunAuthorize = new MenuAuthorize();
            meunAuthorize.setId(id);
            meunAuthorize = this.select(meunAuthorize);
            return Result.ok("查询成功！",meunAuthorize);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询权限异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result list(Integer page, Integer size, MenuAuthorize menuAuthorize) {
        try{
            PageInfo<MenuAuthorize> accountPage = selectList(page, size, menuAuthorize);
            return Result.ok("查询成功！",accountPage);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询权限列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result update(MenuAuthorize menuAuthorize) {
        try{
            if(countBySign(menuAuthorize.getId().toString(),menuAuthorize.getSign())>0) return Result.fail("权限标识不能重复！");

            Integer num = this.updateById(menuAuthorize);
            if(num>0) return Result.ok("更新成功！");
            return Result.fail("更新失败！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新权限异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer count(MenuAuthorize menuAuthorize) throws Exception {
        QueryWrapper<MenuAuthorize> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(menuAuthorize.getName())) queryWrapper.eq("name",menuAuthorize.getName());
        if(!StringUtils.isEmpty(menuAuthorize.getSign())) queryWrapper.eq("sign",menuAuthorize.getSign());
        return menuAuthorizeMapper.selectCount(queryWrapper);
    }

    @Override
    public Result create(MenuAuthorize menuAuthorize) {
        try{
            if(countBySign(null,menuAuthorize.getSign())>0) return Result.fail("权限标识不能重复！");

            Integer num = this.insert(menuAuthorize);
            if(num>0) return Result.ok("创建成功！");
            return Result.fail("创建失败！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("创建权限异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<MenuAuthorize> selectByIds(List<Long> ids) throws Exception{
        QueryWrapper<MenuAuthorize> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", ids);
        return menuAuthorizeMapper.selectList(queryWrapper);
    }

    @Override
    public Integer countBySign(String id, String sign) throws Exception {
        QueryWrapper<MenuAuthorize> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(id)) queryWrapper.ne("id", id);
        queryWrapper.eq("sign", sign);
        return menuAuthorizeMapper.selectCount(queryWrapper);
    }
}

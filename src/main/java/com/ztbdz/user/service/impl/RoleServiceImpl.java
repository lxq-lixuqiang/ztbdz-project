package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.RoleMapper;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.service.RoleService;
import com.ztbdz.user.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Integer insert(Role role) throws Exception {
        role.init();
        return roleMapper.insert(role);
    }

    @Override
    public Role select(Role role) throws Exception {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(role.getType())) queryWrapper.eq("type", role.getType());
        return roleMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer deletes(List<String> ids) throws Exception {
        Role role = new Role();
        role.setIsDefault(0);
        PageInfo<Role> rolePageInfo = selectList(1, 5, role);


        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return roleMapper.delete(queryWrapper);
    }

    @Override
    public Integer updateById(Role role) throws Exception {
        return null;
    }

    @Override
    public Role getById(Long id) throws Exception {
        return null;
    }

    @Override
    public PageInfo<Role> selectList(Integer page, Integer size, Role role) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date");

        if(!StringUtils.isEmpty(role.getTypeName())) queryWrapper.like("type_name", role.getTypeName());
        if(!StringUtils.isEmpty(role.getType())) queryWrapper.eq("type", role.getType());
        if(!StringUtils.isEmpty(role.getIsDefault())) queryWrapper.eq("is_default", role.getIsDefault());
        return new PageInfo(roleMapper.selectList(queryWrapper));
    }

    @Override
    public Result get(Long id) {
        return null;
    }

    @Override
    public Result list(Integer page, Integer size, Role role) {
        return null;
    }

    @Override
    public Result update(Role role) {
        return null;
    }

    @Override
    public Result deleteList(List<String> ids) {
        try{
            if(deletes(ids)<=0) return Result.fail("删除失败！");
            return Result.ok("删除成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("删除角色异常，原因："+e.getMessage());
        }
    }
}

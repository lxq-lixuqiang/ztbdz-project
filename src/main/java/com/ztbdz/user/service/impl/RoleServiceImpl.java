package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.RoleMapper;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.MenuAuthorize;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.MenuAuthorizeService;
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
    @Autowired
    private MemberService memberService;
    @Autowired
    private MenuAuthorizeService menuAuthorizeService;

    @Override
    public Integer insert(Role role) throws Exception {
        return roleMapper.insert(role);
    }

    @Override
    public Role select(Role role) throws Exception {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(role.getId())) queryWrapper.eq("id", role.getId());
        if(!StringUtils.isEmpty(role.getType())) queryWrapper.eq("type", role.getType());
        return roleMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer deletes(List<Long> ids) throws Exception {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return roleMapper.delete(queryWrapper);
    }

    @Override
    public Integer updateById(Role role) throws Exception {
        role.update();
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", role.getId().toString());
        return roleMapper.updateById(role);
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
        try{
            Role role = new Role();
            role.setId(id);
            return Result.ok("查询成功",select(role));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询人员异常，原因："+e.getMessage());
        }

    }

    @Override
    public Result list(Integer page, Integer size, Role role) {
        try{
            PageInfo<Role> rolePageInfo =  selectList(page,size,role);
            return Result.ok("查询成功！",rolePageInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询角色列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result create(Role role) {
        try{
            Integer num = this.insert(role);
            if(num<=0) return Result.fail("创建失败！");
            return Result.ok("创建成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("创建角色异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result update(Role role) {
        try{
            Integer num = this.updateById(role);
            if(num<=0) return Result.fail("更新失败！");
            return Result.ok("更新成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新角色异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result deleteList(List<Long> ids) {
        try{
            Role role = new Role();
            Member member = new Member();
            for(Long id : ids){ // 防止默认角色被删除
                role.setId(id);
                Role selectRole = this.select(role);
                if(selectRole.getIsDefault() == 0) return Result.fail("默认角色不能删除！");

                member.setRole(role);
                PageInfo<Member> memberPageInfo = memberService.selectList(1,1,member);
                if(memberPageInfo.getTotal() > 0) return Result.fail("角色有人员在使用，无法删除！");
            }

            if(this.deletes(ids)<=0) return Result.fail("删除失败！");
            return Result.ok("删除成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("删除角色异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result allocation(Long roleId, List<Long> meunIds) {
        try{
            Role role = new Role();
            role.setId(roleId);
            role = this.select(role);

            List<MenuAuthorize> menuAuthorizeList = menuAuthorizeService.selectByIds(meunIds);
            role.setMeunAuthorize(menuAuthorizeList);
            Integer num = updateById(role);
            if(num<=0) return Result.fail("分配失败！");
            return Result.ok("分配成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("分配权限异常，原因："+e.getMessage());
        }
    }
}

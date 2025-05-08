package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.RoleMapper;
import com.ztbdz.user.mapper.RoleRelatedAuthorizeMapper;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.pojo.RoleRelatedAuthorize;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.MenuAuthorizeService;
import com.ztbdz.user.service.RoleService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MenuAuthorizeService menuAuthorizeService;
    @Autowired
    private RoleRelatedAuthorizeMapper roleRelatedAuthorizeMapper;

    @Override
    public Integer insert(Role role) throws Exception {
        role.setIsDefault(1);
        return roleMapper.insert(role);
    }

    @Override
    public Role selectById(Long id) throws Exception {
        return roleMapper.selectById(id);
    }

    @Override
    public List<Role> selectList(Role role) throws Exception {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(role.getId())) queryWrapper.eq("id", role.getId());
        if(!StringUtils.isEmpty(role.getType())) queryWrapper.eq("type", role.getType());
        if(!StringUtils.isEmpty(role.getTypeName())) queryWrapper.eq("type_name", role.getTypeName());
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public Integer countByTypeAndTypeName(String id,String type,String typeName) throws Exception {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(id)){
            queryWrapper.ne("id", id).and(wrapper ->
                wrapper.eq("type", type).or().eq("type_name", typeName)
            );
        }else{
            queryWrapper.eq("type", type).or().eq("type_name", typeName);
        }

        return roleMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer deletes(List<Long> ids) throws Exception {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return roleMapper.delete(queryWrapper);
    }

    @Override
    public Integer updateById(Role role) throws Exception {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", role.getId().toString());
        return roleMapper.updateById(role);
    }

    @Override
    public PageInfo<Role> selectList(Integer page, Integer size, Role role) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("update_date");

        if(!StringUtils.isEmpty(role.getTypeName())) queryWrapper.like("type_name", role.getTypeName());
        if(!StringUtils.isEmpty(role.getType())) queryWrapper.eq("type", role.getType());
        if(!StringUtils.isEmpty(role.getIsDefault())) queryWrapper.eq("is_default", role.getIsDefault());
        return new PageInfo(roleMapper.selectList(queryWrapper));
    }

    @Override
    public Result get(Long id) {
        try{
            Role role = this.selectById(id);
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            this.getMenuAuthorizeInfo(roles);
            return Result.ok("查询成功",roles.get(0));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询人员异常，原因："+e.getMessage());
        }

    }

    @Override
    public Result list(Integer page, Integer size, Role role) {
        try{
            PageInfo<Role> rolePageInfo =  selectList(page,size,role);
            this.getMenuAuthorizeInfo(rolePageInfo.getList());
            return Result.ok("查询成功！",rolePageInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询角色列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result create(Role role) {
        try{
            // 标识和名称不能一样
            if(countByTypeAndTypeName(null,role.getType(),role.getTypeName())>0) return Result.fail("角色名称或角色标识不能重复！");

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
            // 标识和名称不能一样
            if(countByTypeAndTypeName(role.getId().toString(),role.getType(),role.getTypeName())>0) return Result.fail("角色名称或角色标识不能重复！");

            Integer num = this.updateById(role);
            if(num<=0) return Result.fail("更新失败！");
            return Result.ok("更新成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新角色异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteList(List<Long> ids) {
        try{
            Role role;
            Member member = new Member();
            for(Long id : ids){ // 防止默认角色被删除
                role = this.selectById(id);
                if(role.getIsDefault() == 0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("默认角色不能删除！");
                }

                role = new Role();
                role.setId(id);
                member.setRole(role);
                List<Member> memberList = memberService.selectList(member);
                if(memberList.size() > 0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("角色有人员在使用，无法删除！");
                }
            }

            if(this.deletes(ids)<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail("删除失败！");
            }
            return Result.ok("删除成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("删除角色异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result allocation(List<RoleRelatedAuthorize> roleRelatedAuthorizeList) {
        try{
            //重新分配
            Map<Long,Long> roleMap = new HashMap();
            for(RoleRelatedAuthorize roleRelatedAuthorize : roleRelatedAuthorizeList){
                roleMap.put(roleRelatedAuthorize.getRoleId(),roleRelatedAuthorize.getRoleId());
            }
            QueryWrapper<RoleRelatedAuthorize> queryWrapper ;
            for(Long role : roleMap.keySet()){
                queryWrapper = new QueryWrapper();
                queryWrapper.eq("role_id",role.toString());
                roleRelatedAuthorizeMapper.delete(queryWrapper);
            }
            for(RoleRelatedAuthorize roleRelatedAuthorize : roleRelatedAuthorizeList){
                roleRelatedAuthorizeMapper.insert(roleRelatedAuthorize);
            }
            return Result.ok("分配成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("分配权限异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result allocationMember(Role role) {
        try{
            List<Member> memberList = role.getMember();
            for(Member member : memberList){
                member.setRole(role);
               memberService.updateById(member);
            }
            return Result.ok("分配成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("分配人员异常，原因："+e.getMessage());
        }
    }

    @Override
    public void getMenuAuthorizeInfo(List<Role> roleList) throws Exception {
        this.getMenuAuthorizeInfo(roleList,true);
    }

    @Override
    public void getMenuAuthorizeInfo(List<Role> roleList,boolean isMember) throws Exception{
        QueryWrapper<RoleRelatedAuthorize> queryWrapper ;
        for(Role role : roleList){
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("role_id",role.getId().toString());
            List<RoleRelatedAuthorize> roleRelatedAuthorizeList = roleRelatedAuthorizeMapper.selectList(queryWrapper);
            if(roleRelatedAuthorizeList!=null && roleRelatedAuthorizeList.size()>0){ //菜单权限
                List<Long> ids = new ArrayList();
                for(RoleRelatedAuthorize roleRelatedAuthorize : roleRelatedAuthorizeList){
                    ids.add(roleRelatedAuthorize.getMenuAuthorizeId());
                }
                role.setMeunAuthorize(menuAuthorizeService.selectByIds(ids));
            }
            if(isMember){
                // 分配人员
                Member member = new Member();
                member.setRole(role);
                role.setMember(memberService.selectList(member));
            }
        }

    }

}

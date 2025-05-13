package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.pojo.RoleRelatedAuthorize;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface RoleService {

    /**
     * 新增角色
     * @param role
     * @return
     */
    Integer insert(Role role) throws Exception;

    /**
     * 查询角色
     * @param id
     * @return
     */
    Role selectById(Long id) throws Exception;

    /**
     * 查询角色
     * @param role
     * @return
     */
    List<Role> selectList(Role role) throws Exception;

    /**
     * 查询角色数量
     * @param type
     * @param typeName
     * @return
     */
    Integer countByTypeAndTypeName(String id,String type,String typeName) throws Exception;


    /**
     * 删除角色
     * @param ids
     * @return
     */
    Integer deletes(List<Long> ids) throws Exception;

    /**
     * 更新角色
     * @param role
     * @return
     */
    Integer updateById(Role role) throws Exception;

    /**
     * 查询多个角色
     * @param page
     * @param size
     * @param role
     * @return
     */
    PageInfo<Role> selectList(Integer page, Integer size, Role role) throws Exception;

    /**
     *  查询角色
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 查询角色列表
     * @param page
     * @param size
     * @param role
     * @return
     */
    Result list(Integer page,Integer size,Role role);


    /**
     * 创建角色
     * @param role
     * @return
     */
    Result create(Role role);

    /**
     * 更新角色
     * @param role
     * @return
     */
    Result update(Role role);

    /**
     * 批量删除角色
     * @param ids
     * @return
     */
    Result deleteList(List<Long> ids);


    /**
     * 角色分配权限
     * @param roleRelatedAuthorizeList
     * @return
     */
    Result allocation(List<RoleRelatedAuthorize> roleRelatedAuthorizeList);


    /**
     * 角色分配人员
     * @param role
     * @return
     */
    Result allocationMember(Role role);


    /**
     * 获取角色分配的菜单权限
     * @param roleList
     * @throws Exception
     */
    void getMenuAuthorizeInfo(List<Role> roleList) throws Exception;

    /**
     * 获取角色分配的菜单权限
     * @param roleList
     * @throws Exception
     */
    void getMenuAuthorizeInfo(List<Role> roleList,boolean isMember) throws Exception;

    /**
     * 校验权限
     * @param url 访问的页面地址
     * @return
     */
    boolean verifyAuthority(String url) throws Exception;
}

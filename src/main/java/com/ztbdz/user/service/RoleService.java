package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.web.util.Result;

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
     * @param role
     * @return
     */
    Role select(Role role) throws Exception;


    /**
     * 删除角色
     * @param ids
     * @return
     */
    Integer deletes(List<String> ids) throws Exception;

    /**
     * 更新角色
     * @param role
     * @return
     */
    Integer updateById(Role role) throws Exception;

    /**
     * 查询角色
     * @param id
     * @return
     */
    Role getById(Long id) throws Exception;

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
    Result deleteList(List<String> ids);
}

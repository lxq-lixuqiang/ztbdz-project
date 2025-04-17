package com.ztbdz.user.service;

import com.ztbdz.user.pojo.Role;

public interface RoleService {

    /**
     * 新增角色
     * @param role
     * @return
     */
    Integer insert(Role role) throws Exception;

}

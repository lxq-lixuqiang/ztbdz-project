package com.ztbdz.user.service.impl;

import com.ztbdz.user.mapper.RoleMapper;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
}

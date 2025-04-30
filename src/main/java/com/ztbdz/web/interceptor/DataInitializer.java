package com.ztbdz.web.interceptor;

import com.ztbdz.user.pojo.Account;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.RoleService;
import com.ztbdz.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        try{
            //初始化 角色
            if(roleService.countByTypeAndTypeName(null,"admin","管理员")==0) roleService.insert(new Role("admin","管理员","管理员",0));
            if(roleService.countByTypeAndTypeName(null,"tenderee","招标方")==0) roleService.insert(new Role("tenderee","招标方","招标方",0));
            if(roleService.countByTypeAndTypeName(null,"bidder","投标方")==0) roleService.insert(new Role("bidder","投标方","投标方",0));
            if(roleService.countByTypeAndTypeName(null,"expert","专家")==0) roleService.insert(new Role("expert","专家","专家",0));

            //初始化 管理员
            if(userService.selectMember(null,"admin")==null){
                User user = new User();
                user.setUsername("admin");
                user.setPassword("123456");
                Member member = new Member();
                member.setName("admin");
                member.setRole(new Role("admin","管理员","管理员",0));
                Account account = new Account();
                account.setAccountName("单位");
                member.setAccount(account);
                user.setMember(member);
                userService.create(user,"-1");
            }
        }catch (Exception e) {
            log.error("初始化数据异常！", e);
        }
    }
}

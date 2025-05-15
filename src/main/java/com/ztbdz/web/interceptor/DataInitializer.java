package com.ztbdz.web.interceptor;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.*;
import com.ztbdz.user.service.MenuAuthorizeService;
import com.ztbdz.user.service.RoleService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.web.config.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DataInitializer {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private MenuAuthorizeService menuAuthorizeService;
    @Autowired
    private RedisTemplate redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        try{
            //初始化 删除缓存
            redisTemplate.delete(SystemConfig.REDIS_ROLE_AND_MENU); // 角色和菜单关联信息
            redisTemplate.delete(SystemConfig.REDIS_ALL_MENU); // 全部菜单信息

            //初始化 角色
            if(roleService.countByTypeAndTypeName(null,"admin","管理员")==0) roleService.insert(new Role("admin","管理员","管理员",0));
            if(roleService.countByTypeAndTypeName(null,"tenderee","招标方")==0) roleService.insert(new Role("tenderee","招标方","招标方",0));
            if(roleService.countByTypeAndTypeName(null,"bidder","投标方")==0) roleService.insert(new Role("bidder","投标方","投标方",0));
            if(roleService.countByTypeAndTypeName(null,"expert","专家")==0) roleService.insert(new Role("expert","专家","专家",0));
            if(roleService.countByTypeAndTypeName(null,"finance","财务")==0) roleService.insert(new Role("finance","财务","财务",0));

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

            // 初始化菜单权限 并 关联角色
            PageInfo<MenuAuthorize> menuPage =  menuAuthorizeService.selectList(1,1,new MenuAuthorize());
            if(menuPage.getTotal()<=0){
                List<Role> roleList = roleService.selectList(new Role());
                Map<String,Long> roleMap = new HashMap();
                for(Role role :roleList){
                    roleMap.put(role.getType(),role.getId());
                }
                Map<String,List<MenuAuthorize>> saveDataMap = new HashMap();
                // 管理员
                List<MenuAuthorize> adminMenuList = new ArrayList();
                saveDataMap.put("admin",adminMenuList);
                // 招标方
                List<MenuAuthorize> tendereeMenuList = new ArrayList();
                tendereeMenuList.add(new MenuAuthorize("招标方管理页面","tenderee","/tenderee.html"));
                saveDataMap.put("tenderee",tendereeMenuList);
                // 投标方
                List<MenuAuthorize> bidderMenuList = new ArrayList();
                bidderMenuList.add(new MenuAuthorize("投标方管理页面","bider","/bider.html"));
                bidderMenuList.add(new MenuAuthorize("投标方报名详情页","application","/application.html"));
                saveDataMap.put("bidder",bidderMenuList);
                // 专家
                List<MenuAuthorize> expertMenuList = new ArrayList();
                expertMenuList.add(new MenuAuthorize("专家管理页面","expert","/expert.html"));
                expertMenuList.add(new MenuAuthorize("专家评审页面","Bid evaluation","/Bid evaluation.html"));
                saveDataMap.put("expert",expertMenuList);
                // 财务
                List<MenuAuthorize> treasurerMenuList = new ArrayList();
                treasurerMenuList.add(new MenuAuthorize("财务管理页面","finance","/finance.html"));
                saveDataMap.put("finance",treasurerMenuList);

                for(String key :saveDataMap.keySet()){
                    List<MenuAuthorize> menuList = saveDataMap.get(key);
                    List<RoleRelatedAuthorize> roleRelatedAuthorizeList = new ArrayList();
                    for(MenuAuthorize menuAuthorize :menuList){
                        MenuAuthorize menuAuthorize1 = menuAuthorizeService.select(menuAuthorize);
                        if(menuAuthorize1==null){
                            menuAuthorizeService.insert(menuAuthorize);
                            menuAuthorize1 = menuAuthorize;
                        }
                        RoleRelatedAuthorize roleRelatedAuthorize = new RoleRelatedAuthorize(roleMap.get(key),menuAuthorize1.getId());
                        roleRelatedAuthorizeList.add(roleRelatedAuthorize);
                    }
                    roleService.allocation(roleRelatedAuthorizeList);
                }
            }

        }catch (Exception e) {
            log.error("初始化数据异常！", e);
        }
    }
}

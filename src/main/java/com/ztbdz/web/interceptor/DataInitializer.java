package com.ztbdz.web.interceptor;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.*;
import com.ztbdz.user.service.ExpertInfoService;
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
    @Autowired
    private ExpertInfoService expertInfoService;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        try{
            //初始化 删除缓存
            redisTemplate.delete(SystemConfig.REDIS_ROLE_AND_MENU); // 角色和菜单关联信息
            redisTemplate.delete(SystemConfig.REDIS_ALL_MENU); // 全部菜单信息

            //初始化 角色
            if(redisTemplate.opsForValue().get("initRole") == null){
                if(roleService.countByTypeAndTypeName(null,"admin","管理员")==0) roleService.insert(new Role("admin","管理员","管理员",0));
                if(roleService.countByTypeAndTypeName(null,"tenderee","招标方")==0) roleService.insert(new Role("tenderee","招标方","招标方",0));
                if(roleService.countByTypeAndTypeName(null,"bidder","投标方")==0) roleService.insert(new Role("bidder","投标方","投标方",0));
                if(roleService.countByTypeAndTypeName(null,"expert","专家")==0) roleService.insert(new Role("expert","专家","专家",0));
                if(roleService.countByTypeAndTypeName(null,"finance","财务")==0) roleService.insert(new Role("finance","财务","财务",0));
                if(roleService.countByTypeAndTypeName(null,"expertSelect","抽取专家员")==0) roleService.insert(new Role("expertSelect","抽取专家员","抽取专家员",0));
                if(roleService.countByTypeAndTypeName(null,"manage","项目经理")==0) roleService.insert(new Role("manage","项目经理","项目经理",0));
                redisTemplate.opsForValue().set("initRole",true);
            }

            //初始化 管理员
            if(redisTemplate.opsForValue().get("initAdmin") == null){
                if(userService.selectMember(null,"admin")==null){ // 管理员
                    User user = new User();
                    user.setUsername("admin");
                    user.setPassword("123456");
                    Member member = new Member();
                    member.setName("admin");
                    member.setRole(new Role("admin","管理员","管理员",0));
                    Account account = new Account();
                    account.setAccountName("管理员单位");
                    member.setAccount(account);
                    user.setMember(member);
                    userService.create(user,"-1");
                }
                if(userService.selectMember(null,"zj")==null){ // 专家组长
                    Member member = new Member();
                    member.setPhone("zj");
                    member.setName("zj");
                    member.setRole(new Role("expert","专家","专家",0));
                    Account account = new Account();
                    account.setAccountName("专家组长单位");
                    member.setAccount(account);
                    ExpertInfo expertInfo = new ExpertInfo();
                    expertInfo.setIsAdmin(1);
                    expertInfo.setIsCheck(1);
                    expertInfo.setMember(member);
                    expertInfoService.create(expertInfo);
                }
                redisTemplate.opsForValue().set("initAdmin",true);
            }

            // 初始化菜单权限 并 关联角色
            // 清空 menu_authorize和role_related_authorize 表数据才能进行初始化数据
            if(redisTemplate.opsForValue().get("initMenu") == null){
                PageInfo<MenuAuthorize> menuPage =  menuAuthorizeService.selectList(1,1,new MenuAuthorize());
                if(menuPage.getTotal()<=0){
                    List<Role> roleList = roleService.selectList(new Role());
                    Map<String,Long> roleMap = new HashMap();
                    for(Role role :roleList){
                        roleMap.put(role.getType(),role.getId());
                    }
                    Map<String,List<MenuAuthorize>> saveDataMap = new HashMap();

                    // 菜单
                    MenuAuthorize audit = new MenuAuthorize("项目审查页面","audit","/audit.html");
                    MenuAuthorize tenderee = new MenuAuthorize("招标方管理页面","tenderee","/tenderee.html");
                    MenuAuthorize bider = new MenuAuthorize("投标方管理页面","bider","/bider.html");
                    MenuAuthorize application = new MenuAuthorize("投标方报名详情页","application","/Application.html");
                    MenuAuthorize project_d = new MenuAuthorize("项目详情","project-d","/project-d.html");
                    MenuAuthorize expert = new MenuAuthorize("专家管理页面","expert","/expert.html");
                    MenuAuthorize Bid_evaluation = new MenuAuthorize("专家评审页面","Bid evaluation","/Bid evaluation.html");
                    MenuAuthorize finance = new MenuAuthorize("财务管理页面","finance","/finance.html");
                    MenuAuthorize expertSelect = new MenuAuthorize("抽取专家页面","expertSelect","/expert-select.html");
                    MenuAuthorize manage = new MenuAuthorize("项目经理工作台","manage","/manage.html");
                    MenuAuthorize printExpert = new MenuAuthorize("专家库管理","print-expert","/print-expert.html");

                    // 角色与菜单关联
                    // 管理员
                    List<MenuAuthorize> adminMenuList = new ArrayList();
                    adminMenuList.add(audit);
                    saveDataMap.put("admin",adminMenuList);

                    // 招标方
                    List<MenuAuthorize> tendereeMenuList = new ArrayList();
                    tendereeMenuList.add(tenderee);
                    saveDataMap.put("tenderee",tendereeMenuList);

                    // 投标方
                    List<MenuAuthorize> bidderMenuList = new ArrayList();
                    bidderMenuList.add(bider);
                    bidderMenuList.add(application);
                    bidderMenuList.add(project_d);
                    saveDataMap.put("bidder",bidderMenuList);

                    // 专家
                    List<MenuAuthorize> expertMenuList = new ArrayList();
                    expertMenuList.add(expert);
                    expertMenuList.add(Bid_evaluation);
                    saveDataMap.put("expert",expertMenuList);

                    // 财务
                    List<MenuAuthorize> treasurerMenuList = new ArrayList();
                    treasurerMenuList.add(finance);
                    saveDataMap.put("finance",treasurerMenuList);

                    // 抽取专家员
                    List<MenuAuthorize> extractExpertMenuList = new ArrayList();
                    extractExpertMenuList.add(expertSelect);
                    saveDataMap.put("expertSelect",extractExpertMenuList);

                    // 项目经理
                    List<MenuAuthorize> projectManagerMenuList = new ArrayList();
                    projectManagerMenuList.add(manage);
                    projectManagerMenuList.add(expertSelect);
                    projectManagerMenuList.add(finance);
                    projectManagerMenuList.add(tenderee);
                    projectManagerMenuList.add(project_d);
                    projectManagerMenuList.add(expert);
                    projectManagerMenuList.add(audit);
                    projectManagerMenuList.add(printExpert);
                    saveDataMap.put("manage",projectManagerMenuList);


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
                redisTemplate.opsForValue().set("initMenu",true);
            }

        }catch (Exception e) {
            log.error("初始化数据异常！", e);
        }
    }
}

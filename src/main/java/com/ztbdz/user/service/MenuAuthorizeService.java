package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.MenuAuthorize;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface MenuAuthorizeService {

    /**
     * 创建权限
     * @param menuAuthorize
     * @return
     */
    Integer insert(MenuAuthorize menuAuthorize) throws Exception;

    /**
     * 删除权限
     * @param id
     * @return
     */
    Integer delete(Long id) throws Exception;

    /**
     * 更新权限
     * @param menuAuthorize
     * @return
     */
    Integer updateById(MenuAuthorize menuAuthorize) throws Exception;

    /**
     * 查询权限
     * @param menuAuthorize
     * @return
     */
    MenuAuthorize select(MenuAuthorize menuAuthorize) throws Exception;


    /**
     * 查询权限
     * @param menuAuthorize
     * @return
     */
    List<MenuAuthorize> selectList(MenuAuthorize menuAuthorize) throws Exception;

    /**
     * 查询权限列表
     * @param page
     * @param size
     * @param menuAuthorize
     * @return
     */
    PageInfo<MenuAuthorize> selectList(Integer page, Integer size, MenuAuthorize menuAuthorize) throws Exception;

    /**
     * 查询权限信息
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 查询权限列表
     * @param page
     * @param size
     * @param menuAuthorize
     * @return
     */
    Result list(Integer page,Integer size,MenuAuthorize menuAuthorize);


    /**
     * 修改权限
     * @param menuAuthorize
     * @return
     */
    Result update(MenuAuthorize menuAuthorize);

    /**
     * 权限数量
     * @param menuAuthorize
     * @return
     */
    Integer count(MenuAuthorize menuAuthorize) throws Exception;


    /**
     * 创建权限
     * @param menuAuthorize
     * @return
     */
    Result create(MenuAuthorize menuAuthorize);

    /**
     * 多id查询权限
     * @param ids
     * @return
     */
    List<MenuAuthorize> selectByIds(List<Long> ids) throws Exception;


    /**
     * 查询菜单数量
     * @param sign
     * @return
     */
    Integer countBySign(String id,String sign) throws Exception;
}

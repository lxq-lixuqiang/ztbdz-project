package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.web.util.Result;

import java.util.List;

public interface MemberService {


    /**
     * 创建人员
     * @param member
     * @return
     */
    Integer insert(Member member) throws Exception;

    /**
     * 删除人员
     * @param id
     * @return
     */
    Integer delete(Long id) throws Exception;

    /**
     * 更新人员
     * @param member
     * @return
     */
    Integer updateById(Member member) throws Exception;

    /**
     * 查询人员
     * @param id
     * @return
     */
    Member getById(Long id) throws Exception;

    /**
     * 查询多人员
     * @param page
     * @param size
     * @param member
     * @return
     */
    PageInfo<Member> selectList(Integer page, Integer size,Member member) throws Exception;

    /**
     * 查询人员信息
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 查询人员列表
     * @param page
     * @param size
     * @param member
     * @return
     */
    Result list(Integer page,Integer size,Member member);


    /**
     * 更新人员
     * @param user
     * @return
     */
    Result update(User user);


    /**
     * 批量删除人员
     * @param ids
     * @return
     */
    Result deleteList(List<Long> ids);


    /**
     * 删除角色
     * @param ids
     * @return
     */
    Integer deletes(List<Long> ids) throws Exception;
}

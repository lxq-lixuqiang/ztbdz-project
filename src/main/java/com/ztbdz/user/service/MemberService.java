package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.web.util.Result;

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
     * @param member
     * @return
     */
    Result update(Member member);

}

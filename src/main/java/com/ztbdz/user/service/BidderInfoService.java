package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.web.util.Result;

public interface BidderInfoService {


    /**
     * 查询投标方根据人员id
     * @param memberId
     * @return
     */
    Result getMemberInfo(Long memberId);

    /**
     * 查询投标方
     * @param id
     * @return
     */
    Result getMemberAndAccount(Long id);

    /**
     * 更新投标方
     * @param bidderInfo
     * @return
     */
    Result update(BidderInfo bidderInfo);

    /**
     * 审核投标方
     * @param bidderInfo
     * @return
     */
    Result review(BidderInfo bidderInfo);

    /**
     * 注册投标方
     * @param bidderInfo
     * @return
     */
    Result register(BidderInfo bidderInfo);

    /**
     * 查询投标方
     * @param memberId
     * @return
     */
    BidderInfo selectByMemberId(Long memberId) throws Exception;

    /**
     * 更新投标方
     * @param bidderInfo
     * @return
     */
    Integer updateById(BidderInfo bidderInfo) throws Exception;

    /**
     * 新增投标方
     * @param bidderInfo
     * @return
     */
    Integer create(BidderInfo bidderInfo) throws Exception;

    /**
     * 查询是否有重复法人身份证号
     * @param idCard
     * @return
     */
    Integer countIdCard(String idCard) throws Exception;

    /**
     * 添加投标方
     * @param bidderInfo
     * @return
     */
    Integer insert(BidderInfo bidderInfo) throws Exception;

    /**
     * 查询投标方
     * @param id
     * @return
     */
    BidderInfo get(Long id) throws Exception;

    /**
     * 查询投标方
     * @param id
     * @return
     */
    BidderInfo getOther(Long id) throws Exception;

    /**
     * 查询多投标方
     * @param page
     * @param size
     * @param bidderInfo
     * @return
     */
    Result list(Integer page,Integer size,BidderInfo bidderInfo);

    /**
     * 查询多投标方
     * @param page
     * @param size
     * @param bidderInfo
     * @return
     */
    PageInfo<BidderInfo> page(Integer page, Integer size, BidderInfo bidderInfo) throws Exception;

}

package com.ztbdz.user.service;

import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.web.util.Result;

public interface BidderInfoService {


    /**
     * 查询投标方
     * @param memberId
     * @return
     */
    Result getMemberInfo(Long memberId);

    /**
     * 更新投标方
     * @param bidderInfo
     * @return
     */
    Result update(BidderInfo bidderInfo);

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
     * 添加投标方
     * @param bidderInfo
     * @return
     */
    Integer insert(BidderInfo bidderInfo) throws Exception;


}

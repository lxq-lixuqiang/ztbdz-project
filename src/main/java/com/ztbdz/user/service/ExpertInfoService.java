package com.ztbdz.user.service;

import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.user.pojo.ExpertInfo;
import com.ztbdz.web.util.Result;

public interface ExpertInfoService {


    /**
     * 查询专家
     * @param memberId
     * @return
     */
    Result getMemberInfo(Long memberId);

    /**
     * 更新专家
     * @param expertInfo
     * @return
     */
    Result update(ExpertInfo expertInfo);

    /**
     * 查询专家
     * @param memberId
     * @return
     */
    ExpertInfo selectByMemberId(Long memberId) throws Exception;

    /**
     * 更新专家
     * @param expertInfo
     * @return
     */
    Integer updateById(ExpertInfo expertInfo) throws Exception;

    /**
     * 添加专家
     * @param expertInfo
     * @return
     */
    Integer insert(ExpertInfo expertInfo) throws Exception;

}

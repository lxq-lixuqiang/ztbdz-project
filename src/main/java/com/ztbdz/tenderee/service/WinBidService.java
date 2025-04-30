package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.WinBid;

import java.util.List;

public interface WinBidService {

    /**
     * 添加中标人
     * @param winBid
     * @return
     */
    Integer insert(WinBid winBid) throws Exception;

    /**
     * 根据中标id删除中标信息
     * @param winBidId
     * @return
     * @throws Exception
     */
    Integer delete(Long winBidId) throws Exception;


    /**
     * 查询人员列表
     * @param memberId
     * @param winBidId
     * @return
     * @throws Exception
     */
    List<WinBid> selectList(Long memberId,Long winBidId) throws Exception;
}

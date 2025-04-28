package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.WinBid;

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
}

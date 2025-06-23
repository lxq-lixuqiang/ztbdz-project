package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.WinBidMapper;
import com.ztbdz.tenderee.pojo.WinBid;
import com.ztbdz.tenderee.service.WinBidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Slf4j
@Service
public class WinBidServiceImpl implements WinBidService {
    @Autowired
    private WinBidMapper winBidMapper;


    @Override
    public Integer insert(WinBid winBid) throws Exception{
        return winBidMapper.insert(winBid);
    }

    @Override
    public Integer delete(Long winBidId) throws Exception {
        QueryWrapper<WinBid> queryWrapper = new QueryWrapper();
        queryWrapper.eq("win_bid_id",winBidId);
        return winBidMapper.delete(queryWrapper);
    }

    @Override
    public List<WinBid> selectList(Long memberId, Long winBidId) throws Exception {
        QueryWrapper<WinBid> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(memberId)) queryWrapper.eq("member_id",memberId);
        if(!StringUtils.isEmpty(winBidId)) queryWrapper.eq("win_bid_id",winBidId);
        return winBidMapper.selectList(queryWrapper);
    }
}

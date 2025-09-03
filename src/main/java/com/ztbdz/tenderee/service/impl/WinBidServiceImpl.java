package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.WinBidMapper;
import com.ztbdz.tenderee.pojo.WinBid;
import com.ztbdz.tenderee.service.WinBidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Slf4j
@Service
@CacheConfig(cacheNames = "winBid")
public class WinBidServiceImpl implements WinBidService {
    @Autowired
    private WinBidMapper winBidMapper;


    @CacheEvict(cacheNames = "winBid",allEntries = true)
    @Override
    public Integer insert(WinBid winBid) throws Exception{
        return winBidMapper.insert(winBid);
    }

    @CacheEvict(cacheNames = "winBid",allEntries = true)
    @Override
    public Integer delete(Long winBidId) throws Exception {
        QueryWrapper<WinBid> queryWrapper = new QueryWrapper();
        queryWrapper.eq("win_bid_id",winBidId);
        return winBidMapper.delete(queryWrapper);
    }

    @Cacheable
    @Override
    public List<WinBid> selectList(Long memberId, Long winBidId) throws Exception {
        QueryWrapper<WinBid> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(memberId)) queryWrapper.eq("member_id",memberId);
        if(!StringUtils.isEmpty(winBidId)) queryWrapper.eq("win_bid_id",winBidId);
        return winBidMapper.selectList(queryWrapper);
    }
}

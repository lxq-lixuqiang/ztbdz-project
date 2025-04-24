package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.TenderMapper;
import com.ztbdz.tenderee.pojo.Tender;
import com.ztbdz.tenderee.service.TenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TenderServiceImpl implements TenderService {
    @Autowired
    private TenderMapper tenderMapper;

    @Override
    public Integer insert(Tender tender) {
        return tenderMapper.insert(tender);
    }

    @Override
    public List<Tender> selectListByProjectId(Long projectId) throws Exception {
        QueryWrapper<Tender> queryWrapper = new QueryWrapper();
        queryWrapper.eq("project_id", projectId.toString());
        return tenderMapper.selectList(queryWrapper);
    }

    @Override
    public Integer deleteByProjectId(Long projectId) throws Exception {
        QueryWrapper<Tender> queryWrapper = new QueryWrapper();
        queryWrapper.eq("project_id", projectId);
        return tenderMapper.delete(queryWrapper);
    }
}

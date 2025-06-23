package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.SpecialityMapper;
import com.ztbdz.tenderee.pojo.Speciality;
import com.ztbdz.tenderee.service.SpecialityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SpecialityServiceImpl implements SpecialityService {
    @Autowired
    private SpecialityMapper specialityMapper;


    @Override
    public Integer insert(Speciality speciality) throws Exception {
        return specialityMapper.insert(speciality);
    }

    @Override
    public Integer deleteByReviewInfoId(Long reviewInfoId) throws Exception {
        QueryWrapper<Speciality> queryWrapper = new QueryWrapper();
        queryWrapper.eq("review_info_id",reviewInfoId);
        return specialityMapper.delete(queryWrapper);
    }
}

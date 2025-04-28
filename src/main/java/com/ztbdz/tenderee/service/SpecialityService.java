package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.Speciality;

public interface SpecialityService {

    /**
     * 保存专业要求
     * @param speciality
     * @return
     * @throws Exception
     */
    Integer insert(Speciality speciality) throws Exception;

    /**
     * 根据评审信息id删除专业要求
     * @param reviewInfoId
     * @return
     * @throws Exception
     */
    Integer deleteByReviewInfoId(Long reviewInfoId) throws Exception;
}

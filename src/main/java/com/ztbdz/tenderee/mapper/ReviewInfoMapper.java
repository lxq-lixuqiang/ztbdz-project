package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.ReviewInfo;
import com.ztbdz.tenderee.pojo.Speciality;
import com.ztbdz.user.pojo.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewInfoMapper extends BaseMapper<ReviewInfo> {

    List<ReviewInfo> selectByProjectName(@Param("projectName") String projectName);


    List<Member> randomExpert(@Param("hideExperts") String[] hideExperts,
                              @Param("hideAccounts") String[] hideAccounts,
                              @Param("speciality") Speciality speciality);
}

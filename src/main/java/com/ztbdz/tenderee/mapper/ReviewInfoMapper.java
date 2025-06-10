package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.ReviewInfo;
import com.ztbdz.tenderee.pojo.Speciality;
import com.ztbdz.user.pojo.ExpertInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewInfoMapper extends BaseMapper<ReviewInfo> {

    List<ReviewInfo> selectByProjectName(@Param("projectName") String projectName,
                                         @Param("ids") List<Long> ids);


    List<ExpertInfo> randomExpert(@Param("hideExperts") List<String> hideExperts,
                                  @Param("hideAccounts") List<String> hideAccounts,
                                  @Param("planNum") Integer planNum,
                                  @Param("speciality") Speciality speciality);
}

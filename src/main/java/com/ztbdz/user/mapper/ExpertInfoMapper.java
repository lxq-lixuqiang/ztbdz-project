package com.ztbdz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.user.pojo.ExpertInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertInfoMapper extends BaseMapper<ExpertInfo> {

    List<ExpertInfo> selectMember(@Param("expertInfo") ExpertInfo expertInfo);

    List<ExpertInfo> selectExpertTo(@Param("expertIds") List<Long> expertIds);


}

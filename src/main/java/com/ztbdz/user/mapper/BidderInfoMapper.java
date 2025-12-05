package com.ztbdz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.user.pojo.BidderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidderInfoMapper extends BaseMapper<BidderInfo> {

    List<BidderInfo> selectMember(@Param("bidderInfo") BidderInfo bidderInfo);

    BidderInfo getOther(@Param("id")Long id);
}

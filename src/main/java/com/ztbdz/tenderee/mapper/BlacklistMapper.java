package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.Blacklist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistMapper extends BaseMapper<Blacklist> {

    Integer verifyBlacklist(@Param("landlordName")String landlordName,@Param("bidderName")String bidderName,@Param("type")String type);
}

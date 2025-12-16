package com.ztbdz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.user.pojo.RetrievePassword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RetrievePasswordMapper extends BaseMapper<RetrievePassword> {

    RetrievePassword selectBidderInfo(@Param("id")Long id);
}

package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.Tenderee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TendereeMapper extends BaseMapper<Tenderee> {

    Tenderee selectProject(@Param("id")Long id);
}

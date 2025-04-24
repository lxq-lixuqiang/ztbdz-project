package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.Tenderee;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TendereeMapper extends BaseMapper<Tenderee> {

    Tenderee selectProject(@Param("id")Long id);
}

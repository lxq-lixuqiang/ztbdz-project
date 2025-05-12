package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.Tenderee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TendereeMapper extends BaseMapper<Tenderee> {

    Tenderee selectProject(@Param("id")Long id);

    List<Tenderee> selectListProject(Tenderee tenderee);

}

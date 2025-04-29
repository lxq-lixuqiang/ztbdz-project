package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRegisterMapper extends BaseMapper<ProjectRegister> {

    List<ProjectRegister> selectByCountProjectId();

    List<ProjectRegister> selectListById(@Param("id") Long id,@Param("notIds") List<Long> notIds);

    List<ProjectRegister> selectMemberProjectByIds(@Param("ids") List<Long> ids);

}

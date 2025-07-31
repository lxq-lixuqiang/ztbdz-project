package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRegisterMapper extends BaseMapper<ProjectRegister> {

    List<ProjectRegister> selectByCountProjectId(@Param("project")Project project,@Param("memberId")Long memberId,@Param("state")Integer state);

    List<ProjectRegister> selectListById(@Param("id") Long id,@Param("notIds") List<Long> notIds);

    List<ProjectRegister> selectMemberProjectByIds(@Param("ids") List<Long> ids);

    List<ProjectRegister> selectProjectByProjectIds(@Param("projectIds") List<Long> projectIds);

    List<ProjectRegister> getProject(@Param("projectId") Long projectId,@Param("state") Integer state);

    ProjectRegister selectProjectAndMemberById(Long id);

    List<ProjectRegister> selectInvoice(Project project);

}

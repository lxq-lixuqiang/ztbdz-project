package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.pojo.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMapper extends BaseMapper<Project> {

    List<Project> listAvailable(@Param("project")Project project, @Param("memberId")Long memberId,@Param("state")Integer state);

    List<Project> runProject(@Param("project")Project project, @Param("memberId")Long memberId,@Param("state")Integer state);

    List<Project> reviewEndProject(Project project);

    List<Project> expertProject(@Param("project")Project project, @Param("memberId")Long memberId,@Param("state")Integer state);

    List<Project> extractProjectList(@Param("state")Integer state,@Param("project")Project project);

    List<Project> selectByIds(@Param("ids")List<Long> ids,@Param("memberId")Long memberId);

    List<Project> selectByIds2(@Param("ids")List<Long> ids);

    List<Project> selectByIds3(@Param("ids")List<Long> ids,@Param("sort")String sort);
}

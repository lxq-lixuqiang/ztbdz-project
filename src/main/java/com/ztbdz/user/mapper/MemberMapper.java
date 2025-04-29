package com.ztbdz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.user.pojo.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberMapper extends BaseMapper<Member> {

    Member selectRoleAndAccount(@Param("id")Long id);
}

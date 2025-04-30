package com.ztbdz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

    User selectMember(@Param("id")Long id,@Param("username")String username);
}

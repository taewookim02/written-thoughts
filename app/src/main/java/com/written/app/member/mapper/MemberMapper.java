package com.written.app.member.mapper;

import com.written.app.member.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {
    @Select("SELECT * FROM MEMBER WHERE ID = #{id} AND PWD = #{pwd}")
    MemberVo login(MemberVo vo);
}

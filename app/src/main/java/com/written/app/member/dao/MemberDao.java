package com.written.app.member.dao;

import com.written.app.member.mapper.MemberMapper;
import com.written.app.member.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberDao {
    private final MemberMapper mapper;
    public MemberVo login(MemberVo vo) {
        return mapper.login(vo);
    }
}

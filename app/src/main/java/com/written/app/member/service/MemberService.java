package com.written.app.member.service;

import com.written.app.member.dao.MemberDao;
import com.written.app.member.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDao dao;
    public MemberVo login(MemberVo vo) {
        return dao.login(vo);
    }
}

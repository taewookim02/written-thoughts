package com.written.app.member.controller;

import com.written.app.member.service.MemberService;
import com.written.app.member.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

//    @GetMapping("login")
//    public MemberVo login(MemberVo vo) {
//        System.out.println("vo = " + vo);
//        // print member
//        MemberVo loginMemberVo = service.login(vo);
//        return loginMemberVo;
//    }
}

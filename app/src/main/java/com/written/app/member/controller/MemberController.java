package com.written.app.member.controller;

import com.written.app.member.dto.MemberFormDto;
import com.written.app.member.service.MemberService;
import com.written.app.member.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

//    @PostMapping("login")
//    public Map<String, String> login(@RequestBody MemberVo vo) {
//        HashMap<String, String> map = new HashMap<>();
//        // TODO: implement login
//        map.put("message", "ok");
//        map.put("zzz", "zzsdfsdf");
//
//        return map;
//    }
//
//    @PostMapping("signup")
//    public Map<String, String> signUp(@RequestBody MemberFormDto dto) {
//        System.out.println("dto = " + dto);
//
//
//
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("message", "ok");
//        map.put("hello", "wrod");
//
//        return map;
//    }

}

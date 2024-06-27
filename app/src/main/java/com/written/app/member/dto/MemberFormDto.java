package com.written.app.member.dto;

import lombok.Data;

@Data
public class MemberFormDto {
    private String id;
    private String pwd;
    private String confirmPwd;
}

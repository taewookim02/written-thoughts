package com.written.app.member.vo;

import lombok.Data;

@Data
public class MemberVo {
    private String memberNo;
    private String id;
    private String pwd;
    private String nick;
    private String delYn;
    private String createdDate;
}

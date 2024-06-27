package com.written.app.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member {
    @Id // pk
    @GeneratedValue // auto-increment, strategy = ...
    private Integer memberNo;
    private String email;
    private String password;
    private String nick;
    private char delYn;
    private String createdDate;



}

package com.atm.bankaccount_1.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private int id;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String account;
    private int balance; // 잔고
    private Date signUpDate;
}

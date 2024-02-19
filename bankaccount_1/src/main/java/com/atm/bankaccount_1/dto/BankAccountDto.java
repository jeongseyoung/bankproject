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
public class BankAccountDto {
    private String myaccount; // 계좌
    private String opponent_account;
    private String password;

    private int transferFee; // 이체요청
    private int depositFee; // 입금
    private int withdrawalFee; // 출금
    private int balance; // 잔고

    private Date thisDate;
}

package com.atm.bankaccount_1.utils.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    USER_PHONE_ALREADY_EXIST("등록된 전화번호입니다.", HttpStatus.CONFLICT);

    private String message;
    private HttpStatus httpStatus;
}

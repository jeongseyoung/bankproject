package com.atm.bankaccount_1.utils.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    USER_PHONE_ALREADY_EXIST("등록된 전화번호입니다.", HttpStatus.CONFLICT),
    BANKACCOUNT_NOT_FOUND("없는 계좌입니다.", HttpStatus.NOT_FOUND),
    TRANSFER_FAILED("이체 실패", HttpStatus.NOT_IMPLEMENTED),
    PASSWORD_INCORRECT("비밀번호가 일치하지 않음", HttpStatus.UNAUTHORIZED),
    INSUFFICENT_BALANCE("잔고 부족", HttpStatus.INSUFFICIENT_STORAGE);

    private String message;
    private HttpStatus httpStatus;
}

package com.atm.bankaccount_1.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private String message;
    private ErrorCode errorCode;
}

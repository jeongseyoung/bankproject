package com.atm.bankaccount_1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.bankaccount_1.dto.UserDto;
import com.atm.bankaccount_1.service.MyAccountService;

import lombok.RequiredArgsConstructor;

/**
 * MyAccountController
 */
@RestController
@RequiredArgsConstructor
public class MyAccountController {

    private final MyAccountService myAccountService;

    // 내 통장 정보 조회
    @PostMapping("/myaccount")
    public ResponseEntity<UserDto> myaccount(@RequestBody UserDto userDto) {
        return new ResponseEntity<UserDto>(myAccountService.myaccount(userDto), HttpStatus.OK);
    }

}

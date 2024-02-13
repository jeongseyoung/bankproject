package com.atm.bankaccount_1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.bankaccount_1.dto.UserDto;
import com.atm.bankaccount_1.service.SignUpService;

import lombok.RequiredArgsConstructor;

/**
 * signUpController
 */
@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody UserDto userDto) {
        return new ResponseEntity<UserDto>(signUpService.signup(userDto), HttpStatus.OK);
    }
}
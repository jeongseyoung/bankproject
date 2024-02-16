package com.atm.bankaccount_1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.bankaccount_1.dto.BankAccountDto;
import com.atm.bankaccount_1.service.Depo_Withdrawal_Service;

import lombok.RequiredArgsConstructor;

/**
 * Depo_Withdrawal_Controller
 */
@RestController
@RequiredArgsConstructor
public class Depo_Withdrawal_Controller {

    private final Depo_Withdrawal_Service depo_withdrawal_Service;

    // 이체
    @PostMapping("/transfer")
    public ResponseEntity<BankAccountDto> transfer(@RequestBody BankAccountDto bankAccountDto) {
        return new ResponseEntity<BankAccountDto>(
                depo_withdrawal_Service.transfer(bankAccountDto), HttpStatus.OK);
    }

    // 입금
    @PostMapping("/deposit")
    public ResponseEntity<BankAccountDto> deposit(@RequestBody BankAccountDto bankAccountDto) {
        return new ResponseEntity<BankAccountDto>(depo_withdrawal_Service.deposit(bankAccountDto),
                HttpStatus.OK);
    }

    // 출금
    @PostMapping("/withdrawal")
    public ResponseEntity<BankAccountDto> withdrawal(@RequestBody BankAccountDto bankAccountDto) {
        System.out.println("bankAccountDto: " + bankAccountDto.getMyaccount());
        return new ResponseEntity<BankAccountDto>(depo_withdrawal_Service.withdrawal(bankAccountDto),
                HttpStatus.OK);
    }
}
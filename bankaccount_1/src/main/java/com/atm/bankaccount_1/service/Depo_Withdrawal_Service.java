package com.atm.bankaccount_1.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atm.bankaccount_1.dto.BankAccountDto;
import com.atm.bankaccount_1.entity.BankAccountEntity;
import com.atm.bankaccount_1.entity.UserEntity;
import com.atm.bankaccount_1.repository.BankAccountRepository;
import com.atm.bankaccount_1.repository.UserRepository;
import com.atm.bankaccount_1.utils.exception.CustomException;
import com.atm.bankaccount_1.utils.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Depo_Withdrawal_Service {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    // transfer 이체
    @Transactional(rollbackFor = {
            Exception.class, CustomException.class
    })
    public BankAccountDto transfer(BankAccountDto bankAccountDto) {
        // A = 나, B = 상대
        BankAccountEntity person_A = findAccount_BankAccountEntity(bankAccountDto.getMyaccount());
        BankAccountEntity person_B = findAccount_BankAccountEntity(bankAccountDto.getOpponent_account());

        UserEntity A = findAccount_UserEntity(bankAccountDto.getMyaccount());
        UserEntity B = findAccount_UserEntity(bankAccountDto.getOpponent_account());

        try {
            if (checkPw(bankAccountDto.getPassword(), person_A.getPassword())) {
                int person_A_balance = person_A.getBalance();
                int person_B_balance = person_B.getBalance();

                // A계좌에서 B계좌로 이체, 잔고 부족 시 exception 발생
                if (person_A.getBalance() == 0 || person_A.getBalance() < bankAccountDto.getTransferFee()) {
                    throw new CustomException("잔고 부족", ErrorCode.INSUFFICENT_BALANCE);
                }
                person_A_balance -= bankAccountDto.getTransferFee();
                person_B_balance += bankAccountDto.getTransferFee();

                person_A.setBalance(person_A_balance);
                person_B.setBalance(person_B_balance);
                A.setBalance(person_A_balance);
                B.setBalance(person_B_balance);

                bankAccountRepository.save(person_A);
                bankAccountRepository.save(person_B);
                userRepository.save(A);
                userRepository.save(B);

            } else {
                throw new CustomException("비밀번호가 일치하지 않습니다.", ErrorCode.PASSWORD_INCORRECT);
            }
        } catch (Exception e) {
            throw new CustomException("이체 실패, 사유: " + e.getMessage(), ErrorCode.TRANSFER_FAILED);
        }

        return mapToBankAccountDto(person_A);
    }

    // 출금
    public BankAccountDto withdrawal(BankAccountDto bankAccountDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawal'");
    }

    // find 계좌 - BankAccountEntity
    public BankAccountEntity findAccount_BankAccountEntity(String account) {
        BankAccountEntity bankAccountEntity = bankAccountRepository.findByaccount(account).orElseThrow(() -> {
            throw new CustomException("없는 계좌", ErrorCode.BANKACCOUNT_NOT_FOUND);
        });
        return bankAccountEntity;
    }

    // find 계좌 - UserEntity
    public UserEntity findAccount_UserEntity(String account) {
        UserEntity userEntity = userRepository.findByaccount(account).orElseThrow(() -> {
            throw new CustomException("없는 계좌", ErrorCode.BANKACCOUNT_NOT_FOUND);
        });
        return userEntity;
    }

    // 비번확인
    public boolean checkPw(String pw, String hashedPw) {
        return BCrypt.checkpw(pw, hashedPw);
    }

    public BankAccountDto mapToBankAccountDto(BankAccountEntity bankAccountEntity) {
        BankAccountDto bankAccountDto = BankAccountDto.builder()
                .myaccount(bankAccountEntity.getAccount())
                .transferFee(bankAccountEntity.getTransfer()) // ?
                .depositFee(bankAccountEntity.getDeposit())
                .withdrawalFee(bankAccountEntity.getWithdrawal())
                .build();
        return bankAccountDto;
    }
}
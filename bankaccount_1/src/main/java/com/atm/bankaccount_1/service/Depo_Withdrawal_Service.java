package com.atm.bankaccount_1.service;

import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atm.bankaccount_1.dto.BankAccountDto;
import com.atm.bankaccount_1.entity.BankAccountEntity;
import com.atm.bankaccount_1.entity.BankMainEntity;
import com.atm.bankaccount_1.entity.T_D_W_ListEntity;
import com.atm.bankaccount_1.entity.UserEntity;
import com.atm.bankaccount_1.repository.BankAccountRepository;
import com.atm.bankaccount_1.repository.BankMainRepository;
import com.atm.bankaccount_1.repository.T_D_W_ListRepository;
import com.atm.bankaccount_1.repository.UserRepository;
import com.atm.bankaccount_1.utils.exception.CustomException;
import com.atm.bankaccount_1.utils.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Depo_Withdrawal_Service {

    private final BankMainRepository bankMainRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final T_D_W_ListRepository t_d_w_ListRepository;

    Date today = new Date();

    // transfer 이체
    @Transactional(rollbackFor = {
            Exception.class, CustomException.class
    })
    public BankAccountDto transfer(BankAccountDto bankAccountDto) {
        // A = 나, B = 상대
        BankAccountEntity person_A = findAccount_BankAccountEntity(bankAccountDto.getMyaccount());
        BankAccountEntity person_B = findAccount_BankAccountEntity(bankAccountDto.getOpponent_account());

        T_D_W_ListEntity t_d_w_ListEntity = mapToT_D_W_ListEntity(bankAccountDto);
        // System.out.println("t_d_w_ListEntity: " +
        // t_d_w_ListEntity.getBankAccountEntity().getId());
        t_d_w_ListEntity.setBankAccountEntity(person_A);
        t_d_w_ListEntity.setDateOfTransfer(today);

        // try { //비밀번호 체크 -> 틀리면 exception
        if (checkPw(bankAccountDto.getPassword(), person_A.getPassword())) {
            int person_A_balance = person_A.getBalance();
            int person_B_balance = person_B.getBalance();

            // A계좌에서 B계좌로 이체, 잔고 부족 시 exception 발생
            if (person_A.getBalance() == 0 || person_A.getBalance() < bankAccountDto.getTransferFee()) {
                throw new CustomException("잔고 부족", ErrorCode.INSUFFICENT_BALANCE);
            }
            /*
             * A통장에서 B통장으로 이체이므로
             * A계좌 - 이체요청금액
             * B계좌 + 이체요청금액
             */
            person_A_balance -= bankAccountDto.getTransferFee();
            person_B_balance += bankAccountDto.getTransferFee();

            person_A.setBalance(person_A_balance);
            person_B.setBalance(person_B_balance);

            bankAccountRepository.save(person_A);
            bankAccountRepository.save(person_B);
            t_d_w_ListRepository.save(t_d_w_ListEntity);
        } else {
            throw new CustomException("비밀번호가 일치하지 않습니다.", ErrorCode.PASSWORD_INCORRECT);
        }
        // } catch (Exception e) {
        // throw new CustomException("이체 실패, 사유: " + e.getMessage(),
        // ErrorCode.TRANSFER_FAILED);
        // }

        return mapToBankAccountDto(person_A);
    }

    // 입금 -> 나에게 입금
    @Transactional(rollbackFor = {
            Exception.class, CustomException.class
    })
    public BankAccountDto deposit(BankAccountDto bankAccountDto) {
        BankAccountEntity bankAccountEntity = findAccount_BankAccountEntity(bankAccountDto.getMyaccount());
        BankMainEntity bankMainEntity = bankMainRepository.findById(52).get();

        T_D_W_ListEntity t_d_w_ListEntity = mapToT_D_W_ListEntity(bankAccountDto);
        t_d_w_ListEntity.setBankAccountEntity(bankAccountEntity);
        t_d_w_ListEntity.setDateOfDeposit(today);

        int mybalance = bankAccountEntity.getBalance();
        mybalance += bankAccountDto.getDepositFee();
        Integer totalAmount = bankMainEntity.getTotalAmount();
        totalAmount += bankAccountDto.getDepositFee();

        bankAccountEntity.setBalance(mybalance);
        bankMainEntity.setTotalAmount(totalAmount);

        bankAccountRepository.save(bankAccountEntity);
        bankMainRepository.save(bankMainEntity);
        t_d_w_ListRepository.save(t_d_w_ListEntity);

        return mapToBankAccountDto(bankAccountEntity);
    }

    // 출금
    @Transactional(rollbackFor = {
            Exception.class, CustomException.class
    })
    public BankAccountDto withdrawal(BankAccountDto bankAccountDto) {
        BankAccountEntity bankAccountEntity = findAccount_BankAccountEntity(bankAccountDto.getMyaccount());
        BankMainEntity bankMainEntity = bankMainRepository.findById(52).get();

        T_D_W_ListEntity t_d_w_ListEntity = mapToT_D_W_ListEntity(bankAccountDto);
        t_d_w_ListEntity.setBankAccountEntity(bankAccountEntity);
        t_d_w_ListEntity.setDateOfWithdrawal(today);

        // 비밀번호 체크
        if (checkPw(bankAccountDto.getPassword(), bankAccountEntity.getPassword())) {
            int balance = bankAccountEntity.getBalance();
            Integer totalAmount = bankMainEntity.getTotalAmount();
            // 잔고 확인 후 잔고 부족하면 exception
            if (balance == 0 || balance < bankAccountDto.getWithdrawalFee()) {
                throw new CustomException("잔고 부족", ErrorCode.INSUFFICENT_BALANCE);
            }
            // 내 잔고에서 출금 요청 금액을 빼줌.
            balance -= bankAccountDto.getWithdrawalFee();
            totalAmount -= bankAccountDto.getWithdrawalFee();

            bankAccountEntity.setBalance(balance);
            bankMainEntity.setTotalAmount(totalAmount);
            // userEntity.setBalance(balance);

            bankAccountRepository.save(bankAccountEntity);
            bankMainRepository.save(bankMainEntity);
            t_d_w_ListRepository.save(t_d_w_ListEntity);

        } else {
            throw new CustomException("비밀번호가 일치하지 않습니다.", ErrorCode.PASSWORD_INCORRECT);
        }
        return mapToBankAccountDto(bankAccountEntity);
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
                .transferFee(bankAccountEntity.getTransfer())
                .depositFee(bankAccountEntity.getDeposit())
                .withdrawalFee(bankAccountEntity.getWithdrawal())
                .build();
        return bankAccountDto;
    }

    public T_D_W_ListEntity mapToT_D_W_ListEntity(BankAccountDto bankAccountDto) {
        T_D_W_ListEntity setListEntity = T_D_W_ListEntity.builder()
                .TransferOpponent_account(bankAccountDto.getOpponent_account())
                .ListOfTransfer(bankAccountDto.getTransferFee())
                .ListOfDeposit(bankAccountDto.getDepositFee())
                .ListOfWithdrawal(bankAccountDto.getWithdrawalFee())
                .DateOfTransfer(bankAccountDto.getThisDate())
                .DateOfDeposit(bankAccountDto.getThisDate())
                .DateOfWithdrawal(bankAccountDto.getThisDate())
                .build();
        return setListEntity;
    }

}
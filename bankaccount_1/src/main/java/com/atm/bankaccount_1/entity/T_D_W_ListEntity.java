package com.atm.bankaccount_1.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * T_D_W_ListEntity Transfer, Deposit, Withdrawal
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class T_D_W_ListEntity {
    @Id
    @GeneratedValue
    private int id;

    private int ListOfTransfer;
    private String TransferOpponent_account;
    private Date DateOfTransfer;

    private int ListOfDeposit;
    private Date DateOfDeposit;

    private int ListOfWithdrawal;
    private Date DateOfWithdrawal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BankAccount_id")
    private BankAccountEntity bankAccountEntity;

}
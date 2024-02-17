package com.atm.bankaccount_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atm.bankaccount_1.entity.BankMainEntity;

public interface BankMainRepository extends JpaRepository<BankMainEntity, Integer> {

}

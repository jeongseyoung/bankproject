package com.atm.bankaccount_1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atm.bankaccount_1.entity.BankAccountEntity;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {

    Optional<BankAccountEntity> findByaccount(String account);

}

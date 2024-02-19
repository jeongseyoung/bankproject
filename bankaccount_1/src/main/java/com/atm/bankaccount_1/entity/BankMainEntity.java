package com.atm.bankaccount_1.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * 시스템 총 관리.
 * 관리자 생성.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BankMainEntity {

    @Id
    @GeneratedValue
    private int id;
    private String admin; // admin
    private String password;
    private Integer totalAmount; // 은행 총 자산

    // @JsonIgnore
    @OneToMany(mappedBy = "bankMainEntity", cascade = CascadeType.ALL)
    List<BankAccountEntity> bankAccountEntities = new ArrayList<BankAccountEntity>();

    public void addBankAccountEntity(BankAccountEntity bankAccountEntity) {
        bankAccountEntity.setBankMainEntity(this);
        this.bankAccountEntities.add(bankAccountEntity);
    }

    // @JsonIgnore
    @OneToMany(mappedBy = "bankMainEntity", cascade = CascadeType.ALL)
    List<UserEntity> userEntities = new ArrayList<UserEntity>();

    public void addUserEntity(UserEntity userEntity) {
        userEntity.setBankMainEntity(this);
        this.userEntities.add(userEntity);
    }
}

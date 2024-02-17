package com.atm.bankaccount_1.entity;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

/**
 * userEntity
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
// @Table(name = "")
public class UserEntity {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String account;
    // private int balance; // 잔고
    @CreationTimestamp
    private Date signUpDate;

    // BankAccountEntity에서 private UserEntity *****userEntity***** <<<<<<- mapped by
    // @JsonIgnore
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    List<BankAccountEntity> MyAccountList = new ArrayList<BankAccountEntity>();

    public void addMyAccount(BankAccountEntity bankAccountEntity) {
        bankAccountEntity.setUserEntity(this);
        this.MyAccountList.add(bankAccountEntity);
    }

    // @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankmain_id")
    BankMainEntity bankMainEntity;

}
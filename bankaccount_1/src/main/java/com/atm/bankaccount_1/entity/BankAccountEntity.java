package com.atm.bankaccount_1.entity;

import java.util.ArrayList;
import java.util.List;

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

/**
 * BankEntity
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BankAccountEntity {

    @Id
    @GeneratedValue
    private int id;
    private String account; // 계좌
    private String password;

    private int transfer; // 이체
    private int deposit; // 입금
    private int withdrawal;
    private int balance; // 잔고

    // @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    // @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankmain_id")
    private BankMainEntity bankMainEntity;

    @OneToMany(mappedBy = "bankAccountEntity", cascade = CascadeType.ALL)
    List<T_D_W_ListEntity> t_d_w_ListRepository = new ArrayList<T_D_W_ListEntity>();

    public void addT_D_WList(T_D_W_ListEntity t_d_w_ListEntity) {
        t_d_w_ListEntity.setBankAccountEntity(this);
        this.t_d_w_ListRepository.add(t_d_w_ListEntity);
    }
}
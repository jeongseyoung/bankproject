package com.atm.bankaccount_1.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atm.bankaccount_1.dto.UserDto;
import com.atm.bankaccount_1.entity.BankAccountEntity;
import com.atm.bankaccount_1.entity.UserEntity;
import com.atm.bankaccount_1.repository.BankAccountRepository;
import com.atm.bankaccount_1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * signUpService
 */
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    /*
     * User생성(가입)과 계좌개설.
     * 
     * CheckedException, UnCheckedException 발생시 롤백.
     * 트랜잭션은 RuntimeException(unchecked exception)이 발생하면
     * 롤백 디폴트값으로 설정되어 있음.
     */
    @Transactional(rollbackFor = { Exception.class })
    public UserDto signup(UserDto userDto) {
        String pw = encodePassword(userDto.getPassword()); // 비밀번호 Bcrypt로 암호화
        String account = createAccount();
        UserEntity userEntity = mapToUserEntity(userDto, pw);
        BankAccountEntity bankAccountEntity = mapToBankAccountEntity(userEntity, pw);

        // stream?
        if (!checkAccount(account)) {
            userEntity.setAccount(account);
            bankAccountEntity.setAccount(account);
        } else {
            account = createAccount(); // 이거도 다시 중복 체크해야되는데
            userEntity.setAccount(account);
            bankAccountEntity.setAccount(account);
        }
        bankAccountRepository.save(bankAccountEntity);

        return mapToUserDto(userRepository.save(userEntity));
    }

    /*
     * 계좌생성,
     * 000-00-00000000 각 부분을 랜덤함수를 이용하여 생성 후 합침
     */
    public String createAccount() {
        int phase1 = (int) (Math.random() * (999 - 100)) + 100;
        int phase2 = (int) (Math.random() * (99 - 10)) + 10;
        int phase3 = (int) (Math.random() * (99999999 - 10000000)) + 10000000;

        String account = phase1 + "-" + phase2 + "-" + phase3;

        return account;
    }

    /*
     * 계좌중복 체크
     * true - 중복, false - 생성가능
     */
    public boolean checkAccount(String account) {
        return bankAccountRepository.findByaccount(account).isPresent();
    }

    /*
     * Dto -> Entity
     */
    public UserEntity mapToUserEntity(UserDto userDto, String pw) {

        UserEntity userEntity = UserEntity.builder()
                .name(userDto.getName())
                .password(pw)
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .build();
        return userEntity;
    }

    /*
     * Entity -> Dto
     */
    public UserDto mapToUserDto(UserEntity userEntity) {
        UserDto userDto = UserDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .account(userEntity.getAccount())
                .balance(userEntity.getBalance())
                .signUpDate(userEntity.getSignUpDate())
                .build();
        return userDto;
    }

    public BankAccountEntity mapToBankAccountEntity(UserEntity userEntity, String pw) {
        BankAccountEntity bankAccountEntity = BankAccountEntity.builder()
                .password(pw)
                .deposit(0)
                .withdrawal(0)
                .balance(0)
                .userEntity(userEntity)
                .build();
        return bankAccountEntity;
    }

    // 비밀번호 암호화
    public String encodePassword(String pw) {
        return BCrypt.hashpw(pw, BCrypt.gensalt());
    }
}
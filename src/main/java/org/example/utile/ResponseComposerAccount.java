package org.example.utile;

import org.example.dao.EmailDao;
import org.example.dao.PasswordDao;
import org.example.dto.AccountResponseDto;
import org.example.dto.AccountResponseDtoCompose;
import java.util.List;

public class ResponseComposerAccount {

    private final EmailDao emailDao;
    private final PasswordDao passwordDao;

    public ResponseComposerAccount(EmailDao emailDao, PasswordDao passwordDao) {
        this.emailDao = emailDao;
        this.passwordDao = passwordDao;
    }

    public AccountResponseDtoCompose compose(AccountResponseDto accountResponseDto) {
        String email = "";
        if (accountResponseDto.email() != 0) {
            email = emailDao.getById(accountResponseDto.email()).email();
        }
        String password = "";
        if (accountResponseDto.encryptedPassword() != 0) {
            password = passwordDao.getById(accountResponseDto.encryptedPassword()).encryptedPassword();
        }
        return new AccountResponseDtoCompose(
                accountResponseDto.id(),
                accountResponseDto.serviceName(),
                email,
                accountResponseDto.username(),
                password,
                accountResponseDto.created(),
                accountResponseDto.updated()
        );
    }

    public List<AccountResponseDtoCompose> compose(List<AccountResponseDto> accountResponseDto) {
        return accountResponseDto.stream().map(this::compose).toList();
    }
}

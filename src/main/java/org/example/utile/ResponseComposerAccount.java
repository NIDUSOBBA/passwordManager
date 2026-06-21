package org.example.utile;

import org.example.dao.EmailDao;
import org.example.dao.PasswordDao;
import org.example.dto.AccountResponseDto;
import org.example.dto.AccountResponseDtoCompose;
import org.example.dto.EmailDto;
import org.example.dto.PasswordDto;

public class ResponseComposerAccount {

    private final EmailDao emailDao;
    private final PasswordDao passwordDao;

    public ResponseComposerAccount(EmailDao emailDao, PasswordDao passwordDao) {
        this.emailDao = emailDao;
        this.passwordDao = passwordDao;
    }

    public AccountResponseDtoCompose compose(AccountResponseDto accountResponseDto){
        EmailDto emailDto = emailDao.getById(accountResponseDto.email());
        PasswordDto passwordDto = passwordDao.getById(accountResponseDto.encryptedPassword());
        return new AccountResponseDtoCompose(
                accountResponseDto.id(),
                accountResponseDto.serviceName(),
                emailDto.email(),
                accountResponseDto.username(),
                passwordDto.encryptedPassword()
        );
    }
}

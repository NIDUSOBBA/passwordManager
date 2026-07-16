package org.example.service;

import org.example.dao.AccountDao;
import org.example.dto.AccountCreateDto;
import org.example.dto.AccountResponseDto;
import org.example.dto.AccountResponseDtoCompose;
import org.example.dto.AccountUpdateDto;
import org.example.utile.ResponseComposerAccount;

import java.util.List;


public class AccountService   {

    private final AccountDao accountDao;
    private final ResponseComposerAccount responseComposerAccount;

    public AccountService(AccountDao accountDao, ResponseComposerAccount responseComposerAccount) {
        this.accountDao = accountDao;
        this.responseComposerAccount = responseComposerAccount;
    }

    public void create(AccountCreateDto account) {
        accountDao.crete(account);
    }

    public List<AccountResponseDtoCompose> getAllCompose(){
        return responseComposerAccount.compose(accountDao.getAll());
    }

    public AccountResponseDtoCompose getLastCompose(){
        return responseComposerAccount.compose(accountDao.getLast());
    }

    public void update(AccountUpdateDto account) {
        accountDao.update(account);

    }

    public void deleteById(int id){
        accountDao.deleteById(id);
    }

}

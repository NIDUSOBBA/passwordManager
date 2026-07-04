package org.example.service;

import org.example.dao.PasswordDao;
import org.example.dto.PasswordDto;

import java.util.List;

public class PasswordService   {

    private final PasswordDao passwordDao;

    public PasswordService(PasswordDao passwordDao) {
        this.passwordDao = passwordDao;
    }

    public void create(String password) {
        passwordDao.crete(password);
    }

    public PasswordDto getById(int id){
        return passwordDao.getById(id);
    }
    public List<PasswordDto> getAll(){
        return passwordDao.getAll();
    }

    public void deleteById(int id){
        passwordDao.deleteById(id);
    }

}

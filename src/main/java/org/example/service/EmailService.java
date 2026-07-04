package org.example.service;

import org.example.dao.EmailDao;
import org.example.dto.EmailDto;

import java.util.List;

public class EmailService {

    private final EmailDao emailDao;

    public EmailService(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    public void create(String line) {
        emailDao.create(line);
    }

    public EmailDto getById(int id) {
        return emailDao.getById(id);
    }

    public List<EmailDto> getAll(){
        return emailDao.getAll();
    }

    public void deleteById(int id){
        emailDao.deleteById(id);
    }

}
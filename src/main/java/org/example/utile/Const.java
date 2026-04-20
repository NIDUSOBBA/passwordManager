package org.example.utile;

public class Const {

    public static final String PLUG = "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░";
    public static final String CREATE_ACCOUNT = "Создание аккаунта [serviceName(String) email(id) username(String) password(int)]";
    public static final String UPDATE_ACCOUNT = "Обновление аккаунта [id(int) serviceName(String) username(String) password(int)]";
    public static final String CREATE_PASSWORD = "Создание пароля [password(String)]";
    public static final String UPDATE_PASSWORD = "Обновление пароля [id(int) password(String)]";
    public static final String CREATE_EMAIL = "Создание почты [email(String)]";
    public static final String CONCLUSION = "При [end] завершение работы программы при [h] другие возможные команды";
    public static final String COMMANDS = "Назначения [a] аккаунты [p] пароли [e] почта [ac] - создание.\n[ag id] - достать по id. [ag] - достать все.\n[au] - обновить. (у почты нету) [ad id] - удалить по id.";
    public static final String NOT_EXISTENT_COMMAND = "Не существующая команда для возможных команд";
    public static final String INVALID_FIELD = "Не подходящий тип данных форма: ";

}

package org.example.utile;

public class Const {

    public static final String PLUG = "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░";

    public static final String HELP = "help";
    public static final String END = "end";

    public static final String CREATE_ACCOUNT = "Создание аккаунта [serviceName(String) email(id) username(String) password(int)]";
    public static final String CREATE_PASSWORD = "Создание пароля [password(String)]";
    public static final String CREATE_EMAIL = "Создание почты [email(String)]";

    public static final String UPDATE_ACCOUNT = "Обновление аккаунта [id(int) serviceName(String) username(String) password(int)]";
    public static final String UPDATE_PASSWORD = "Обновление пароля [id(int) password(String)]";

    public static final String CONCLUSION = "При [end] завершение работы программы при [help] другие возможные команды";
    public static final String COMMANDS = "Назначения [a] аккаунты [p] пароли [e] почта \n[ac] - создание.[ag id(int)] - достать по id. \n[ag] - достать все. [au] - обновить. (у почты нету) \n[ad id(int)] - удалить по id.";

    public static final String NOT_EXISTENT_COMMAND = "Не существующая команда для возможных команд";
    public static final String INVALID_FIELD = "Не подходящий тип данных форма: \n";

    public static final String BD_URL = "jdbc:sqlite:password_vault.db";

    public static final int VALID_ACCOUNT_SPLIT_LENGTH = 4;
    public static final int VALID_REQUEST_ID_SPLIT_LENGTH = 2;

}

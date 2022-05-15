package ru.yandex.praktikum.Courier;

import org.apache.commons.lang3.RandomStringUtils;

public class Courier {
    public String login;
    public String password;
    public String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier() {

    }

    public static Courier getAllRandom() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login, password, firstName);
    }

    public static Courier getRandomNoLogin() {
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(null, password, firstName);
    }

    public static Courier getRandomNoPassword() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login, null, firstName);
    }

    @Override
    public String toString() {
        return "Courier {" + "login:" + login + "," + "password:" + password + "," + "firstName:" + firstName + "}";
    }
}

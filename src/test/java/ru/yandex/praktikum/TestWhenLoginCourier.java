package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier.Courier;
import ru.yandex.praktikum.Courier.CourierClient;
import ru.yandex.praktikum.Courier.CourierCredentials;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestWhenLoginCourier {
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    @Description("Такой учетки нет в базе")
    public void loginNonExistentCourier() {
        Courier courier = Courier.getAllRandom();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Прошла авторизация несуществующего пользователя", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Ошибка в тексте", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Авторизация курьера, с ошибкой в поле - пароль")
    public void courierLoginWithoutPass() {
        Courier firstCourier = Courier.getAllRandom();
        courierClient.create(firstCourier);
        ValidatableResponse firstLoginResponse = courierClient.login(CourierCredentials.from(firstCourier));
        courierId = firstLoginResponse.extract().path("id");

        Courier secondCourier = new Courier(firstCourier.login, firstCourier.password + RandomStringUtils.randomAlphabetic(1), firstCourier.firstName);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(secondCourier));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Успешная авторизация с неверным паролем", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Ошибка в тексте", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Авторизация курьера, с ошибкой в поле - логин")
    public void courierLoginWithoutLogin() {
        Courier firstCourier = Courier.getAllRandom();
        courierClient.create(firstCourier);
        ValidatableResponse firstLoginResponse = courierClient.login(CourierCredentials.from(firstCourier));
        courierId = firstLoginResponse.extract().path("id");

        Courier secondCourier = new Courier(firstCourier.login + RandomStringUtils.randomAlphabetic(1), firstCourier.password, firstCourier.firstName);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(secondCourier));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Успешная авторизация с неверным паролем", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Ошибка в тексте", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с пустым паролем")
    @Description("Авторизация курьера, без ввода пароля")
    public void courierLoginFromNullPass() {
        Courier firstCourier = Courier.getAllRandom();
        courierClient.create(firstCourier);
        ValidatableResponse firstLoginResponse = courierClient.login(CourierCredentials.from(firstCourier));
        courierId = firstLoginResponse.extract().path("id");

        Courier secondCourier = new Courier(firstCourier.login, "", firstCourier.firstName);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(secondCourier));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Успешная авторизация с неверным паролем", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Ошибка в тексте", message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с пустым логином")
    @Description("Авторизация курьера, без ввода логина")
    public void courierLoginFromNullLogin() {
        Courier firstCourier = Courier.getAllRandom();
        courierClient.create(firstCourier);
        ValidatableResponse firstLoginResponse = courierClient.login(CourierCredentials.from(firstCourier));
        courierId = firstLoginResponse.extract().path("id");

        Courier secondCourier = new Courier("", firstCourier.password, firstCourier.firstName);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(secondCourier));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Успешная авторизация с неверным паролем", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Ошибка в тексте", message, equalTo("Недостаточно данных для входа"));
    }
}

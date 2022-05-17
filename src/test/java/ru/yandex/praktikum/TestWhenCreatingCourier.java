package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier.Courier;
import ru.yandex.praktikum.Courier.CourierClient;
import ru.yandex.praktikum.Courier.CourierCredentials;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class TestWhenCreatingCourier {
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
    @DisplayName("Регистрация курьера")
    @Description("Создали курьера и прошли авторизацию")
    public void courierBeCreatedWithValidData() {
        Courier courier = Courier.getAllRandom();

        ValidatableResponse isCourierCreated = courierClient.create(courier);
        int statusCode = isCourierCreated.extract().statusCode();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        assertThat("Курьер не создан", statusCode, equalTo(SC_CREATED));
        assertThat("Данные не корректны", courierId, is(not(0)));
    }

    @Test
    @DisplayName("Курьер уже был создан")
    @Description("Проверили, что нельзя создать одинаковых курьеров")
    public void createAnExistingCourier() {
        Courier firstCourier = Courier.getAllRandom();
        Courier secondCourier = new Courier(firstCourier.login, firstCourier.password, firstCourier.firstName);

        ValidatableResponse isFirstCourierCreated = courierClient.create(firstCourier);
        int statusCodeFirstCourier = isFirstCourierCreated.extract().statusCode();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(firstCourier));
        courierId = loginResponse.extract().path("id");
        ValidatableResponse isSecondCourierCreated = courierClient.create(secondCourier);
        int statusCodeSecondCourier = isSecondCourierCreated.extract().statusCode();
        String message = isSecondCourierCreated.extract().path("message");

        assertThat("Первый курьер не создан", statusCodeFirstCourier, equalTo(SC_CREATED));
        assertThat("Повторно создался существующий курьер", statusCodeSecondCourier, equalTo(SC_CONFLICT));
        assertThat("Ошибка в тексте", message, equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверили, что нельзя создать курьера с пустым логином")
    public void creatingCourierWithoutLogin() {
        Courier courier = Courier.getRandomNoLogin();
        ValidatableResponse isCourierCreated = courierClient.create(courier);
        int statusCode = isCourierCreated.extract().statusCode();
        String message = isCourierCreated.extract().path("message");

        assertThat("Недостаточно данных для создания учетной записи", statusCode, is(SC_BAD_REQUEST));
        assertThat("Ошибка в тексте", message, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверили, что нельзя создать курьера с пустым паролем")
    public void creatingCourierWithoutPassword() {
        Courier courier = Courier.getRandomNoPassword();
        ValidatableResponse isCourierCreated = courierClient.create(courier);
        int statusCode = isCourierCreated.extract().statusCode();
        String message = isCourierCreated.extract().path("message");

        assertThat("Недостаточно данных для создания учетной записи", statusCode, is(SC_BAD_REQUEST));
        assertThat("Ошибка в тексте", message, equalTo("Недостаточно данных для создания учетной записи"));
    }
}

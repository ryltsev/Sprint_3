package ru.yandex.praktikum.Courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.BaseSpec;

import static io.restassured.RestAssured.given;

public class CourierClient extends BaseSpec {
    public final String PATH = BASE_URL + "courier/";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(PATH + "login/")
                .then();
    }

    @Step("Удаление курьера")
    public void delete(int courierId) {
        given()
                .spec(getBaseSpec())
                .when()
                .delete(PATH + courierId)
                .then();
    }
}

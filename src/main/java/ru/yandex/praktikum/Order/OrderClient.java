package ru.yandex.praktikum.Order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.BaseSpec;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseSpec {
    private final String ORDER_PATH = "http://qa-scooter.praktikum-services.ru/api/v1/orders/";

    @Step("Создание заказа")
    public int createOrder(Order order) {
        return given().spec(getBaseSpec()).body(order).when().post(ORDER_PATH).then().extract().statusCode();
    }

    @Step("Получение списка заказов")
    public Response getListOrder() {
        return given().spec(getBaseSpec()).get(ORDER_PATH);
    }

    @Step("Получение трек номера")
    public int getOrderTrack(Order order) {
        return given().spec(getBaseSpec()).body(order).when().post(ORDER_PATH).then().extract().path("track");

    }
}

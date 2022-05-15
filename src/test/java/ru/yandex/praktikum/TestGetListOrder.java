package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Order.OrderClient;

import static org.hamcrest.Matchers.notNullValue;

public class TestGetListOrder {
    public OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем, что в списке заказов, есть данные")
    public void getListOrder() {
        Response response = orderClient.getListOrder();
        response.then().body("orders", notNullValue());
    }
}

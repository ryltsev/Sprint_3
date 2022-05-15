package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.Order.Order;
import ru.yandex.praktikum.Order.OrderClient;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

@RunWith(Parameterized.class)
public class TestCreateOrder {
    private final List<String> color;
    private final int expected;
    public OrderClient orderClient;

    public TestCreateOrder(List<String> color, int expected) {
        this.color = color;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] getOrder() {
        return new Object[][]{{List.of("BLACK"), 201}, {List.of("GREY"), 201}, {List.of("BLACK", "GREY"), 201}, {List.of(""), 201}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с переборкой цветов")
    public void createOrder() {
        Order order = new Order(color);
        order.setColor(color);
        int actual = orderClient.createOrder(order);
        int orderId = orderClient.getOrderTrack(order);

        assertThat("Заказ не создался, ошибка статус кода", actual, equalTo(expected));
        assertThat("Трек номер не создан", orderId, is(not(0)));
    }
}

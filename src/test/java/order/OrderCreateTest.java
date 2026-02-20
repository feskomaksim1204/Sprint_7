package order;

import client.OrderClient;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private OrderClient orderClient;
    private Order order;

    private final List<String> color;
    private final int expectedStatusCode;

    public OrderCreateTest(List<String> color, int expectedStatusCode) {
        this.color = color;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK"), 201},           // только BLACK
                {Arrays.asList("GREY"), 201},            // только GREY
                {Arrays.asList("BLACK", "GREY"), 201},   // оба цвета
                {Arrays.asList(), 201}                    // без цвета
        });
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();

        // Создаём заказ с тестовыми данными и переданным цветом
        order = new Order(
                "Иван",
                "Петров",
                "Москва, ул. Ленина, д.1",
                "1",
                "+7-999-123-45-67",
                3,
                "2026-03-01",
                "Позвонить за час",
                color
        );
    }

    @Test
    public void orderCanBeCreatedWithDifferentColors() {
        Response response = orderClient.create(order);

        response.then()
                .statusCode(expectedStatusCode)
                .body("track", is(notNullValue()));
    }
}

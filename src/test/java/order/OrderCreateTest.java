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

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private OrderClient orderClient;
    private Order order;

    private final List<String> color;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},           // только BLACK
                {Arrays.asList("GREY")},            // только GREY
                {Arrays.asList("BLACK", "GREY")},   // оба цвета
                {Arrays.asList()}                    // без цвета
        });
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();

        order = new Order(
                "Максим",
                "Максимов",
                "Москва, ул. Просторная, д.1",
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
                .statusCode(SC_CREATED)  // 201 для всех случаев
                .body("track", is(notNullValue()));
    }
}

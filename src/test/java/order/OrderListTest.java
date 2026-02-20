package order;

import client.OrderClient;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;  // Добавлен этот импорт

public class OrderListTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void ordersListIsReturned() {
        Response response = orderClient.getOrders();

        response.then()
                .statusCode(200)
                .body("orders", is(notNullValue()))
                .body("orders.size()", greaterThan(0));
    }
}

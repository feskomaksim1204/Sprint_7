package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;
import model.CourierData;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response create(Order order) {
        return given()
                .baseUri(CourierData.BASE_URL)
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .baseUri(CourierData.BASE_URL)
                .when()
                .get(ORDER_PATH);
    }

    @Step("Отмена заказа с track {trackId}")
    public Response cancel(int trackId) {
        return given()
                .baseUri(CourierData.BASE_URL)
                .when()
                .put(ORDER_PATH + "/cancel/" + trackId);
    }
}

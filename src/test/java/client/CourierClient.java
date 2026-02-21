package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierData;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Создание курьера {courier.login}")
    public Response create(Courier courier) {
        return given()
                .baseUri(CourierData.BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CourierData.COURIER_PATH);
    }

    @Step("Логин курьера {courier.login}")
    public Response login(Courier courier) {
        return given()
                .baseUri(CourierData.BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CourierData.COURIER_PATH + "/login");
    }

    @Step("Удаление курьера с id {courierId}")
    public Response delete(int courierId) {
        return given()
                .baseUri(CourierData.BASE_URL)
                .when()
                .delete(CourierData.COURIER_PATH + "/" + courierId);
    }

    @Step("Получение ID курьера {courier.login}")
    public int getCourierId(Courier courier) {
        return login(courier)
                .then()
                .extract()
                .path("id");
    }
}

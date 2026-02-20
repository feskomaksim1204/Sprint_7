package client;

import io.restassured.response.Response;
import model.Courier;
import model.CourierData;

import static io.restassured.RestAssured.given;

public class CourierClient {

    // Создание курьера
    public Response create(Courier courier) {
        return given()
                .baseUri(CourierData.BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CourierData.COURIER_PATH);
    }

    // Логин курьера
    public Response login(Courier courier) {
        return given()
                .baseUri(CourierData.BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CourierData.COURIER_PATH + "/login");
    }

    // Удаление курьера
    public Response delete(int courierId) {
        return given()
                .baseUri(CourierData.BASE_URL)
                .when()
                .delete(CourierData.COURIER_PATH + "/" + courierId);
    }

    // Получение ID курьера по логину и паролю
    public int getCourierId(Courier courier) {
        return login(courier)
                .then()
                .extract()
                .path("id");
    }
}

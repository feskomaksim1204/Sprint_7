package courier;

import io.restassured.response.Response;
import model.Courier;
import model.CourierData;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CourierCreateTest extends BaseCourierTest {

    @Test
    public void courierCanBeCreated() {
        // Создаём курьера
        Response response = courierClient.create(courier);

        // Проверяем ответ
        response.then()
                .statusCode(201)
                .body("ok", is(true));

        // Получаем ID для удаления в tearDown
        courierId = courierClient.getCourierId(courier);
    }

    @Test
    public void cannotCreateSameCourierTwice() {
        // Создаём первого курьера
        courierClient.create(courier);

        // Пытаемся создать такого же
        Response response = courierClient.create(courier);

        // Проверяем ошибку
        response.then()
                .statusCode(409)
                .body("message", equalTo(CourierData.ERROR_LOGIN_ALREADY_USED));

        // Получаем ID для удаления в tearDown
        courierId = courierClient.getCourierId(courier);
    }

    @Test
    public void cannotCreateCourierWithoutLogin() {
        // Создаём курьера без логина
        Courier invalidCourier = new Courier(null, courier.getPassword(), courier.getFirstName());

        Response response = courierClient.create(invalidCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo(CourierData.ERROR_INSUFFICIENT_DATA));
    }

    @Test
    public void cannotCreateCourierWithoutPassword() {
        // Создаём курьера без пароля
        Courier invalidCourier = new Courier(courier.getLogin(), null, courier.getFirstName());

        Response response = courierClient.create(invalidCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo(CourierData.ERROR_INSUFFICIENT_DATA));
    }
}

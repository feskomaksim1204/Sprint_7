package courier;

import io.restassured.response.Response;
import model.Courier;
import model.CourierData;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CourierCreateTest extends BaseCourierTest {

    @Test
    public void courierCanBeCreated() {
        Response response = courierClient.create(courier);

        response.then()
                .statusCode(SC_CREATED)  // 201
                .body("ok", is(true));

        courierId = courierClient.getCourierId(courier);
    }

    @Test
    public void cannotCreateSameCourierTwice() {
        courierClient.create(courier);

        Response response = courierClient.create(courier);

        response.then()
                .statusCode(SC_CONFLICT)  // 409
                .body("message", equalTo(CourierData.ERROR_LOGIN_ALREADY_USED));

        courierId = courierClient.getCourierId(courier);
    }

    @Test
    public void cannotCreateCourierWithoutLogin() {
        Courier invalidCourier = new Courier(null, courier.getPassword(), courier.getFirstName());

        Response response = courierClient.create(invalidCourier);

        response.then()
                .statusCode(SC_BAD_REQUEST)  // 400
                .body("message", equalTo(CourierData.ERROR_INSUFFICIENT_DATA));
    }

    @Test
    public void cannotCreateCourierWithoutPassword() {
        Courier invalidCourier = new Courier(courier.getLogin(), null, courier.getFirstName());

        Response response = courierClient.create(invalidCourier);

        response.then()
                .statusCode(SC_BAD_REQUEST)  // 400
                .body("message", equalTo(CourierData.ERROR_INSUFFICIENT_DATA));
    }
}
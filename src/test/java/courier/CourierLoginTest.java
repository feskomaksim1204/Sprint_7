package courier;

import io.restassured.response.Response;
import model.Courier;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class CourierLoginTest extends BaseCourierTest {
    private Courier existingCourier;
    private int existingCourierId;

    @Before
    public void setUpLoginTest() {
        existingCourier = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());

        courierClient.create(existingCourier);
        existingCourierId = courierClient.getCourierId(existingCourier);
    }

    @Test
    public void courierCanLogin() {
        Response response = courierClient.login(existingCourier);

        response.then()
                .statusCode(SC_OK)  // 200
                .body("id", is(notNullValue()));
    }

    @Test
    public void cannotLoginWithWrongLogin() {
        Courier wrongLoginCourier = new Courier("wrongLogin123", existingCourier.getPassword(), null);

        Response response = courierClient.login(wrongLoginCourier);

        response.then()
                .statusCode(SC_NOT_FOUND)  // 404
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void cannotLoginWithWrongPassword() {
        Courier wrongPasswordCourier = new Courier(existingCourier.getLogin(), "wrongPassword", null);

        Response response = courierClient.login(wrongPasswordCourier);

        response.then()
                .statusCode(SC_NOT_FOUND)  // 404
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void cannotLoginWithoutLogin() {
        Courier noLoginCourier = new Courier(null, existingCourier.getPassword(), null);

        Response response = courierClient.login(noLoginCourier);

        response.then()
                .statusCode(SC_BAD_REQUEST)  // 400
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void cannotLoginWithoutPassword() {
        Courier noPasswordCourier = new Courier(existingCourier.getLogin(), "", null);

        Response response = courierClient.login(noPasswordCourier);

        response.then()
                .statusCode(SC_BAD_REQUEST)  // 400
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void cannotLoginWithNonExistingUser() {
        Courier nonExistingCourier = new Courier("neverExistedLogin", "neverExistedPass", null);

        Response response = courierClient.login(nonExistingCourier);

        response.then()
                .statusCode(SC_NOT_FOUND)  // 404
                .body("message", equalTo("Учетная запись не найдена"));
    }
}

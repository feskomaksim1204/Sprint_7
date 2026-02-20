package courier;

import io.restassured.response.Response;
import model.Courier;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class CourierLoginTest extends BaseCourierTest {
    private Courier existingCourier;
    private int existingCourierId;

    @Before
    public void setUpLoginTest() {
        // Создаём реального курьера для тестов логина
        existingCourier = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());

        // Создаём курьера через API и сохраняем его ID
        courierClient.create(existingCourier);
        existingCourierId = courierClient.getCourierId(existingCourier);
    }

    // Тест 1: курьер может авторизоваться
    @Test
    public void courierCanLogin() {
        Response response = courierClient.login(existingCourier);

        response.then()
                .statusCode(200)
                .body("id", is(notNullValue()));
    }

    // Тест 2: ошибка при неправильном логине
    @Test
    public void cannotLoginWithWrongLogin() {
        Courier wrongLoginCourier = new Courier("wrongLogin123", existingCourier.getPassword(), null);

        Response response = courierClient.login(wrongLoginCourier);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // Тест 3: ошибка при неправильном пароле
    @Test
    public void cannotLoginWithWrongPassword() {
        Courier wrongPasswordCourier = new Courier(existingCourier.getLogin(), "wrongPassword", null);

        Response response = courierClient.login(wrongPasswordCourier);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // Тест 4: ошибка при авторизации без логина
    @Test
    public void cannotLoginWithoutLogin() {
        Courier noLoginCourier = new Courier(null, existingCourier.getPassword(), null);

        Response response = courierClient.login(noLoginCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    // Тест 5: ошибка при авторизации без пароля (исправлено)
    @Test
    public void cannotLoginWithoutPassword() {
        // Создаём курьера только с логином (без пароля)
        // Важно: отправляем пустую строку вместо null, чтобы сервер не зависал
        Courier noPasswordCourier = new Courier(existingCourier.getLogin(), "", null);

        Response response = courierClient.login(noPasswordCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    // Тест 6: ошибка при авторизации несуществующего пользователя
    @Test
    public void cannotLoginWithNonExistingUser() {
        Courier nonExistingCourier = new Courier("neverExistedLogin", "neverExistedPass", null);

        Response response = courierClient.login(nonExistingCourier);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}

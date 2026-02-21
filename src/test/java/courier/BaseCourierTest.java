package courier;

import client.CourierClient;
import com.github.javafaker.Faker;
import model.Courier;
import org.junit.After;
import org.junit.Before;

import java.util.Locale;

public class BaseCourierTest {
    protected CourierClient courierClient;
    protected Courier courier;
    protected Faker faker;
    protected int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        faker = new Faker(new Locale("ru"));

        // Генерируем уникальные данные для каждого теста
        String login = faker.name().lastName() + faker.number().digits(4);
        String password = faker.number().digits(6);
        String firstName = faker.name().firstName();

        courier = new Courier(login, password, firstName);
    }

    @After
    public void tearDown() {
        // Удаляем курьера после теста, если он был создан
        if (courierId > 0) {
            courierClient.delete(courierId);
        }
    }
}

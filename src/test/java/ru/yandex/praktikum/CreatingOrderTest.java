package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.Matchers.notNullValue;

//Класс параметризованного теста проверки ручки "Создание заказа"
@RunWith(Parameterized.class)
public class CreatingOrderTest {
    private final String black;
    private final String grey;
    private final AccountData accountData = new AccountData();
    private final OrderClient orderClient = new OrderClient();

    public CreatingOrderTest(String black, String grey) {
        this.black = black;
        this.grey = grey;
    }

    @Parameterized.Parameters(name = "Тест CreatingOrderTest - {0} - {1}")
    public static Object[] getParameters() {
        return new Object[][]{
                {"BLACK", "GREY"},
                {null, "GREY"},
                {"BLACK", null},
                {null, null},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = accountData.getUrl();
    }

    @Test
    @DisplayName("Check request creating order  /api/v1/orders")
    @Description("Check status code <201> and and message <track> when creating order")
    public void CreatingOrderColorBlackGreyTest() {
        OrderData orderData = new OrderData(new String[]{black, grey});
        Response response = orderClient.CreatingOrder(orderData);
        response.then().statusCode(HTTP_CREATED).and()
                .assertThat().body("track", notNullValue());
    }
}

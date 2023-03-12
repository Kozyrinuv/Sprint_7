package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.notNullValue;

//Класс тестов проверки ручки "Получение списка заказов"
public class OrderListTest {
    private final AccountData accountData = new AccountData();
    private final OrderClient orderClient = new OrderClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = accountData.getUrl();
    }

    @Test
    @DisplayName("Check request getting a list of orders /api/v1/orders")
    @Description("Check status code <200> and <notNullValue> body  order when getting a list of orders")
    public void getOrderListTest() {
        Response response = orderClient.getOrderList();
        response.then().statusCode(HTTP_OK)
                .and()
                .assertThat().body(notNullValue());
        //или можно так если уверены, что есть хоть один заказ .assertThat().body("orders[0].id", notNullValue());
    }
}

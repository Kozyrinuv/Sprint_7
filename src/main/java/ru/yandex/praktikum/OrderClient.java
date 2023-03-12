package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

//Класс с методами запросов для создания заказа и получения списка заказов
public class OrderClient {
    private final AccountData accountData = new AccountData();

    // метод для шага "Создание заказа"
    @Step("Request Creating Order")
    public Response CreatingOrder(OrderData orderData) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(orderData)
                .when()
                .post(accountData.getCreatingOrder());
        return response;
    }

    // метод для шага "Получение списка заказов
    @Step("Request Get Order List")
    public Response getOrderList() {
        Response response = given()
                .contentType(ContentType.JSON)
                .get(accountData.getOrderList());
        return response;
    }
}

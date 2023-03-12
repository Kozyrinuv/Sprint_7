package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

//Класс с методами запросов клиента для создания, удаления и авторизации клиента
public class CourierClient {
    private final AccountData accountData = new AccountData();

    // метод для шага "Запрос на создание курьера" /v1/courier
    @Step("Send request Create Courier")
    public Response requestCreateCourier(String json) {
        //Запрос на создание курьера
        Response response = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(accountData.getCreatingCourier());
        return response;
    }

    // метод для шага "Первичное создание курьера для тестов проверки дубликатов" /v1/courier
    @Step("Request Create Courier")
    public void createFirstCourier(String json) {
        //Создаем курьера для теств проверки дубликатов
        Response response = requestCreateCourier(json);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    // метод для шага "Проверка авторизации курьера - есть ли такой курьер?":
    @Step("Send request Courier Authorization Check")
    public int loginCourier(String json) {
        //Проверяем залогинился ли курьер (имеет id) или нет
        Response response = requestLoginCourier(json);
        int code = response.getStatusCode();
        if (code != 200) return (-1);
        else return response.then().extract().path("id");
    }

    // метод для шага "Создание запроса на логин курьера
    @Step("Send request Login Courier")
    public Response requestLoginCourier(String json) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(accountData.getLoginCourier());
        return response;
    }

    // метод для шага "Удаление курьера по его id":
    @Step("Send request to deleting a courier by its id  {id}")
    public void deleteCourier(int id) {
        //Удаляем курьэра с полученным id авторизации
        String path = accountData.getCreatingCourier() + "/" + id;
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(path);
    }

}

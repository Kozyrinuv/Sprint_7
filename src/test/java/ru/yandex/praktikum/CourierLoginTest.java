package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

//Класс тестов проверки ручки "Автризация курьера" (предполагается, что курьер создан)
public class CourierLoginTest {
    private final CourierClient courierClient = new CourierClient();
    private final AccountData accountData = new AccountData();
    private final String mylogin = accountData.getLogin();
    private final String mypassword = accountData.getPassword();
    private final String myname = accountData.getName();
    private int idCourier;// id залогинившегося курьера

    @Before
    public void setUp() {
        RestAssured.baseURI = accountData.getUrl();
        String json = "{" + accountData.getLogin() + "\"," + accountData.getPassword() + "\"," + accountData.getName() + "\"}";
        courierClient.requestCreateCourier(json);//Пытаемся создать курьера
        idCourier = -1;
    }

    @After
    public void closeDown() {
        String json = "{" + accountData.getLogin() + "\"," + accountData.getPassword() + "\"," + accountData.getName() + "\"}";
        idCourier = courierClient.loginCourier(json);//Проверка наличия id-курьера в базе после логина
        if (idCourier > 0) courierClient.deleteCourier(idCourier);//Удаление курьера по его id
    }

    @Test  //Тест успешной авторизации курьера
    @DisplayName("Check login OK courier /api/v1/courier/login")
    @Description("Check status code <200> and <notNullValue> field <id> when request login courier")
    public void loginCourierOKTest() {
        String json = "{" + mylogin + "\"," + mypassword + "\"}";
        Response response = courierClient.requestLoginCourier(json);
        response.then().statusCode(HTTP_OK).and()
                .assertThat().body("id", notNullValue());
    }

    @Test   //Тест авторизации не существующего курьера (нет в базе пары Login и Password)
    @DisplayName("Check login courier request with non-existent Login and Password /api/v1/courier/login")
    @Description("Check status code <404> and message <Account not found> when request login courier")
    public void loginCourierNoExistLoginAndPasswordTest() {
        String addLoginPassword = RandomStringUtils.randomAlphabetic(5);
        String json = "{" + mylogin + addLoginPassword + "\"," + mypassword + addLoginPassword + "\"}";
        Response response = courierClient.requestLoginCourier(json);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test  //Тест авторизации курьера без задания Login
    @DisplayName("Check login courier without login  /api/v1/courier/login")
    @Description("Check status code <400> and message <Insufficient login information> when request login courier")
    public void loginCourierWithoutLoginTest() {
        String json = "{" + mypassword + "\"}";
        Response response = courierClient.requestLoginCourier(json);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }

    //*******************************************************************************************
    //!!!Error - Service unavailable - in Postman also Error                                    *
    //!!!Данный метод API оставляет в базе id-клиента поэтому после каждого теста делаем логин  *
    //что бы получить такой id-клиента и потом его удалить                                      *
    //*******************************************************************************************
    @Test  //Тест авторизации курьера без задания Password
    @DisplayName("Check login courier without password  /api/v1/courier/login")
    @Description("!!!Error - Service unavailable - Check status code <400> and message <Insufficient login information> when request login courier")
    public void loginCourierWithoutPasswordTest() {
        String json = "{" + mylogin + "\"}";
        Response response = courierClient.requestLoginCourier(json);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test  //Тест авторизации курьера (нет в базе такого Login, но есть такой Password)
    @DisplayName("Check login courier request with non-existent only Login /api/v1/courier/login")
    @Description("Check status code <404> and message <Account not found> when request login courier")
    public void loginCourierNoExistLoginTest() {
        String addLogin = RandomStringUtils.randomAlphabetic(5);
        String json = "{" + mylogin + addLogin + "\"," + mypassword + "\"}";
        Response response = courierClient.requestLoginCourier(json);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test     //Тест авторизации курьера (нет в базе такого Password, но есть такой Login)
    @DisplayName("Check login courier request with non-existent only Password /api/v1/courier/login")
    @Description("Check status code <404> and message <Account not found> when request login courier")
    public void loginCourierNoExistPasswordTest() {
        String addPassword = RandomStringUtils.randomAlphabetic(5);
        String json = "{" + mylogin + "\"," + mypassword + addPassword + "\"}";
        Response response = courierClient.requestLoginCourier(json);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HTTP_NOT_FOUND);
    }

}

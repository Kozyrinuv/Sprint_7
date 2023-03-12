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

//Класс тестов проверки ручки "Создание курьера"
public class CreatingCourierTest {
    private final CourierClient courierClient = new CourierClient();
    private final AccountData accountData = new AccountData();
    private final String mylogin = accountData.getLogin();
    private final String mypassword = accountData.getPassword();
    private final String myname = accountData.getName();
    private int idCourier;// id залогинившегося курьера (для удаления)

    @Before
    public void setUp() {
        RestAssured.baseURI = accountData.getUrl();
        idCourier = -1;
        String json = "{" + accountData.getLogin() + "\"," + accountData.getPassword() + "\"}";
        idCourier = courierClient.loginCourier(json);//Проверка наличия курьера с тестовым Login и Password
        if (idCourier > 0) courierClient.deleteCourier(idCourier);//Если такой курьер есть в наличии -> удаляем
    }

    @After
    public void closeDown() {
        String json = "{" + accountData.getLogin() + "\"," + accountData.getPassword() + "\"}";
        idCourier = courierClient.loginCourier(json);
        if (idCourier > 0) courierClient.deleteCourier(idCourier);
    }

    @Test //Тест успешного создания нового курьера
    @DisplayName("Check request creating new courier /api/v1/courier")
    @Description("Check status code <201> and message <OK>  when request creating new courier")
    public void creatingCourierOKTest() {
        String json = "{" + mylogin + "\"," + mypassword + "\"," + myname + "\"}";
        Response response = courierClient.requestCreateCourier(json);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(HTTP_CREATED);
    }

    @Test //Тест попытки создания дубликата курьера с таким же Login и Password как уже существующий
    @DisplayName("Check request creating duplicate courier /api/v1/courier")
    @Description("Check status code <409> and message <This login is already in use>  when request creating new courier")
    public void creatingCourierDuplicateTest() {
        String json = "{" + mylogin + "\"," + mypassword + "\"," + myname + "\"}";
        // "Первичное создание курьера для тестов проверки дубликатов
        courierClient.createFirstCourier(json);
        json = "{" + mylogin + "\"," + mypassword + "\"," + myname + "\"}";
        Response response = courierClient.requestCreateCourier(json);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(HTTP_CONFLICT);
    }

    @Test //Тест попытки создания курьера с таким же Login, но другим Password как уже существующий
    @DisplayName("Check request creating duplicate login of courier but another password /api/v1/courier")
    @Description("Check status code <409> and message <This login is already in use> when request creating new courier")
    public void creatingCourierDuplicateLoginTest() {
        String json = "{" + mylogin + "\"," + mypassword + "\"," + myname + "\"}";
        // "Первичное создание курьера для тестов проверки дубликатов
        courierClient.createFirstCourier(json);
        String addPassword = RandomStringUtils.randomAlphabetic(5);
        json = "{" + mylogin + "\"," + mypassword + addPassword + "\"," + myname + "\"}";
        Response response = courierClient.requestCreateCourier(json);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(HTTP_CONFLICT);
    }

    @Test  //Тест попытки создания курьера без Password
    @DisplayName("Check request creating courier without password /api/v1/courier")
    @Description("Check status code <400> and message <Insufficient data to create an account> when request creating new courier")
    public void creatingCourierNOPasswordTest() {
        String json = "{" + mylogin + "\"," + myname + "\"}";
        Response response = courierClient.requestCreateCourier(json);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test  //Тест попытки создания курьера без Login
    @DisplayName("Check request creating courier without login /api/v1/courier")
    @Description("Check status code <400> and message <Insufficient data to create an account> when request creating new courier")
    public void creatingCourierNOLoginTest() {
        String json = "{" + mypassword + "\"," + myname + "\"}";
        Response response = courierClient.requestCreateCourier(json);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(HTTP_BAD_REQUEST);
    }

}

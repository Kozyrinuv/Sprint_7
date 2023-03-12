package ru.yandex.praktikum;

//Randomные Login и Password не создаю, чтобы можно было отыскать записи в базе (при наличии диступа к ней)
// например по полю логина и удобно было отлаживать и контролировать работу тестов в Postman
//также при Randomных данных сложно проконтролировать, что все тестовые данные удалились из базы после тестов
public class AccountData {
    private final String login = "\"login\": \"misha_k";
    private final String password = "\"password\": \"43215";
    private final String name = "\"firstName\": \"Mikle";
    private final String url = "https://qa-scooter.praktikum-services.ru/api/v1";
    private final String creatingCourier = "/courier";
    private final String loginCourier = creatingCourier + "/login";
    private final String creatingOrder = "/orders";
    private final String orderList = "/orders";

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatingCourier() {
        return creatingCourier;
    }

    public String getLoginCourier() {
        return loginCourier;
    }

    public String getCreatingOrder() {
        return creatingOrder;
    }

    public String getOrderList() {
        return orderList;
    }
}

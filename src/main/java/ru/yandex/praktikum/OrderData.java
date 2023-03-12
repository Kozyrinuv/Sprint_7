package ru.yandex.praktikum;

import java.util.Arrays;

// Класс POJO с данными для проверки параметризованного теста проверки ручки "Создание заказа"
public class OrderData {
    //Названия полей заказа и их значения взяты из документации к API
    private final String firstName = "Naruto";
    private final String lastName = "Uchiha";
    private final String address = "Konoha, 142 apt.";
    private final Integer metroStation = 4;
    private final String phone = "+7 800 355 35 35";
    private final Integer rentTime = 5;
    private final String deliveryDate = "2020-06-06";
    private final String comment = "Saske, come back to Konoha";
    private String[] color = {"BLACK", "GREY"};

    // Конструктор с параметрами
    public OrderData(String[] color) {
        this.color = color;
    }

    //Конструктор без параметров
    public OrderData() {
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public Integer getMetroStation() {
        return metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getRentTime() {
        return rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", metroStation=" + metroStation +
                ", phone='" + phone + '\'' +
                ", rentTime=" + rentTime +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", comment='" + comment + '\'' +
                ", color=" + Arrays.toString(color) +
                '}';
    }
}

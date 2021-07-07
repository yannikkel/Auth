package ru.netology.date;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.var;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static ru.netology.date.DataGenerator.SendQuery.makeRequest;


public class DataGenerator {
    private DataGenerator() {
    }

    public static class SendQuery {
        private static RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();


        static void makeRequest(UserInfo userInfo) {
            // сам запрос
            given() // "дано"
                    .spec(requestSpec) // указываем, какую спецификацию используем
                    .body(userInfo) // передаём в теле объект, который будет преобразован в JSON
                    .when() // "когда"
                    .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                    .then() // "тогда ожидаем"
                    .statusCode(200); // код 200 OK
        }
    }

    public static class Registration {
        private static Faker faker = new Faker(new Locale("en"));

        public static UserInfo generateUser(String status) {
            var user = new UserInfo(faker.name().fullName(), faker.internet().password(), status);
            makeRequest(user);
            return user;
        }

        public static UserInfo generateWrongLoginUser(String status) {
            var password = faker.internet().password();
            makeRequest(new UserInfo(faker.name().firstName(), password, status));
            return new UserInfo(faker.name().firstName(), password, status);
        }

        public static UserInfo generateWrongPasswordUser(String status) {
            var login = faker.name().firstName();
            makeRequest(new UserInfo(login, faker.internet().password(), status));
            return new UserInfo(login, faker.internet().password(), status);
        }
    }
}

package ru.netology.diplom.test;

import org.junit.jupiter.api.BeforeEach;
import static com.codeborne.selenide.Selenide.open;

import java.lang.module.Configuration;

public class Test {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen=true;
    }

    @org.junit.jupiter.api.Test
    void shouldGetErrorWithBlockedUser(){
        RegistrationInfo blockedUser = DataGenerator.Registration.getUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $(withText("Пользователь заблокирован")).shouldBe(appear);
    }
}

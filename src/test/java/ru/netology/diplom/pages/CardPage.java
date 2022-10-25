package ru.netology.diplom.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.diplom.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardPage {
    private final SelenideElement numberCardField = $(".input [placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthCard = $("input[placeholder='08']");
    private final SelenideElement yearCard = $("input[placeholder='22']");
    private final SelenideElement fieldCardHolder = $$(".input__top").find(text("Владелец")).parent();
    private final SelenideElement nameHolderCard = fieldCardHolder.$(".input__control");
    private final SelenideElement codeCVC = $("input[placeholder='999']");
    private final SelenideElement buttonContinue = $$("button").find(exactText("Продолжить"));
    private final ElementsCollection resultLinks = $$(".input__top");
    private final SelenideElement incorrectFormat = $(byText("Неверный формат"));
    private final SelenideElement emptyField = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement invalidExpiredDate = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement expiredCard = $(byText("Истёк срок действия карты"));
    private final SelenideElement approvedNotification = $(".notification_status_ok");
    private final SelenideElement declinedNotification = $(".notification_status_error");


    public void fillCardInformationForSelectedWay(DataHelper.CardInfo cardInformation) {
        numberCardField.setValue(cardInformation.getNumber());
        monthCard.setValue(cardInformation.getMonth());
        yearCard.setValue(cardInformation.getYear());
        nameHolderCard.setValue(cardInformation.getHolder());
        codeCVC.setValue(cardInformation.getCvc());
        buttonContinue.click();
    }

    public void shouldImproperFormatNotification() {
        incorrectFormat.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldEmptyFieldNotification() {

        emptyField.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldInvalidExpiredDateNotification() {
        invalidExpiredDate.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldExpiredDatePassNotification() {
        expiredCard.shouldBe(Condition.visible);
    }

    public void approved() {
        approvedNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void declined() {
        declinedNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldValueFieldNumberCard() {
        var fieldNumberCard = resultLinks.find(text("Номер карты")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }

    public void shouldValueFieldMonth() {
        var fieldNumberCard = resultLinks.find(text("Месяц")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }

    public void shouldValueFieldYear() {
        var fieldNumberCard = resultLinks.find(text("Год")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }

    public void shouldValueFieldCodeCVC() {
        var fieldNumberCard = resultLinks.find(text("CVC/CVV")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }

    public void shouldValueFieldHolder() {
        var fieldNumberCard = resultLinks.find(text("Владелец")).parent();
        fieldNumberCard.shouldHave(text("Поле обязательно для заполнения"));
    }

    public void shouldValueFieldHolder2() {
        var fieldNumberCard = resultLinks.find(text("Владелец")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }
}
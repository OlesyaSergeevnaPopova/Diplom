package ru.netology.diplom.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

public class TravelPage {
    private final SelenideElement buttonBuyByDebitCard = $$(".button__text").find(exactText("Купить"));
    private final SelenideElement buttonBuyCreditCard = $$(".button__text").find(exactText("Купить в кредит"));
    private final SelenideElement paymentBySelectedWayHeader = $$(".heading").find(exactText("Путешествие дня"));

    public TravelPage() {
        paymentBySelectedWayHeader.shouldBe(Condition.visible);
    }

    public CardPage selectBuyByDebitCard() {
        buttonBuyByDebitCard.click();
        return new CardPage();
    }

    public CreditCardPage selectBuyByCreditCard() {
        buttonBuyCreditCard.click();
        return new CreditCardPage();

    }
}

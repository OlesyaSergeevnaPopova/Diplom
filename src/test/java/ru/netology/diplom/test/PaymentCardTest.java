package ru.netology.diplom.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.diplom.data.BdHelper;
import ru.netology.diplom.data.DataHelper;
import ru.netology.diplom.pages.TravelPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.diplom.data.BdHelper.cleanDataBase;


public class PaymentCardTest {

    @BeforeEach
    void openPage() {
        cleanDataBase();
        Configuration.holdBrowserOpen = true;
        open("http://localhost:8080");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Nested
    class shouldHappyPathForCardWithDifferentStatus {
        @Test
        void shouldSuccessWithValidDebitCard() {
            val validCardInformation = DataHelper.getValidCardInformation();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(validCardInformation);
            paymentPage.approved();
            assertEquals("APPROVED", BdHelper.getPurchaseByDebitCard());
        }


        @Test
        void shouldUnsuccessWithInvalidDebitCard() {
            val invalidCardInformation = DataHelper.getInvalidCardInformation();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(invalidCardInformation);
            paymentPage.declined();
            assertEquals("DECLINED", BdHelper.getPurchaseByDebitCard());
        }
    }

    @Nested
    class shouldInvalidCardNumber {
        @Test
        void shouldGetNotificationEmptyFields() {
            val incorrectCardInfo = DataHelper.getInvalidCardDataIfEmptyAllFields();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldEmptyFieldNotification();
            paymentPage.shouldImproperFormatNotification();
            paymentPage.shouldValueFieldCodeCVC();
            paymentPage.shouldValueFieldYear();
            paymentPage.shouldValueFieldMonth();
            paymentPage.shouldValueFieldNumberCard();
            paymentPage.shouldValueFieldHolder();
        }

        @Test
        public void shouldFailurePaymentIfEmptyNumberCard() {
            val incorrectCardInfo = DataHelper.getInvalidCardWithNumberEmpty();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldIncorrectFieldNumberCardMiniNumber() {
            val incorrectCardInfo = DataHelper.getInvalidCardWithIncorrectNumber();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldIncorrectFieldNumberCardOtherNumber() {
            val incorrectCardInfo = DataHelper.getInvalidCardNumberIfOutOfDatabase();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
        }
    }

    @Nested
    class shouldInvalidCardFieldMonth {
        @Test
        public void shouldInvalidMonthIfEmpty() {
            val incorrectCardInfo = DataHelper.getInvalidMonthIfEmpty();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldMonth();

        }

        @Test
        public void shouldInvalidNumberOfMonthIfMore12() {
            val incorrectCardInfo = DataHelper.getInvalidNumberOfMonthIfMore12();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldInvalidExpiredDateNotification();

        }

        @Test
        public void shouldInvalidNumberOfMonthIfOneDigit() {
            val incorrectCardInfo = DataHelper.getInvalidNumberOfMonthWhenOneDigit();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldMonth();

        }

        @Test
        public void shouldInvalidNumberOfMonthIfZero() {
            val incorrectCardInfo = DataHelper.getInvalidNumberOfMonthIfZero();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
            paymentPage.shouldValueFieldMonth();
        }
    }

    @Nested
    class shouldInvalidCardFieldYear {
        @Test
        public void shouldInvalidYearIfZero() {
            val incorrectCardInfo = DataHelper.getInvalidYearIfZero();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldExpiredDatePassNotification();
        }

        @Test
        public void shouldInvalidYearIfInTheFarFuture() {
            val incorrectCardInfo = DataHelper.getInvalidYearIfInTheFarFuture();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldInvalidExpiredDateNotification();

        }

        @Test
        public void shouldInvalidNumberOfYearIfOneDigit() {
            val incorrectCardInfo = DataHelper.getInvalidNumberOfYearIfOneDigit();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldYear();

        }

        @Test
        public void shouldInvalidYearIfEmpty() {
            val incorrectCardInfo = DataHelper.getInvalidYearIfEmpty();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldYear();
        }

        @Test
        public void shouldInvalidYearIfBeforeCurrentYear() {
            val incorrectCardInfo = DataHelper.getInvalidPastYear();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldExpiredDatePassNotification();

        }
    }

    @Nested
    class shouldInvalidCardFieldOwner {
        @Test
        public void shouldInvalidCardHolderNameIfEmpty() {
            val incorrectCardInfo = DataHelper.getInvalidCardHolderNameIfEmpty();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldHolder();
        }

        @Test
        public void shouldInvalidCardOwnerNameIfNumericAndSpecialCharacters() {
            val incorrectCardInfo = DataHelper.getInvalidCardOwnerNameIfNumericAndSpecialCharacters();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
            paymentPage.shouldValueFieldHolder2();

        }

        @Test
        public void shouldInvalidCardOwnerNameIfRussianLetters() {
            val incorrectCardInfo = DataHelper.getInvalidCardHolderNameIfRussianLetters();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
            paymentPage.shouldValueFieldHolder2();
        }
    }

    @Nested
    class shouldInvalidCardFieldCodeCVC {
        @Test
        public void shouldInvalidCvcIfEmpty() {
            val incorrectCardInfo = DataHelper.getInvalidCvcIfEmpty();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvcIfOneDigit() {
            val incorrectCardInfo = DataHelper.getInvalidCvcIfOneDigit();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvcIfTwoDigits() {
            val incorrectCardInfo = DataHelper.getInvalidCvcIfTwoDigits();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvvIfThreeZero() {
            val incorrectCardInfo = DataHelper.getInvalidCvvIfThreeZero();
            val paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
            paymentPage.shouldValueFieldCodeCVC();
        }
    }
}
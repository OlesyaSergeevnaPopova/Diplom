package ru.netology.diplom.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.diplom.data.DbHelper;
import ru.netology.diplom.data.DataHelper;
import ru.netology.diplom.pages.TravelPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.diplom.data.DbHelper.cleanDataBase;


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
            var validCardInformation = DataHelper.getValidCardInformation();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(validCardInformation);
            paymentPage.approved();
            assertEquals("APPROVED", DbHelper.getPurchaseByDebitCard());
        }


        @Test
        void shouldUnsuccessWithInvalidDebitCard() {
            var invalidCardInformation = DataHelper.getInvalidCardInformation();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(invalidCardInformation);
            paymentPage.declined();
            assertEquals("DECLINED", DbHelper.getPurchaseByDebitCard());
        }
    }

    @Nested
    class shouldInvalidCardNumber {
        @Test
        void shouldGetNotificationEmptyFields() {
            var incorrectCardInfo = DataHelper.getInvalidCardDataIfEmptyAllFields();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
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
            var incorrectCardInfo = DataHelper.getInvalidCardWithNumberEmpty();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldIncorrectFieldNumberCardMiniNumber() {
            var incorrectCardInfo = DataHelper.getInvalidCardWithIncorrectNumber();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldIncorrectFieldNumberCardOtherNumber() {
            var incorrectCardInfo = DataHelper.getInvalidCardNumberIfOutOfDatabase();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
        }
    }

    @Nested
    class shouldInvalidCardFieldMonth {
        @Test
        public void shouldInvalidMonthIfEmpty() {
            var incorrectCardInfo = DataHelper.getInvalidMonthIfEmpty();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldMonth();

        }

        @Test
        public void shouldInvalidNumberOfMonthIfMore12() {
            var incorrectCardInfo = DataHelper.getInvalidNumberOfMonthIfMore12();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldInvalidExpiredDateNotification();

        }

        @Test
        public void shouldInvalidNumberOfMonthIfOneDigit() {
            var incorrectCardInfo = DataHelper.getInvalidNumberOfMonthWhenOneDigit();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldMonth();

        }

        @Test
        public void shouldInvalidNumberOfMonthIfZero() {
            var incorrectCardInfo = DataHelper.getInvalidNumberOfMonthIfZero();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
            paymentPage.shouldValueFieldMonth();
        }
    }

    @Nested
    class shouldInvalidCardFieldYear {
        @Test
        public void shouldInvalidYearIfZero() {
            var incorrectCardInfo = DataHelper.getInvalidYearIfZero();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldExpiredDatePassNotification();
        }

        @Test
        public void shouldInvalidYearIfInTheFarFuture() {
            var incorrectCardInfo = DataHelper.getInvalidYearIfInTheFarFuture();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldInvalidExpiredDateNotification();

        }

        @Test
        public void shouldInvalidNumberOfYearIfOneDigit() {
            var incorrectCardInfo = DataHelper.getInvalidNumberOfYearIfOneDigit();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldYear();

        }

        @Test
        public void shouldInvalidYearIfEmpty() {
            var incorrectCardInfo = DataHelper.getInvalidYearIfEmpty();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldYear();
        }

        @Test
        public void shouldInvalidYearIfBeforeCurrentYear() {
            var incorrectCardInfo = DataHelper.getInvalidPastYear();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldExpiredDatePassNotification();

        }
    }

    @Nested
    class shouldInvalidCardFieldOwner {
        @Test
        public void shouldInvalidCardHolderNameIfEmpty() {
            var incorrectCardInfo = DataHelper.getInvalidCardHolderNameIfEmpty();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldHolder();
        }

        @Test
        public void shouldInvalidCardOwnerNameIfNumericAndSpecialCharacters() {
            var incorrectCardInfo = DataHelper.getInvalidCardOwnerNameIfNumericAndSpecialCharacters();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
            paymentPage.shouldValueFieldHolder2();

        }

        @Test
        public void shouldInvalidCardOwnerNameIfRussianLetters() {
            var incorrectCardInfo = DataHelper.getInvalidCardHolderNameIfRussianLetters();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
            paymentPage.shouldValueFieldHolder2();
        }
    }

    @Nested
    class shouldInvalidCardFieldCodeCVC {
        @Test
        public void shouldInvalidCvcIfEmpty() {
            var incorrectCardInfo = DataHelper.getInvalidCvcIfEmpty();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvcIfOneDigit() {
            var incorrectCardInfo = DataHelper.getInvalidCvcIfOneDigit();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvcIfTwoDigits() {
            var incorrectCardInfo = DataHelper.getInvalidCvcIfTwoDigits();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvvIfThreeZero() {
            var incorrectCardInfo = DataHelper.getInvalidCvvIfThreeZero();
            var paymentPage = new TravelPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.declined();
            paymentPage.shouldValueFieldCodeCVC();
        }
    }
}
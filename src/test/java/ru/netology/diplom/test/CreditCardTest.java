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


public class CreditCardTest {

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
        void shouldSuccessWithValidCreditCard() {
            var validCardInformation = DataHelper.getValidCardInformation();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(validCardInformation);
            creditCardPage.approved();
            assertEquals("APPROVED", DbHelper.getPurchaseOnCreditCard());
        }

        @Test
        void shouldUnsuccessWithInvalidCreditCard() {
            var invalidCardInformation = DataHelper.getInvalidCardInformation();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInformation);
            creditCardPage.declined();
            assertEquals("DECLINED", DbHelper.getPurchaseOnCreditCard());
        }
    }

    @Nested
    class shouldInvalidCardNumber {
        @Test
        void shouldGetNotificationEmptyFields() {
            var invalidCardInformation = DataHelper.getInvalidCardDataIfEmptyAllFields();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInformation);
            creditCardPage.shouldEmptyFieldNotification();
            creditCardPage.shouldImproperFormatNotification();
            creditCardPage.shouldValueFieldCodeCVC();
            creditCardPage.shouldValueFieldHolder();
            creditCardPage.shouldValueFieldMonth();
            creditCardPage.shouldValueFieldYear();
            creditCardPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldInvalidCardNumberIfEmpty() {
            var invalidCardInfo = DataHelper.getInvalidCardWithNumberEmpty();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldIncorrectFieldNumberCardMiniNumber() {
            var invalidCardInfo = DataHelper.getInvalidCardWithIncorrectNumber();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldIncorrectFieldNumberCardOtherNumber() {
            var invalidCardInfo = DataHelper.getInvalidCardNumberIfOutOfDatabase();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.declined();
        }
    }

    @Nested
    class shouldInvalidCardFieldMonth {
        @Test
        public void shouldInvalidMonthIfEmpty() {
            var invalidCardNumber = DataHelper.getInvalidMonthIfEmpty();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldMonth();
        }

        @Test
        public void shouldInvalidNumberOfMonthIfMore12() {
            var invalidCardNumber = DataHelper.getInvalidNumberOfMonthIfMore12();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldInvalidExpiredDateNotification();
        }

        @Test
        public void shouldInvalidNumberOfMonthIfOneDigit() {
            var invalidCardNumber = DataHelper.getInvalidNumberOfMonthWhenOneDigit();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldMonth();
        }

        @Test
        public void shouldInvalidNumberOfMonthIfZero() {
            var invalidCardNumber = DataHelper.getInvalidNumberOfMonthIfZero();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.declined();
        }
    }

    @Nested
    class shouldInvalidCardFieldYear {
        @Test
        public void shouldInvalidYearIfZero() {
            var invalidCardNumber = DataHelper.getInvalidYearIfZero();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldExpiredDatePassNotification();
        }

        @Test
        public void shouldInvalidYearIfInTheFarFuture() {
            var invalidCardNumber = DataHelper.getInvalidYearIfInTheFarFuture();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldInvalidExpiredDateNotification();
        }

        @Test
        public void shouldInvalidNumberOfYearIfOneDigit() {
            var invalidCardNumber = DataHelper.getInvalidNumberOfYearIfOneDigit();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldYear();
        }

        @Test
        public void shouldInvalidYearIfEmpty() {
            var invalidCardNumber = DataHelper.getInvalidYearIfEmpty();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldYear();
        }

        @Test
        public void shouldInvalidYearIfBeforeCurrentYear() {
            var invalidCardInfo = DataHelper.getInvalidPastYear();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldExpiredDatePassNotification();
        }
    }

    @Nested
    class shouldInvalidCardFieldOwner {
        @Test
        public void shouldInvalidCardOwnerNameIfEmpty() {
            var invalidCardInfo = DataHelper.getInvalidCardHolderNameIfEmpty();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldHolder();
        }

        @Test
        public void shouldInvalidCardOwnerNameIfNumericAndSpecialCharacters() {
            var invalidCardInfo = DataHelper.getInvalidCardOwnerNameIfNumericAndSpecialCharacters();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldHolder2();
        }

        @Test
        public void shouldInvalidCardOwnerNameIfRussianLetters() {
            var invalidCardInfo = DataHelper.getInvalidCardHolderNameIfRussianLetters();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldHolder2();
        }
    }

    @Nested
    class shouldInvalidCardFieldCodeCVC {
        @Test
        public void shouldInvalidCvcIfEmpty() {
            var invalidCardInfo = DataHelper.getInvalidCvcIfEmpty();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvcIfOneDigit() {
            var invalidCardNumber = DataHelper.getInvalidCvcIfOneDigit();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvcIfTwoDigits() {
            var invalidCardNumber = DataHelper.getInvalidCvcIfTwoDigits();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvvIfThreeZero() {
            var invalidCardNumber = DataHelper.getInvalidCvvIfThreeZero();
            var travelPage = new TravelPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldCodeCVC();
        }
    }
}
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
            val validCardInformation = DataHelper.getValidCardInformation();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(validCardInformation);
            creditCardPage.approved();
            assertEquals("APPROVED", BdHelper.getPurchaseOnCreditCard());
        }

        @Test
        void shouldUnsuccessWithInvalidCreditCard() {
            val invalidCardInformation = DataHelper.getInvalidCardInformation();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInformation);
            creditCardPage.declined();
            assertEquals("DECLINED", BdHelper.getPurchaseOnCreditCard());
        }
    }

    @Nested
    class shouldInvalidCardNumber {
        @Test
        void shouldGetNotificationEmptyFields() {
            val invalidCardInformation = DataHelper.getInvalidCardDataIfEmptyAllFields();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
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
            val invalidCardInfo = DataHelper.getInvalidCardWithNumberEmpty();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldIncorrectFieldNumberCardMiniNumber() {
            val invalidCardInfo = DataHelper.getInvalidCardWithIncorrectNumber();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldIncorrectFieldNumberCardOtherNumber() {
            val invalidCardInfo = DataHelper.getInvalidCardNumberIfOutOfDatabase();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.declined();
        }
    }

    @Nested
    class shouldInvalidCardFieldMonth {
        @Test
        public void shouldInvalidMonthIfEmpty() {
            val invalidCardNumber = DataHelper.getInvalidMonthIfEmpty();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldMonth();
        }

        @Test
        public void shouldInvalidNumberOfMonthIfMore12() {
            val invalidCardNumber = DataHelper.getInvalidNumberOfMonthIfMore12();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldInvalidExpiredDateNotification();
        }

        @Test
        public void shouldInvalidNumberOfMonthIfOneDigit() {
            val invalidCardNumber = DataHelper.getInvalidNumberOfMonthWhenOneDigit();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldMonth();
        }

        @Test
        public void shouldInvalidNumberOfMonthIfZero() {
            val invalidCardNumber = DataHelper.getInvalidNumberOfMonthIfZero();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.declined();
        }
    }

    @Nested
    class shouldInvalidCardFieldYear {
        @Test
        public void shouldInvalidYearIfZero() {
            val invalidCardNumber = DataHelper.getInvalidYearIfZero();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldExpiredDatePassNotification();
        }

        @Test
        public void shouldInvalidYearIfInTheFarFuture() {
            val invalidCardNumber = DataHelper.getInvalidYearIfInTheFarFuture();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldInvalidExpiredDateNotification();
        }

        @Test
        public void shouldInvalidNumberOfYearIfOneDigit() {
            val invalidCardNumber = DataHelper.getInvalidNumberOfYearIfOneDigit();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldYear();
        }

        @Test
        public void shouldInvalidYearIfEmpty() {
            val invalidCardNumber = DataHelper.getInvalidYearIfEmpty();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldYear();
        }

        @Test
        public void shouldInvalidYearIfBeforeCurrentYear() {
            val invalidCardInfo = DataHelper.getInvalidPastYear();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldExpiredDatePassNotification();
        }
    }

    @Nested
    class shouldInvalidCardFieldOwner {
        @Test
        public void shouldInvalidCardOwnerNameIfEmpty() {
            val invalidCardInfo = DataHelper.getInvalidCardHolderNameIfEmpty();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldHolder();
        }

        @Test
        public void shouldInvalidCardOwnerNameIfNumericAndSpecialCharacters() {
            val invalidCardInfo = DataHelper.getInvalidCardOwnerNameIfNumericAndSpecialCharacters();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldHolder2();
        }

        @Test
        public void shouldInvalidCardOwnerNameIfRussianLetters() {
            val invalidCardInfo = DataHelper.getInvalidCardHolderNameIfRussianLetters();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldHolder2();
        }
    }

    @Nested
    class shouldInvalidCardFieldCodeCVC {
        @Test
        public void shouldInvalidCvcIfEmpty() {
            val invalidCardInfo = DataHelper.getInvalidCvcIfEmpty();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInfo);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvcIfOneDigit() {
            val invalidCardNumber = DataHelper.getInvalidCvcIfOneDigit();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvcIfTwoDigits() {
            val invalidCardNumber = DataHelper.getInvalidCvcIfTwoDigits();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldInvalidCvvIfThreeZero() {
            val invalidCardNumber = DataHelper.getInvalidCvvIfThreeZero();
            val travelPage = new TravelPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldCodeCVC();
        }
    }
}





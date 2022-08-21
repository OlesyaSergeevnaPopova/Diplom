package ru.netology.diplom.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class CardInfo {
        private String number;
        private String month;
        private String year;
        private String holder;
        private String cvc;
    }

    static Faker faker = new Faker(new Locale("en"));
    static DateTimeFormatter format = DateTimeFormatter.ofPattern("MM");
    static String monthWhenEndOfAction = LocalDate.now().plusMonths(4).format(format);
    static DateTimeFormatter formatYear = DateTimeFormatter.ofPattern("yy");
    static String yearWhenEndOfAction = LocalDate.now().plusYears(2).format(formatYear);
    static String nameHolder = faker.name().fullName();
    static String cvc = Integer.toString(faker.number().numberBetween(100, 999));

    public static CardInfo getValidCardInformation() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder,
                cvc);
    }

    public static CardInfo getInvalidCardInformation() {
        return new CardInfo("4444 4444 4444 4442", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, cvc);

    }
    public static CardInfo getInvalidCardDataIfEmptyAllFields() {
        return new CardInfo("", "", "", "", "");
    }

    public static CardInfo getInvalidCardWithNumberEmpty() {
        return new CardInfo("", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getInvalidCardWithIncorrectNumber() {
        return new CardInfo("4444", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getInvalidCardNumberIfOutOfDatabase() {
        return new CardInfo("5578334456444696",monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getInvalidMonthIfEmpty() {
        return new CardInfo("4444 4444 4444 4441", "",  yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getInvalidNumberOfMonthIfMore12() {
        return new CardInfo("4444 4444 4444 4441", "23", yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getInvalidNumberOfMonthWhenOneDigit() {
        return new CardInfo("4444 4444 4444 4441", "9", yearWhenEndOfAction, nameHolder, cvc);
    }


    public static CardInfo getInvalidNumberOfMonthIfZero() {
        return new CardInfo("4444 4444 4444 4441", "00", yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getInvalidYearIfZero() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, "00", nameHolder, cvc);
    }

    public static CardInfo getInvalidYearIfInTheFarFuture() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, "85", nameHolder, cvc);
    }

    public static CardInfo getInvalidNumberOfYearIfOneDigit() {
        return new CardInfo("4444 4444 4444 4441",  monthWhenEndOfAction, "9", nameHolder, cvc);
    }

    public static CardInfo getInvalidYearIfEmpty() {
        return new CardInfo("4444 4444 4444 4441",  monthWhenEndOfAction, "", nameHolder, cvc);
    }

    public static CardInfo getInvalidPastYear() {
        return new CardInfo("4444 4444 4444 4441",  monthWhenEndOfAction, "18", nameHolder, cvc);
    }
    public static CardInfo getInvalidCardHolderNameIfEmpty() {
        return new CardInfo("4444 4444 4444 4441",  monthWhenEndOfAction, yearWhenEndOfAction, "", cvc);
    }

    public static CardInfo getInvalidCardOwnerNameIfNumericAndSpecialCharacters() {
        return new CardInfo("4444 4444 4444 4441",  monthWhenEndOfAction, yearWhenEndOfAction, "49%953!$", cvc);
    }

    public static CardInfo getInvalidCardHolderNameIfRussianLetters() {
        return new CardInfo("4444 4444 4444 4441",  monthWhenEndOfAction, yearWhenEndOfAction, "Олег Карпов", cvc);
    }

    public static CardInfo getInvalidCvcIfEmpty() {
        return new CardInfo("4444 4444 4444 4441",  monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, "");
    }

    public static CardInfo getInvalidCvcIfOneDigit() {
        return new CardInfo("4444 4444 4444 4441",  monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, "5");
    }

    public static CardInfo getInvalidCvcIfTwoDigits() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, "25");
    }

    public static CardInfo getInvalidCvvIfThreeZero() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, "000");
    }

}


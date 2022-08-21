package ru.netology.diplom.data;

import lombok.val;

import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class BdHelper {
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.username");
    private static final String password = System.getProperty("db.password");

    public BdHelper() {
    }

    public static String getPurchaseByDebitCard() {
        val statusBD = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";

        try (
                val connection = getConnection(url, user, password);
                val payStatus = connection.createStatement()
        ) {
            try (val rs = payStatus.executeQuery(statusBD)) {
                if (rs.next()) {
                    val status = rs.getString(1);
                    return status;
                }
                return null;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String getPurchaseOnCreditCard() { //покупка в кредит
        val statusBD = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";

        try (
                val connection = getConnection(url, user, password);
                val payStatus = connection.createStatement()
        ) {
            try (val rs = payStatus.executeQuery(statusBD)) {
                if (rs.next()) {
                    val status = rs.getString(1);
                    return status;
                }
                return null;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }


    public static void cleanDataBase() { //очистить Базу данных

        val payment = "DELETE FROM payment_entity";
        val credit = "DELETE FROM credit_request_entity";
        val order = "DELETE FROM order_entity";


        try (
                val conn = getConnection(url, user, password);
                val prepareStatCredit = conn.createStatement();
                val prepareStatOrder = conn.createStatement();
                val prepareStatPayment = conn.createStatement()
        ) {
            prepareStatCredit.executeUpdate(credit);
            prepareStatOrder.executeUpdate(order);
            prepareStatPayment.executeUpdate(payment);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }
}
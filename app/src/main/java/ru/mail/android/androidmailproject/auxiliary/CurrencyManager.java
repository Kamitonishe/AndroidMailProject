package ru.mail.android.androidmailproject.auxiliary;

/**
 * Created by dmitrykamaldinov on 11/26/17.
 */

public class CurrencyManager {
    public static String getCurrencyInformation(String baseCurrencyName) {
        String info = baseCurrencyName;
        info += " нормальное название (full name)";
        info += "\n\nИспользуется в странах: ";
        return info;
    }
}

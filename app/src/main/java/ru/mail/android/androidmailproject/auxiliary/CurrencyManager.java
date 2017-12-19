package ru.mail.android.androidmailproject.auxiliary;

/**
 * Created by dmitrykamaldinov on 11/26/17.
 */

public class CurrencyManager {
    private String format;

    public CurrencyManager(String name, String full_name, String russian_name, String[] countries) {
        format = name + "\n\n" + "Full name: " + full_name + "\n" +
                "Russian name: " + russian_name + "\nUsed in countries:\n\t";
        for (String country : countries)
            format += country + ", ";
    }

    public String getFormat() {
        return format;
    }
}

package ru.mail.android.androidmailproject.auxiliary;

/**
 * Created by dmitrykamaldinov on 11/26/17.
 */

public class CurrencyManager {
    private String format;

    public CurrencyManager(String name, String full_name, String russian_name, String part100, String[] countries) {
        format = "\tFull name: " + full_name + "\n" +
                "\tRussian name: " + russian_name + "\n" +
                "\t1/100: " + part100 + "\n\n\tUsed in countries:\n\t";
        for (String country : countries)
            format += country + ", ";
        if (countries.length > 0)
            format = format.substring(0, format.length() - 2);
    }

    public String getFormat() {
        return format;
    }
}

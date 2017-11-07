package ru.mail.android.androidmailproject;

/**
 * Created by dmitrykamaldinov on 10/31/17.
 */

public class Helper {
    public static String aMonthBefore(String date) {
        String[] splited = date.split("-");
        Integer a = Integer.valueOf(splited[1]);
        --a;
        if (a == 0) {
            a = 12;
            Integer b = Integer.valueOf(splited[0]);
            --b;
            splited[0] = b.toString();
        }
        splited[1] = a.toString();
        if (splited[1].length() == 1)
            splited[1] = "0" + splited[1];
        Integer c = Integer.valueOf(splited[2]);
        if (c > 28)
            splited[2] = "28";
        return splited[0] + "-" + splited[1] + "-" + splited[2];
    }
}

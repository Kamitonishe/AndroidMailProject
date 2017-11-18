package ru.mail.android.androidmailproject.auxiliary;

/**
 * Created by dmitrykamaldinov on 10/31/17.
 */

public class DateManager {
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

    public static boolean isLaterThan(String date1, String date2) {
        String[] splited1 = date1.split("-"), splited2 = date2.split("-");

        int y1 = Integer.valueOf(splited1[0]), y2 = Integer.valueOf(splited2[0]);
        if (y1 != y2)
            return y1 > y2;

        int m1 = Integer.valueOf(splited1[1]), m2 = Integer.valueOf(splited2[1]);
        if (m1 != m2)
            return m1 > m2;

        int d1 = Integer.valueOf(splited1[2]), d2 = Integer.valueOf(splited2[2]);
        return d1 > d2;
    }
}

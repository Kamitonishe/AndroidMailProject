package ru.mail.android.androidmailproject.auxiliary;

import static java.lang.StrictMath.max;

/**
 * Created by dmitrykamaldinov on 12/24/17.
 */

public class NumberManager {
    public static double round(double x) {
        String x_ = String.valueOf(x);
        int delta = max(0, x_.length() - 7);
        return Double.valueOf(x_.substring(0, x_.length() - delta));
    }
}

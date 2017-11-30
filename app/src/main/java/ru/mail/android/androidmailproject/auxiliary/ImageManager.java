package ru.mail.android.androidmailproject.auxiliary;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by dmitrykamaldinov on 11/26/17.
 */

public class ImageManager {
    public static Bitmap makeTransparentBackground(Bitmap bitmap) {
        Bitmap bitmap1 = bitmap.copy(bitmap.getConfig() == null ? Bitmap.Config.ARGB_8888 : bitmap.getConfig(), true);

        for (int x = 0; x < bitmap1.getWidth(); ++x) {
            for (int y = 0; y < bitmap1.getHeight(); ++y) {
                if (bitmap1.getPixel(x, y) == Color.WHITE)
                    bitmap1.setPixel(x, y, Color.TRANSPARENT);
            }
        }
        return bitmap1;
    }

    public static Bitmap addBorder(Bitmap bitmap) {
        Bitmap bitmap1 = bitmap.copy(bitmap.getConfig() == null ? Bitmap.Config.ARGB_8888 : bitmap.getConfig(), true);

        for (int x = 0; x < bitmap1.getWidth(); ++x)
            for (int y = 0; y < bitmap1.getHeight(); ++y) {
                if (x == 0 || y == 0 || x == bitmap1.getWidth() - 1 || y == bitmap1.getHeight() - 1)
                    bitmap1.setPixel(x, y, Color.GRAY);
                else
                    y = bitmap.getHeight() - 2;
            }

        return bitmap1;
    }

    public static Bitmap fromBlackToGray(Bitmap bitmap) {

        Bitmap bitmap1 = bitmap.copy(bitmap.getConfig() == null ? Bitmap.Config.ARGB_8888 : bitmap.getConfig(), true);

        for (int x = 0; x < bitmap1.getWidth(); ++x)
            for (int y = 0; y < bitmap1.getHeight(); ++y) {
                int argb = bitmap.getPixel(x, y);
                int r = ((argb>>16)&0xFF) + 119;
                int g = ((argb>>8)&0xFF) + 119;
                int b = ((argb>>0)&0xFF) + 119;
                int a = (argb>>24)&0xFF;
                if (r > 255)
                    r = 255;
                if (g > 255)
                    g = 255;
                if (b > 255)
                    b = 255;
                argb = (((a&0xff)<<24)|(r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
                bitmap1.setPixel(x, y, argb);
            }

        return bitmap1;
    }
}

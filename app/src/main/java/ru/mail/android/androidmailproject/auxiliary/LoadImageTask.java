package ru.mail.android.androidmailproject.auxiliary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import ru.mail.android.androidmailproject.data.CurrenciesSingletone;
import ru.mail.android.androidmailproject.data.ImagesSingltone;

public class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
    private final WeakReference<ImageView> weakIv;
    private final WeakReference<Context> context;
    private final int index;

    public LoadImageTask(Context context, ImageView iv, int index) {
        super();
        weakIv = new WeakReference<>(iv);
        this.context = new WeakReference<>(context);
        this.index = index;
    }


    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;

        InputStream inputStream = null;
        try {
            inputStream = context.get().getAssets().open(getUrlFromIndex(index));
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private static String getUrlFromIndex(int index) {
        String name = CurrenciesSingletone.getInstance().getCurrenciesNames(false)[index];
        return "icons/" + name.toLowerCase() + ".gif";
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled())
            bitmap = null;

        Bitmap bm = ImagesSingltone.getInstance().getBitmapFromMemCache(index);
        ImagesSingltone.getInstance().addBitmapToMemoryCache(index, bitmap);
        if (bm == null)
            bm = bitmap;
        ImageView iv = weakIv.get();
        if (iv != null && this == getBitmapDownloaderTask(iv)) {
            iv.setImageBitmap(bm);
        }
    }

    private static LoadImageTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadDrawable) {
                DownloadDrawable dd = (DownloadDrawable)drawable;
                return dd.getTask();
            }
        }
        return null;
    }

    public String getName() {
        return CurrenciesSingletone.getInstance().getCurrenciesNames(false)[index];
    }


    private static class Utils {
        public static void CopyStream(InputStream is, OutputStream os) {
            final int buffer_size = 1024;
            try {
                byte[] bytes = new byte[buffer_size];
                for (; ; ) {
                    int count = is.read(bytes, 0, buffer_size);
                    if (count == -1)
                        break;
                    os.write(bytes, 0, count);
                }
            } catch (Exception ex) {
            }
        }
    }

    private static class DownloadDrawable extends ColorDrawable {
        private final WeakReference<LoadImageTask> _loadTaskWeak;

        private DownloadDrawable(LoadImageTask loadTask) {
            super(Color.WHITE);
            _loadTaskWeak = new WeakReference<>(loadTask);
        }

        public LoadImageTask getTask() {
            return _loadTaskWeak.get();
        }
    }
}
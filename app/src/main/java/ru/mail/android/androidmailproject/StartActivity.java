package ru.mail.android.androidmailproject;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    private String CURRENT_TIME;
    private long currentTime = 0;
    private final long MAX_TIME = 2000;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        CURRENT_TIME = this.getString(R.string.current_time);

        final Intent intent = new Intent(this, MainActivity.class);


        countDownTimer = new CountDownTimer(MAX_TIME - currentTime, 1) {

            public void onTick(long millisUntilFinished) {
                currentTime = MAX_TIME - millisUntilFinished;
            }

            public void onFinish() {
                startActivity(intent);
                StartActivity.this.finish();
            }
        }.start();
    }

    protected void onStop() {
        super.onStop();
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(CURRENT_TIME, currentTime);
        super.onSaveInstanceState(outState);
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTime = savedInstanceState.getLong(CURRENT_TIME);
    }

}


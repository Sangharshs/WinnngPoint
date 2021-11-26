package com.scratchandwin.sratchwin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        //GOLDEN ACTIVITY
        SharedPreferences getScratchLimit = getSharedPreferences("scratch1", MODE_PRIVATE);
        String updated_scratch_limit = getScratchLimit.getString("scratch_limit", "0");
//        if (updated_scratch_limit.equals("0")){
            SharedPreferences getDate = getSharedPreferences("DATE", MODE_PRIVATE);
            String saved_date = getDate.getString("s_date", "0");

            @SuppressLint("SimpleDateFormat")
            String current_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            if (!saved_date.equals(current_date)) {
                SharedPreferences editValue = getSharedPreferences("scratch1", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = editValue.edit();
                editor1.putString("scratch_limit", String.valueOf(GoldenScratchActivity.DAILY_SCRATCH));
                editor1.apply();

                SharedPreferences editValue1 = getSharedPreferences("bonus", MODE_PRIVATE);
                SharedPreferences.Editor editor111 = editValue1.edit();
                editor111.putString("daily_bonus", "1");
                editor111.apply();
            }

//        }

        SharedPreferences getScratchLimit1 = getSharedPreferences("scratch3", MODE_PRIVATE);
        String updated_scratch_limit1 = getScratchLimit1.getString("scratch_limit", "0");

//        if (updated_scratch_limit1.equals("0") ) {
//            SharedPreferences getDate = getSharedPreferences("DATE", MODE_PRIVATE);
//            String saved_date = getDate.getString("s_date", "0");

           // @SuppressLint("SimpleDateFormat")
          //  String current_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            if (!saved_date.equals(current_date)) {
                SharedPreferences editValue = getSharedPreferences("scratch3", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = editValue.edit();
                editor1.putString("scratch_limit", String.valueOf(GoldenScratchActivity.DAILY_SCRATCH));
                editor1.apply();
            }


//        SharedPreferences getScratchLimit2 = getSharedPreferences("scratch4", MODE_PRIVATE);
//        String updated_scratch_limit2 = getScratchLimit2.getString("scratch_limit", "0");
//
//        if (updated_scratch_limit2.equals("0")) {
//            SharedPreferences getDate = getSharedPreferences("DATE", MODE_PRIVATE);
//            String saved_date = getDate.getString("s_date", "0");
//            finish();
//            Toast.makeText(this, "Your Spin Limit Is Over ", Toast.LENGTH_SHORT).show();
         //   @SuppressLint("SimpleDateFormat")
           // String current_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            if (!saved_date.equals(current_date)) {
                SharedPreferences editValue = getSharedPreferences("scratch4", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = editValue.edit();
                editor1.putString("scratch_limit", String.valueOf(GoldenScratchActivity.DAILY_SCRATCH));
                editor1.apply();
            }

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,
                        MainActivity.class);
                //Intent is used to switch from one activity to another.
                startActivity(i);
                //invoke the SecondActivity.
                finish();
                //the current activity will get finished.
            }
        }, 3000);
    }
}
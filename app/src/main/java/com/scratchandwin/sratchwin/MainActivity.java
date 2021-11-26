package com.scratchandwin.sratchwin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    CardView win_daily_bonus_card, golden_scratch_card,platinum_scratch_card,
             diamond_scratch_card,wallet_card,add_free_scratch,share_and_win;
    TextView totalCoins;
    RewardedVideoAd rewardedVideoAd,rewardedVideoAd1;
    RewardedVideoAdListener rewardedVideoAdListener,rewardedVideoAdListener1;
    int scratchCount = 0;
    final String PREFS_NAME = "MyPrefsFile";
    private static final String ONESIGNAL_APP_ID = "a13020f1-b378-4b16-aa04-57c92580b79d";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        win_daily_bonus_card  = findViewById(R.id.win_daily_bonus);
        golden_scratch_card   = findViewById(R.id.goldenScratch);
        platinum_scratch_card = findViewById(R.id.platinumScratch);
        diamond_scratch_card  = findViewById(R.id.diamondScratch);
        wallet_card           = findViewById(R.id.walletScreen);
        add_free_scratch      = findViewById(R.id.ad_free_scratch);
        share_and_win         = findViewById(R.id.share_and_winCard);
        totalCoins            = findViewById(R.id.total_Coins);

        SharedPreferences editValue = getSharedPreferences("bonus", MODE_PRIVATE);
        String dalywin = editValue.getString("daily_bonus","0");


                Toast.makeText(this, dalywin, Toast.LENGTH_SHORT).show();
                win_daily_bonus_card.setClickable(true);
                win_daily_bonus_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!dalywin.equals("0")){
                            if(rewardedVideoAd1.isAdLoaded()) {
                                rewardedVideoAd1.show();
                                SharedPreferences editValue1 = getSharedPreferences("bonus", MODE_PRIVATE);
                                SharedPreferences.Editor editor = editValue1.edit();
                                editor.putString("daily_bonus","0");
                                editor.apply();
                            }else{
                                Toast.makeText(MainActivity.this, "Ad is not loaded..", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Try Tomorrow", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        horizontal_cards_click();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Load Score
        SharedPreferences coins_s = getSharedPreferences("spin",MODE_PRIVATE);
        String tc = coins_s.getString("Coins","0");
        open_scratch_card_activity();
        totalCoins.setText(tc);

        loadVideoAd();

        load_ad_for_reward();


        @SuppressLint("SimpleDateFormat")
        String current_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        SharedPreferences getDate = getSharedPreferences("DATE_M", MODE_PRIVATE);
        String saved_date = getDate.getString("s_date", "0");


        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(R.layout.privacy_policy, null);
            ProgressBar progressBar = v.findViewById(R.id.progressforprivacy);
            Button cancel = v.findViewById(R.id.cancel);
            Button accept = v.findViewById(R.id.accept);
            WebView webView = v.findViewById(R.id.webview);

            webView.loadUrl("https://sites.google.com/view/scratchandwinprivacy/");
            progressBar.setVisibility(View.GONE);
            //856369125

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setView(v)
                    .setCancelable(false)
                    .create();
            alertDialog.show();
          //  text_coins.setText(r);


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  finish();
                }
            });

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    settings.edit().putBoolean("my_first_time", false).commit();
                }
            });
            // startAlertAtParticularTime();
            //the app is being launched for first time, do something

            // first time task
            // record the fact that the app has been started at least once

        }

    }

    private void loadVideoAd() {
        rewardedVideoAd = new RewardedVideoAd(this, getString(R.string.rewarded_video_ad));
        rewardedVideoAd.loadAd(
                rewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());

        rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
            }

            @Override
            public void onAdLoaded(Ad ad) {

                // Rewarded video ad is loaded and ready to be displayedLog.d("", "Rewarded video ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d("", "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d("", "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                // Call method to give reward
                // giveReward();
            }

            @Override
            public void onRewardedVideoClosed() {
                loadVideoAd();
            }
        };
    }

    private void open_scratch_card_activity() {
        @SuppressLint("SimpleDateFormat")
        String format = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        //  Toast.makeText(getApplicationContext(), format, Toast.LENGTH_SHORT).show();
        SharedPreferences getDate = getSharedPreferences("DATE_M", MODE_PRIVATE);
        SharedPreferences.Editor editor = getDate.edit();
        editor.putString("s_date", format);
        editor.apply();
        //GOLDEN SCRATCH OPEN
        golden_scratch_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rewardedVideoAd.isAdLoaded()){
                    rewardedVideoAd.show();
                }
                Intent intent = new Intent(MainActivity.this, GoldenScratchActivity.class);
                intent.putExtra("golden","golden_scratch");
                startActivity(intent);
            }
        });
        //DIAMOND SCRATCH OPEN
        diamond_scratch_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rewardedVideoAd.isAdLoaded()){
                    rewardedVideoAd.show();
                }
                Intent intent = new Intent(MainActivity.this, DiamondscratchActivity.class);
                //intent.putExtra("diamond","diamond_scratch");
                startActivity(intent);
            }
        });
        //PLATINUM SCRATCH OPEN
        platinum_scratch_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rewardedVideoAd.isAdLoaded()){
                    rewardedVideoAd.show();
                }
                Intent intent = new Intent(MainActivity.this, PlatinumscratchActivity.class);
                intent.putExtra("platinum","platinum_scratch");
                startActivity(intent);
            }
        });
        //WALLET ACTIVITY OPEN
        wallet_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rewardedVideoAd.isAdLoaded()){
                    rewardedVideoAd.show();
                }
                Intent intent = new Intent(MainActivity.this,WithdrawActivity.class);
                startActivity(intent);
            }
        });
    }

    private void horizontal_cards_click(){
//     win_daily_bonus_card.setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View v) {
//             if(rewardedVideoAd1.isAdLoaded()){
//                 rewardedVideoAd1.show();
//             }
//         }
//     });

     add_free_scratch.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(rewardedVideoAd1.isAdLoaded()){
                 rewardedVideoAd1.show();
             }
         }
     });

     share_and_win.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String urlll = getString(R.string.play_store_link)+getApplicationContext().getPackageName();
             Intent shareIntent = new Intent(Intent.ACTION_SEND);
             shareIntent.setType("text/plain");
             shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                     getString(R.string.app_name) +"\n"+"सभी स्मार्टफोन यूजर ध्यान दे आप के पास ऑनलाइन पैसा कमाने का बहुत अच्छा मौका है। बस थोड़ा सा काम करके आप काफी अच्छा पैसा कमा सकते हैं।\n" +
                             "ये मौका आपको दे रहा है Scratch And Win ऐप इस ऐप से आप हर रोज 150-200 रुपए बहुत ही आसानी से कमा सकते हैं इस हिसाब से आप महीने के 3000 से 6000 रुपए  कमा सकते हैं। इतने पैसे कमाने के लिए आप हर रोज Scratch And Win ऐप पर 10 से 15 मिनट काम करना होगा। और इसी के आपको पैसे मिलेंगे। तो जल्दी से इस ऐप को डाउलोड  करे और पैसा कमाना स्टार्ट करे।"+ "\n\n" + urlll);
             startActivity(Intent.createChooser(shareIntent, "Share via"));

             if(rewardedVideoAd1.isAdLoaded()){
                 rewardedVideoAd1.show();
             }
         }
     });

    }

    private void load_ad_for_reward(){
        rewardedVideoAd1 = new RewardedVideoAd(this, getString(R.string.rewarded_video_ad));


        rewardedVideoAd1.loadAd(
                rewardedVideoAd1.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener1)
                        .build());
        rewardedVideoAdListener1 = new RewardedVideoAdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
             //   Toast.makeText(MainActivity.this, "Ad is loaded", Toast.LENGTH_SHORT).show();
                // Rewarded video ad is loaded and ready to be displayedLog.d("", "Rewarded video ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d("", "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                add_reward_in_wallet();

            }

            @Override
            public void onRewardedVideoClosed() {
                     }
        };
    }

    private void add_reward_in_wallet() {
        Random random = new Random();
        String r = "10";
        SharedPreferences coins_s = getSharedPreferences("spin",MODE_PRIVATE);
        scratchCount = Integer.parseInt(coins_s.getString("Coins", "0"));
        scratchCount = scratchCount + (Integer.parseInt(r));

        SharedPreferences.Editor coinsEdit = coins_s.edit();
        coinsEdit.putString("Coins", String.valueOf(scratchCount));
        coinsEdit.apply();

        Toast.makeText(MainActivity.this, r+" Coins You Win ", Toast.LENGTH_SHORT).show();
        recreate();
    }
}
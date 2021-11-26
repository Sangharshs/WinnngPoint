package com.scratchandwin.sratchwin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DiamondscratchActivity extends AppCompatActivity {
    public static String up_s = null;
    public static int DAILY_SCRATCH = 20;
    TextView textView, daily_scratch_limit_textView;
    int scratchCount;
    RewardedVideoAd rewardedVideoAd;
    BroadcastReceiver broadcastReceiver;
    RewardedVideoAdListener rewardedVideoAdListener;
    String r;
    int daily_scratch_limit = 0;
    SimpleDateFormat currentDate;
    ScratchView scratchView;
    CardView cardView;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    AdView adView;
    private NativeAd nativeAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diamondscratch);
        broadcastReceiver = new NetworkChangeReceiver();
        registerBrodcastReciver();

        SharedPreferences scorecoins = getSharedPreferences("spin",MODE_PRIVATE);
        String tc = scorecoins.getString("Coins","0");
        TextView toolbartext = findViewById(R.id.textview_toolbar);
        toolbartext.setText(tc);
        ImageView finishImageview = findViewById(R.id.finish);
        finishImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        daily_scratch_limit_textView = findViewById(R.id.daily_scratch_limit);
        textView = findViewById(R.id.point_Text);
        scratchView = findViewById(R.id.scratch_view);
        cardView = findViewById(R.id.c);

        if (daily_scratch_limit_textView.getText().toString().equals("0")) {
            finish();
            Toast.makeText(this, "Your Today's Spin Limit Over", Toast.LENGTH_SHORT).show();
        }


        Random random = new Random();
        r = String.valueOf(random.nextInt(10));
        textView.setText(r);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Ad....");
        progressDialog.setCancelable(false);

        rewardedVideoAd = new RewardedVideoAd(this, getString(R.string.rewarded_video_ad));
        rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
               // Toast.makeText(DiamondscratchActivity.this, "" + error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                // Rewarded video ad failed to load
                // Log.e("", "Rewarded video ad failed to load: " + error.getErrorMessage());
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


                SharedPreferences coins_s = getSharedPreferences("spin", MODE_PRIVATE);
                scratchCount = Integer.parseInt(coins_s.getString("Coins", "0"));
                scratchCount = scratchCount + (Integer.parseInt(r));

                SharedPreferences.Editor coinsEdit = coins_s.edit();
                coinsEdit.putString("Coins", String.valueOf(scratchCount));
                coinsEdit.apply();

                Toast.makeText(DiamondscratchActivity.this, "Task Complete", Toast.LENGTH_SHORT).show();
                loadVideoAd();
                recreate();
                // Call method to give reward
                // giveReward();
            }

            @Override
            public void onRewardedVideoClosed() {
                loadVideoAd();
            }
        };
        daily_scratch_limit_textView.setText(String.valueOf(daily_scratch_limit));
        if (scratchCount != 0) {
            SharedPreferences coins_s = getSharedPreferences("spin", MODE_PRIVATE);
            scratchCount = Integer.parseInt(coins_s.getString("Coins", "0"));
            scratchCount = scratchCount + (Integer.parseInt(r));
            SharedPreferences.Editor coinsEdit = coins_s.edit();
            coinsEdit.putString("Coins", String.valueOf(scratchCount));
            coinsEdit.apply();


        }
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {


                int dsl = Integer.parseInt(daily_scratch_limit_textView.getText().toString());
                daily_scratch_limit = dsl;
                dsl--;

                if (dsl < 0) {
                    dsl = 0;
                }

                daily_scratch_limit_textView.setText(String.valueOf(dsl));
                String updated_scratch_limit = daily_scratch_limit_textView.getText().toString();

                if (updated_scratch_limit.equals("19")) {
                    @SuppressLint("SimpleDateFormat")
                    String format = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    //  Toast.makeText(getApplicationContext(), format, Toast.LENGTH_SHORT).show();
                    SharedPreferences getDate = getSharedPreferences("DATE", MODE_PRIVATE);
                    SharedPreferences.Editor editor = getDate.edit();
                    editor.putString("s_date", format);
                    editor.apply();
                }


                SharedPreferences getScratchLimit = getSharedPreferences("scratch4", MODE_PRIVATE);
                SharedPreferences.Editor editor = getScratchLimit.edit();
                editor.putString("scratch_limit", String.valueOf(dsl));
                editor.apply();

                new Handler().
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show_winning_box();
                                // Add the line which you want to run after 5 sec.
                            }
                        }, 4000);
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent >= 0.9) {
                }
            }
        });

    }

    private void registerBrodcastReciver() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisteredBrodcastReciver() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onStart() {
        super.onStart();
        loadVideoAd();


        SharedPreferences getScratchLimit = getSharedPreferences("scratch4", MODE_PRIVATE);
        String updated_scratch_limit = getScratchLimit.getString("scratch_limit", "0");

        //Toast.makeText(this, updated_scratch_limit, Toast.LENGTH_SHORT).show();
        daily_scratch_limit_textView.setText(updated_scratch_limit);
        String s_l_t = daily_scratch_limit_textView.getText().toString();

        int p_d_l = Integer.parseInt(s_l_t);
        int D_P = Integer.parseInt(String.valueOf(DAILY_SCRATCH));
        if (updated_scratch_limit.equals("0")) {
               finish();
            Toast.makeText(this, "आपकी आज की सीमा समाप्त हो गई है। कृपया अगले दिन प्रयास करें।", Toast.LENGTH_SHORT).show();

     }

        load_native_ad();

    }

    private void loadVideoAd() {
        rewardedVideoAd.loadAd(
                rewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());
    }

    public void show_winning_box() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.win_coin_diologe, null);

        Button collect_now_btn = v.findViewById(R.id.get_now_btn);
        Button not_now_btn = v.findViewById(R.id.not_btn);
        TextView text_coins = v.findViewById(R.id.text_coins_alet);

        //856369125

        alertDialog = new AlertDialog.Builder(this)
                .setView(v)
                .setCancelable(false)
                .create();
        alertDialog.show();
        text_coins.setText(r);


        collect_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedVideoAd.isAdLoaded()) {
                    rewardedVideoAd.show();
                } else {
                    //  loadVideoAd();
                    Toast.makeText(DiamondscratchActivity.this, "विज्ञापन देखने के बाद ही आपके खाते में पैसा डाला जाएगा|",Toast.LENGTH_LONG).show();
                    recreate();
                }
                alertDialog.cancel();
            }
        });


        not_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(DiamondscratchActivity.this, "Coins Are Not Collected", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });
        // startAlertAtParticularTime();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                  finish();
//                  return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisteredBrodcastReciver();
        // alertDialog.dismiss();
    }

    private void load_native_ad() {


        nativeAd = new NativeAd(this,getString(R.string.native_ad_fb));
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd);

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        // Request an ad
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }
    public void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        NativeAdLayout nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(DiamondscratchActivity.this);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        NativeAdLayout adView = (NativeAdLayout) inflater.inflate(R.layout.native_ad_fb_ayout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(DiamondscratchActivity.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }
}
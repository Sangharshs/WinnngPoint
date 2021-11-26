package com.scratchandwin.sratchwin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class WithdrawActivity extends AppCompatActivity {

    LinearLayout paytmView, googlePayView;
    RadioGroup radioGroup;
    RadioButton googlepay,paytm;
    int selectedID;
     String minimumCoins = "6000";
     String total_amount;
     EditText p_number,g_number;
     String paytm_num, google_pay;
     CardView submitCardpaytm,submit_catd_gpay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        paytmView  = findViewById(R.id.paytmView);
        googlePayView = findViewById(R.id.googlepayView);
        radioGroup = findViewById(R.id.radio_group);
        googlepay  = findViewById(R.id.radio_google_pay);
        paytm      = findViewById(R.id.radio_paytm);
        p_number   = findViewById(R.id.paytm_editText);
        g_number   = findViewById(R.id.g_edittext);
        submitCardpaytm = findViewById(R.id.submit_paytm);
        submit_catd_gpay = findViewById(R.id.submit_gpay);

        selectedID = radioGroup.getCheckedRadioButtonId();

            SharedPreferences coins_s = getSharedPreferences("spin",MODE_PRIVATE);

            total_amount = coins_s.getString("Coins","0");

            googlepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytmView.setVisibility(GONE);
                googlePayView.setVisibility(View.VISIBLE);
            }
        });

            paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googlePayView.setVisibility(GONE);
                paytmView.setVisibility(View.VISIBLE);
            }
        });

            submitCardpaytm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (p_number.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Your Number", Toast.LENGTH_SHORT).show();
                    } else {
                        send_payment_request_with_paytm();
                    }
                }
            });

            submit_catd_gpay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (g_number.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Enter Your Number", Toast.LENGTH_SHORT).show();

                    } else {
                        send_payment_request_with_gpay();
                                   }
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences coins_s = getSharedPreferences("spin",MODE_PRIVATE);
        String tc = coins_s.getString("Coins","0");
        if(Integer.parseInt(minimumCoins) <= Integer.parseInt(tc)){

        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(WithdrawActivity.this).create();
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setTitle("Your Account Not Enough Balance");
            alertDialog.setMessage("6000 minimum balance");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.show();
        }
    }

    private void send_payment_request_with_gpay() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://filmirock.xyz/winner/api/"+"payment_request.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Success")){
                            Toast.makeText(WithdrawActivity.this, "Done", Toast.LENGTH_LONG).show();
                            SharedPreferences getshared = getSharedPreferences("spin",MODE_PRIVATE);
                            String zero  = getshared.getString("Coins","0");
                            SharedPreferences.Editor coinsEdit = getshared.edit();
                            coinsEdit.putString("Coins", "0");
                            coinsEdit.apply();
                            Toast.makeText(WithdrawActivity.this, response, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            // progressDialog.dismiss();
                            Toast.makeText(WithdrawActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WithdrawActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<String, String>();

                google_pay = g_number.getText().toString();
                final String value = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId()))
                        .getText().toString();


                params.put("number",google_pay);
                params.put("method",value);
                params.put("amount",total_amount);

                return params;             }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void send_payment_request_with_paytm(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://filmirock.xyz/winner/api/"+"payment_request.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Success")){
                            Toast.makeText(WithdrawActivity.this, "Done", Toast.LENGTH_LONG).show();
                            SharedPreferences getshared = getSharedPreferences("spin",MODE_PRIVATE);
                            String zero  = getshared.getString("Coins","0");
                            SharedPreferences.Editor coinsEdit = getshared.edit();
                            coinsEdit.putString("Coins", "0");
                            coinsEdit.apply();
                            Toast.makeText(WithdrawActivity.this, response, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            // progressDialog.dismiss();
                            Toast.makeText(WithdrawActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WithdrawActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<String, String>();
                // google_pay = g_number.getText().toString();
                paytm_num = p_number.getText().toString();
                final String value = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId()))
                        .getText().toString();

                params.put("number", paytm_num);
                params.put("method",value);
                params.put("amount",total_amount);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
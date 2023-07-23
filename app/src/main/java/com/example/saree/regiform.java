package com.example.saree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class regiform extends AppCompatActivity {

    private Button regis;
    private AdManagerAdView mAdManagerAdView;
    DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();

    EditText billnoedittext,phoneedittext,descedittext,itemsedittext;
    ProgressBar progressbar;

    // One Button




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiform);

        billnoedittext=findViewById(R.id.billno);
        phoneedittext=findViewById(R.id.phone);
        descedittext=findViewById(R.id.desc);
        itemsedittext=findViewById(R.id.items);
        progressbar=findViewById(R.id.progress);
        progressbar.setVisibility(View.GONE);
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdManagerAdView = findViewById(R.id.regiadManagerAdView);
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        mAdManagerAdView.loadAd(adRequest);


        regis=findViewById(R.id.btnregs);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String billno = billnoedittext.getText().toString().trim();
                String phone = phoneedittext.getText().toString().trim();
                String desc = descedittext.getText().toString().trim();
                String items = itemsedittext.getText().toString().trim();

                    // Validate input values
                    if (billno.isEmpty()) {
                        billnoedittext.setError("Please enter your name");
                        billnoedittext.requestFocus();
                    } else if (phone.isEmpty()) {
                        phoneedittext.setError("Please enter your phone number");
                        phoneedittext.requestFocus();
                    } else if (phone.length() != 10) {
                        phoneedittext.setError("Please enter a valid 10-digit phone number");
                        phoneedittext.requestFocus();

                    } else if (desc.isEmpty()) {
                        descedittext.setError("Please enter a description");
                        descedittext.requestFocus();

                    } else if (items.isEmpty()) {
                        itemsedittext.setError("Please enter the number of items");
                        itemsedittext.requestFocus();

                    } else {
                        // Inputs are valid, proceed with your logic

                        Map<String,Object> task =new HashMap<>();
                        task.put("billno",billno);
                        task.put("desc",desc);
                        task.put("phone",phone);
                        task.put("item",items);
                        task.put("date",date);
                        String ukey = mdatabase.child("entries").push().getKey();
                        task.put("id",ukey);
                        progressbar.setVisibility(View.VISIBLE);

                        mdatabase.child("entries").child(ukey).setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    // Data upload successful
                                    progressbar.setVisibility(View.GONE);
                                    billnoedittext.getText().clear();
                                    itemsedittext.getText().clear();
                                    phoneedittext.getText().clear();
                                    descedittext.getText().clear();

                                    Toast.makeText(regiform.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Data upload failed
                                    progressbar.setVisibility(View.GONE);
                                    Toast.makeText(regiform.this, "Registration Fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
            }



        });
    }
}
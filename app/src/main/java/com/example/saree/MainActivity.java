package com.example.saree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context;
    ImageView nointernet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nointernet = findViewById(R.id.nointernet);


        Button entry = findViewById(R.id.entries);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, entries.class);
                startActivity(intent);
            }
        });


        Button registerbutton = findViewById(R.id.register);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, regiform.class);
                startActivity(intent);
            }
        });

      /*
        if(!isconnected()){
            Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();
            nointernet.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isconnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    */

    }

}
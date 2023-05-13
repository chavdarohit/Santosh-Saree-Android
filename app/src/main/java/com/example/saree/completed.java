package com.example.saree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class completed extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
   completeadapter completeadapter;

    ArrayList<entriesclass> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        recyclerView = findViewById(R.id.ccompletedlist);
        database = FirebaseDatabase.getInstance().getReference("completed");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        completeadapter = new completeadapter(this, list);
        recyclerView.setAdapter(completeadapter);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datsnapshot : snapshot.getChildren()) {
                    entriesclass ent = datsnapshot.getValue(entriesclass.class);
                    list.add(ent);
                }
                completeadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
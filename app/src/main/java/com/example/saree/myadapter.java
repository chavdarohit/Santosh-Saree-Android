package com.example.saree;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.MyViewHolder> {

    Context context;

    ArrayList<entriesclass> list;
    public myadapter(Context context, ArrayList<entriesclass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        entriesclass ent= list.get(position);
        holder.phone.setText(ent.getPhone());
        holder.name.setText(ent.getName());
        holder.item.setText(ent.getItem());
        holder.date.setText(ent.getDate());


        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Task Completed SMS sended", Toast.LENGTH_SHORT).show();
                holder.done.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("entries");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure you want to delete ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do something when OK button is clicked
                        String ukey = list.get(position).getId();
                        databaseReference.child(ukey).removeValue();
                        list.remove(position);
                        notifyItemRemoved(position);



                        Toast.makeText(context, "Entry Deleted", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do something when Cancel button is clicked
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();





            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,item,date,phone;
        AppCompatButton delete,done;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            phone=itemView.findViewById(R.id.cardphone);
            name=itemView.findViewById(R.id.cardname);
            item=itemView.findViewById(R.id.carditemno);
            date=itemView.findViewById(R.id.carddate);
            this.delete= itemView.findViewById(R.id.deleteButton);
            this.done=itemView.findViewById(R.id.doneButton);

        }
    }
}

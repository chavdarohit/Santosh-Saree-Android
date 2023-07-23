package com.example.saree;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class completeadapter extends RecyclerView.Adapter<completeadapter.MyViewHolder> {
    ArrayList<entriesclass> list;
    Context context;

    public void setFilteredList(ArrayList<entriesclass> filteredList)
    {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    public completeadapter(Context context, ArrayList<entriesclass> list)
    {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.completed,parent,false);
        return new completeadapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        entriesclass ent= list.get(position);
        holder.phone.setText(ent.getPhone());
        holder.billno.setText(ent.getBillno());
        holder.item.setText(ent.getItem());
        holder.date.setText(ent.getDate());
        holder.desc.setText(ent.getDesc());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("completed");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure you want to delete ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do something when OK button is clicked

                        //-------------------------------------------Main code for deleteing the specific entry---------------------------

                        String ukey = list.get(position).getId();
                        list.remove(position);
                        databaseReference.child(ukey).removeValue();
                        notifyItemRemoved(position);
                        notifyDataSetChanged();

                        //------------Refreshing the activity from the adapter----------------
                        if (context instanceof Activity) {
                            ((Activity) context).recreate();
                        }

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

        TextView billno,item,date,phone,desc;
        AppCompatButton delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            desc=itemView.findViewById(R.id.carddesc);
            phone=itemView.findViewById(R.id.cardphone);
            billno =itemView.findViewById(R.id.cardbillno);
            item=itemView.findViewById(R.id.carditemno);
            date=itemView.findViewById(R.id.carddate);
            this.delete = itemView.findViewById(R.id.delcompleted);


        }
    }

}

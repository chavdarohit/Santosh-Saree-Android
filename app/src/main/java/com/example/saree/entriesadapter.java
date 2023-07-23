package com.example.saree;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class entriesadapter extends RecyclerView.Adapter<entriesadapter.MyViewHolder>{

    Context context;

    ArrayList<entriesclass> list;
    ArrayList<entriesclass> listfull;

    public void setFilteredList(ArrayList<entriesclass> filteredList)
    {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    public entriesadapter(Context context, ArrayList<entriesclass> list) {
        this.context = context;
        this.list = list;
        listfull = new ArrayList<>(list);
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
        holder.billno.setText(ent.getBillno());
        holder.item.setText(ent.getItem());
        holder.date.setText(ent.getDate());
        holder.desc.setText(ent.getDesc());

        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //------------Taking the data completed data from the database-------------------
                String billno = list.get(position).getBillno();
                String id = list.get(position).getId();
                String date = list.get(position).getDate();
                String desc = list.get(position).getDesc();
                String item = list.get(position).getItem();
                String phone = list.get(position).getPhone();

                //---------------------Sending SMS to the Client------------------------------------------

                String wurl = "https://wa.me/+91"+phone+"?text=પ્રિય ગ્રાહક ,\n" +
                        "તમારા Bill No "+billno+" ની તમામ વસ્તુઓ તૈયાર છે.\n" +
                        "કૃપા કરીને bill સાથે લાવવા વિનંતી.\n" +
                        "આભાર,\n" +
                        "સંતોષ રાજકોટ";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wurl));
                context.startActivity(intent);



                //-------------Adding this Item into completed list in database-------------------------------
                Map<String,Object> task =new HashMap<>();
                task.put("billno",billno);
                task.put("desc",desc);
                task.put("phone",phone);
                task.put("item",item);
                task.put("id",id);
                task.put("date",date);
                DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
                mdatabase.child("completed").child(id).setValue(task);

                //---------Deleting from Entries list ---------------------------------
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("entries");
                list.remove(position);
                databaseReference.child(id).removeValue();
                notifyItemRemoved(position);
                notifyDataSetChanged();

                //------------Refreshing the activity from the adapter----------------
                if (context instanceof Activity) {
                    ((Activity) context).recreate();
                }
                Toast.makeText(context, "SMS sended Successfully", Toast.LENGTH_SHORT).show();
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
        AppCompatButton delete,done;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            desc=itemView.findViewById(R.id.carddesc);
            phone=itemView.findViewById(R.id.cardphone);
            billno =itemView.findViewById(R.id.cardbillno);
            item=itemView.findViewById(R.id.carditemno);
            date=itemView.findViewById(R.id.carddate);

            this.delete= itemView.findViewById(R.id.deleteButton);
            this.done=itemView.findViewById(R.id.doneButton);

        }
    }
}

package com.example.securemessaging;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsAdapter  extends RecyclerView.Adapter<ContactsAdapter.contactsViewHolder> {
    Activity activity;
    ArrayList<ContactModel> arrayList;
    private RecyclerViewInterface recyclerViewInterface;

    public ContactsAdapter(Activity activity, ArrayList<ContactModel> arrayList, RecyclerViewInterface recyclerViewInterface) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.recyclerViewInterface = recyclerViewInterface;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public contactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_for_contacts_items, parent, false);
        return new contactsViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull contactsViewHolder holder, int position) {

        ContactModel model = arrayList.get(position);
        holder.Contname.setText(model.getName());
        holder.Contnumber.setText(model.getNumber());

    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }






    public class contactsViewHolder extends RecyclerView.ViewHolder {

        TextView Contname, Contnumber;

        public contactsViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            Contname = itemView.findViewById(R.id.contactsName);
            Contnumber = itemView.findViewById(R.id.contactNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null ){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}

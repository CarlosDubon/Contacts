package com.bonusteam.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolderContactAdapter> {

    ArrayList<Contacto> contactosList = new ArrayList<>();

    public ContactAdapter(ArrayList<Contacto> contactosList){
        this.contactosList = contactosList;
    }

    public static class ViewHolderContactAdapter extends RecyclerView.ViewHolder{
        LinearLayout linearLayout_main;
        ImageView imageView_contact;
        TextView textView_name;
        TextView textView_number;
        TextView textView_call;
        public ViewHolderContactAdapter(View itemView) {
            super(itemView);
            linearLayout_main = itemView.findViewById(R.id.container_main);
            imageView_contact = itemView.findViewById(R.id.contact_image);
            textView_name = itemView.findViewById(R.id.contact_name);
            textView_number = itemView.findViewById(R.id.contact_number);
            textView_call = itemView.findViewById(R.id.btn_call);
        }
    }

    @NonNull
    @Override
    public ViewHolderContactAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_contacts,parent,false);
        return new ViewHolderContactAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContactAdapter holder, int position) {
        holder.imageView_contact.setImageResource(contactosList.get(position).getImagen());
        holder.textView_name.setText(contactosList.get(position).getName());
        holder.textView_number.setText(contactosList.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }
}

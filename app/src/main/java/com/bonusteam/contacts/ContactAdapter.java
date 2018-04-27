package com.bonusteam.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolderContactAdapter> {

    ArrayList<Contacto> contactosList;

    public ContactAdapter(ArrayList<Contacto> contactosList){
        this.contactosList = contactosList;
    }

    public static class ViewHolderContactAdapter extends RecyclerView.ViewHolder{
        LinearLayout linearLayout_main;
        ImageView imageView_contact;
        TextView textView_name;
        TextView textView_number;
        ImageButton textView_call;
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
        holder.textView_name.setText(contactosList.get(position).getName());
        holder.textView_number.setText(contactosList.get(position).getNumber());
        if(contactosList.get(position).getImagen() != null){
            holder.imageView_contact.setImageBitmap(contactosList.get(position).getImagen());
        }else{
            holder.imageView_contact.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }
}

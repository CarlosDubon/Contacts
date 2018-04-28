package com.bonusteam.contacts;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolderContactAdapter> {

    ArrayList<Contacto> contactosList;
    Dialog dialog;

    public ContactAdapter(ArrayList<Contacto> contactosList){
        this.contactosList = contactosList;
    }

    public static class ViewHolderContactAdapter extends RecyclerView.ViewHolder{
        LinearLayout linearLayout_main;
        ImageView imageView_contact;
        TextView textView_name;
        TextView textView_number;
        TextView textView_fav;
        ImageButton textView_call;
        public ViewHolderContactAdapter(View itemView) {
            super(itemView);
            linearLayout_main = itemView.findViewById(R.id.container_main);
            imageView_contact = itemView.findViewById(R.id.contact_image);
            textView_name = itemView.findViewById(R.id.contact_name);
            textView_number = itemView.findViewById(R.id.contact_number);
            textView_fav = itemView.findViewById(R.id.text_favorite);
            textView_call = itemView.findViewById(R.id.btn_call);


        }
    }

    @NonNull
    @Override
    public ViewHolderContactAdapter onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_contacts,parent,false);
        final ViewHolderContactAdapter vHolder = new ViewHolderContactAdapter(view);

        //Dialog initialization
        dialog = new Dialog(parent.getContext());
        dialog.setContentView(R.layout.dialog_contact);



        vHolder.linearLayout_main.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ImageView imgContactDiag = dialog.findViewById(R.id.contact_image_dialog);
                TextView nameContactDiag = dialog.findViewById(R.id.text_name_dialog);
                TextView phoneContactDiag = dialog.findViewById(R.id.text_number_dialog);
                TextView emailContactDiag = dialog.findViewById(R.id.text_email_dialog);
                TextView callContactDiag = dialog.findViewById(R.id.text_call);
                TextView shareContactDiag = dialog.findViewById(R.id.text_share);
                FloatingActionButton btnFavorite = dialog.findViewById(R.id.btn_fav);;


                nameContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getName());
                phoneContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getNumber());
                emailContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getEmail());


                if(contactosList.get(vHolder.getAdapterPosition()).getImagen() != null){
                    imgContactDiag.setImageBitmap(contactosList.get(vHolder.getAdapterPosition()).getImagen());
                }else{
                    imgContactDiag.setImageResource(R.drawable.ic_launcher_background);
                }

                dialog.show();

                btnFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(contactosList.get(vHolder.getAdapterPosition()).isFavorite()){
                            removeFavorite(vHolder.getAdapterPosition());
                            dialog.dismiss();
                            Snackbar.make(parent,vHolder.textView_name.getText().toString() +" removed to favorites",Snackbar.LENGTH_SHORT).show();
                        }else{
                            addFavorite(vHolder.getAdapterPosition());
                            dialog.dismiss();
                            Snackbar.make(parent,vHolder.textView_name.getText().toString() +" added to favorites",Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderContactAdapter holder, final int position) {
        holder.textView_name.setText(contactosList.get(position).getName());
        holder.textView_number.setText(contactosList.get(position).getNumber());
        if(contactosList.get(position).getImagen() != null){
            holder.imageView_contact.setImageBitmap(contactosList.get(position).getImagen());
        }else{
            holder.imageView_contact.setImageResource(R.drawable.ic_launcher_background);
        }
        if(contactosList.get(position).isFavorite()){
            holder.textView_fav.setVisibility(View.VISIBLE);
        }else {
            holder.textView_fav.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }

    public abstract void addFavorite(int index);
    public abstract void removeFavorite(int index);
}

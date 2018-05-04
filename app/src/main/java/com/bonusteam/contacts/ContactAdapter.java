package com.bonusteam.contacts;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public abstract class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolderContactAdapter> implements Parcelable{

    ArrayList<Contacto> contactosList;
    private   Dialog dialog;
    private   Context contex;

    public ContactAdapter(ArrayList<Contacto> contactosList, Context context){
        this.contactosList = contactosList;
        this.contex = context;
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
                if(contex.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ImageView imgContactDiag = dialog.findViewById(R.id.contact_image_dialog);
                    final TextView nameContactDiag = dialog.findViewById(R.id.text_name_dialog);
                    final TextView phoneContactDiag = dialog.findViewById(R.id.text_number_dialog);
                    final TextView emailContactDiag = dialog.findViewById(R.id.text_email_dialog);
                    TextView birthContactDiag = dialog.findViewById(R.id.text_birth_dialog);
                    TextView callContactDiag = dialog.findViewById(R.id.text_call);
                    TextView shareContactDiag = dialog.findViewById(R.id.text_share);
                    FloatingActionButton btnFavorite = dialog.findViewById(R.id.btn_fav);

                    String phones="";
                    for(int i=0;i<contactosList.get(vHolder.getAdapterPosition()).getNumbers().size();i++){
                        phones = phones + contactosList.get(vHolder.getAdapterPosition()).getNumbers().get(i) + "\n";
                    }

                    nameContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getName());
                    phoneContactDiag.setText(phones);
                    emailContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getEmail());
                    birthContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getBirth());

                    if (contactosList.get(vHolder.getAdapterPosition()).getImagen() != null) {
                        imgContactDiag.setImageBitmap(contactosList.get(vHolder.getAdapterPosition()).getImagen());
                    } else {
                        Random random = new Random();
                        int p = random.nextInt(3);
                        switch (p) {
                            case 0:
                                imgContactDiag.setImageResource(R.drawable.default_image_blue);
                                break;
                            case 1:
                                imgContactDiag.setImageResource(R.drawable.default_image_green);
                                break;
                            case 2:
                                imgContactDiag.setImageResource(R.drawable.default_image_red);
                                break;
                        }
                    }

                    dialog.show();

                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (contactosList.get(vHolder.getAdapterPosition()).isFavorite()) {
                                removeFavorite(vHolder.getAdapterPosition());
                                dialog.dismiss();
                                Snackbar.make(parent, vHolder.textView_name.getText().toString() + " removed to favorites", Snackbar.LENGTH_SHORT).show();
                            } else {
                                addFavorite(vHolder.getAdapterPosition());
                                dialog.dismiss();
                                Snackbar.make(parent, vHolder.textView_name.getText().toString() + " added to favorites", Snackbar.LENGTH_SHORT).show();

                            }
                        }
                    });
                    callContactDiag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addRecents(vHolder.getAdapterPosition());
                            callContact(phoneContactDiag.getText().toString());
                        }
                    });
                    shareContactDiag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareContact(nameContactDiag.getText().toString() + "\n" + phoneContactDiag.getText().toString() + "\n" + emailContactDiag.getText().toString());
                        }
                    });
                }else if(contex.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    Bundle bundle = new Bundle();
                    if(contactosList.get(vHolder.getAdapterPosition()).getImagen() != null) {
                        bundle.putByteArray("CONTACT_IMAGE", compressBitmap(contactosList.get(vHolder.getAdapterPosition()).getImagen()));
                    }
                    String phones="";
                    for(int i=0;i<contactosList.get(vHolder.getAdapterPosition()).getNumbers().size();i++){
                        phones = phones+contactosList.get(vHolder.getAdapterPosition()).getNumbers().get(i) + "\n";
                    }
                    bundle.putString("CONTACT_NAME",contactosList.get(vHolder.getAdapterPosition()).getName());
                    bundle.putString("CONTACT_PHONE",phones);
                    bundle.putString("CONTACT_BIRTH",contactosList.get(vHolder.getAdapterPosition()).getBirth());
                    bundle.putString("CONTACT_EMAIL",contactosList.get(vHolder.getAdapterPosition()).getEmail());

                    ContactDetailFragment contactDetailFragment = new ContactDetailFragment();
                    contactDetailFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((Activity)v.getContext()).getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.detail_contact_view,contactDetailFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderContactAdapter holder, final int position) {
        holder.textView_name.setText(contactosList.get(position).getName());
        holder.textView_number.setText(contactosList.get(position).getNumbers().get(0));
        if(contactosList.get(position).getImagen() != null){
            holder.imageView_contact.setImageBitmap(contactosList.get(position).getImagen());
        }else{
            Random random = new Random();
            int p = random.nextInt(3);
            switch (p){
                case 0:
                    holder.imageView_contact.setImageResource(R.drawable.default_image_blue);
                    break;
                case 1:
                    holder.imageView_contact.setImageResource(R.drawable.default_image_green);
                    break;
                case 2:
                    holder.imageView_contact.setImageResource(R.drawable.default_image_red);
                    break;
            }

        }
        if(contactosList.get(position).isFavorite()){
            holder.textView_fav.setVisibility(View.VISIBLE);
        }else {
            holder.textView_fav.setVisibility(View.INVISIBLE);
        }
        holder.textView_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecents(position);
                callContact(holder.textView_number.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }
    public void callContact(String phone){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone));
        try {
            contex.startActivity(callIntent);
        }catch (SecurityException e){
            e.getMessage();
        }
    }

    public void shareContact(String info){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        Bundle bundle = new Bundle();
        bundle.putString(Intent.EXTRA_TEXT,info);
        shareIntent.putExtras(bundle);
        contex.startActivity(shareIntent);

    }

    public void setFilter(ArrayList<Contacto> contactosList){
        this.contactosList = new ArrayList<>();
        this.contactosList.addAll(contactosList);
        notifyDataSetChanged();
    }

    private byte[] compressBitmap(Bitmap bm){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    public abstract void addFavorite(int index);
    public abstract void removeFavorite(int index);
    public abstract void addRecents(int index);


}

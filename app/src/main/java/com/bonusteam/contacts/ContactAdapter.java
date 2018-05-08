package com.bonusteam.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public abstract class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolderContactAdapter> implements Parcelable{
    private int PERMISSIONS_REQUEST_CALL_PHONE = 99;
    private int PERMISSION_MANAGE_DOCUMENTS = 97;
    private ManagerAdministrator managerAdministrator;
    ArrayList<Contacto> contactosList;
    private   Dialog dialogContact;
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
        dialogContact = new Dialog(parent.getContext());
        dialogContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogContact.setContentView(R.layout.dialog_contact);



        vHolder.linearLayout_main.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(contex.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ImageView imgContactDiag = dialogContact.findViewById(R.id.contact_image_dialog);
                    final TextView nameContactDiag = dialogContact.findViewById(R.id.text_name_dialog);
                    final TextView phoneContactDiag = dialogContact.findViewById(R.id.text_number_dialog);
                    final TextView emailContactDiag = dialogContact.findViewById(R.id.text_email_dialog);
                    TextView birthContactDiag = dialogContact.findViewById(R.id.text_birth_dialog);
                    TextView callContactDiag = dialogContact.findViewById(R.id.text_call);
                    TextView shareContactDiag = dialogContact.findViewById(R.id.text_share);
                    TextView editContactDiag = dialogContact.findViewById(R.id.text_edit_dilog);
                    TextView deleteContactDiag = dialogContact.findViewById(R.id.text_delete_dialog);
                    FloatingActionButton btnFavorite = dialogContact.findViewById(R.id.btn_fav);
                    String textNoAviable = contex.getResources().getString(R.string.text_no_aviable);
                    String phones="";
                    for(int i=0;i<contactosList.get(vHolder.getAdapterPosition()).getNumbers().size();i++){
                        phones = phones + contactosList.get(vHolder.getAdapterPosition()).getNumbers().get(i) + "\n";
                    }

                    if(!contactosList.get(vHolder.getAdapterPosition()).getName().equals("")) {
                        nameContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getName());
                    }else{
                        nameContactDiag.setText(textNoAviable);
                    }
                    if(!phones.equals("")) {
                        phoneContactDiag.setText(phones);
                    }else{
                        phoneContactDiag.setText(textNoAviable);
                    }
                    if(!contactosList.get(vHolder.getAdapterPosition()).getEmail().equals("")) {
                        emailContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getEmail());
                    }else{
                        emailContactDiag.setText(textNoAviable);
                    }

                    if(!contactosList.get(vHolder.getAdapterPosition()).getBirth().equals("")) {
                        birthContactDiag.setText(contactosList.get(vHolder.getAdapterPosition()).getBirth());
                    }else{
                        birthContactDiag.setText(textNoAviable);
                    }

                    if (contactosList.get(vHolder.getAdapterPosition()).getImagen() != null){
                        imgContactDiag.setImageURI(Uri.parse(contactosList.get(vHolder.getAdapterPosition()).getImagen()));
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
                    dialogContact.show();


                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (contactosList.get(vHolder.getAdapterPosition()).isFavorite()) {
                                removeFavorite(vHolder.getAdapterPosition());
                                dialogContact.dismiss();
                                Snackbar.make(parent, vHolder.textView_name.getText().toString() + " " +contex.getResources().getString(R.string.text_contact_removed), Snackbar.LENGTH_SHORT).show();
                            } else {
                                addFavorite(vHolder.getAdapterPosition());
                                dialogContact.dismiss();
                                Snackbar.make(parent, vHolder.textView_name.getText().toString() +" " + contex.getResources().getString(R.string.text_contact_added), Snackbar.LENGTH_SHORT).show();

                            }
                        }
                    });
                    editContactDiag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(contex,EditContact.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("CONTACT_OBJ",contactosList.get(vHolder.getAdapterPosition()));
                            bundle.putInt("INDEX",vHolder.getAdapterPosition());
                            intent.putExtras(bundle);
                            contex.getApplicationContext().startActivity(intent);
                        }
                    });
                    callContactDiag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addRecents(vHolder.getAdapterPosition());
                            String phones[] = phoneContactDiag.getText().toString().split("\n");
                            if (ContextCompat.checkSelfPermission(contex, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) contex, new String[]{Manifest.permission.CALL_PHONE},PERMISSIONS_REQUEST_CALL_PHONE);
                                dialogContact.dismiss();
                                dialogContact.show();
                            }
                            callContact(phones[0]);

                        }
                    });
                    shareContactDiag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareContact(nameContactDiag.getText().toString() + "\n" + phoneContactDiag.getText().toString() + "\n" + emailContactDiag.getText().toString());
                        }
                    });
                    deleteContactDiag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(contex);
                            alertDialog.setTitle(R.string.title_alert_dialog);
                            alertDialog.setMessage(R.string.message_alert_dialog);
                            alertDialog.setIcon(R.drawable.ic_delete_black_24dp);

                            alertDialog.setPositiveButton(R.string.text_accept, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(contex instanceof ManagerAdministrator){
                                        ((ManagerAdministrator)contex).deleteContact(contex,contactosList.get(vHolder.getAdapterPosition()),vHolder.getAdapterPosition());
                                        dialogContact.dismiss();
                                    }
                                }
                            });

                            alertDialog.setNegativeButton(R.string.text_calncel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog diag = alertDialog.create();
                            diag.show();

                            Button okButton = diag.getButton(AlertDialog.BUTTON_POSITIVE);
                            Button cancelButton = diag.getButton(AlertDialog.BUTTON_NEGATIVE);

                            okButton.setTextColor(contex.getResources().getColor(R.color.colorPrimaryDark));
                            cancelButton.setTextColor(contex.getResources().getColor(R.color.colorPrimary));


                        }
                    });
                }else if(contex.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("CONTACT",contactosList.get(vHolder.getAdapterPosition()));
                    bundle.putInt("INDEX",vHolder.getAdapterPosition());

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
        if(!contactosList.get(position).getName().equals("")) {
            holder.textView_name.setText(contactosList.get(position).getName());
        }else {
            holder.textView_name.setText(contex.getResources().getString(R.string.text_no_aviable));
        }

        if(!contactosList.get(position).getNumbers().get(0).equals("")) {
            holder.textView_number.setText(contactosList.get(position).getNumbers().get(0));
        }else{
            holder.textView_number.setText(contex.getResources().getString(R.string.text_no_aviable));
        }
        if(contactosList.get(position).getImagen() != null){
            holder.imageView_contact.setImageURI(Uri.parse(contactosList.get(position).getImagen()));
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
                if (ContextCompat.checkSelfPermission(contex, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) contex, new String[]{Manifest.permission.CALL_PHONE},PERMISSIONS_REQUEST_CALL_PHONE);
                }
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

    public abstract void addFavorite(int index);
    public abstract void removeFavorite(int index);
    public abstract void addRecents(int index);


}

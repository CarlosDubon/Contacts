package com.bonusteam.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class ContactDetailFragment extends android.app.Fragment {
    private int PERMISSIONS_REQUEST_CALL_PHONE = 99;
    TextView textViewName,textViewPhone,textViewBirth,textViewEmail,textViewCall,textViewShare,editContact,deleteContact;
    ImageView imageViewContact;
    Contacto contacto;
    FloatingActionButton favoriteBtn;
    private ManagerAdministrator managerAdministrator;
    int index;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_detail,container,false);

        imageViewContact = view.findViewById(R.id.img_contact_add);
        textViewName = view.findViewById(R.id.text_name_dialog);
        textViewPhone=view.findViewById(R.id.text_number_dialog);
        textViewBirth = view.findViewById(R.id.text_birth_dialog);
        textViewEmail = view.findViewById(R.id.text_email_dialog);
        textViewCall = view.findViewById(R.id.text_call);
        textViewShare = view.findViewById(R.id.text_share);
        editContact = view.findViewById(R.id.edit_btn);
        deleteContact = view.findViewById(R.id.delete_btn);
        favoriteBtn = view.findViewById(R.id.favorite_btn);
        String textNoAviable = getActivity().getResources().getString(R.string.text_no_aviable);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            contacto = bundle.getParcelable("CONTACT");
            index = bundle.getInt("INDEX");
        }

        if(contacto.getImagen()!=null) {
            imageViewContact.setImageURI(Uri.parse(contacto.getImagen()));
        }else{
            Random random = new Random();
            int p = random.nextInt(3);
            switch (p) {
                case 0:
                    imageViewContact.setImageResource(R.drawable.default_image_blue);
                    break;
                case 1:
                    imageViewContact.setImageResource(R.drawable.default_image_green);
                    break;
                case 2:
                    imageViewContact.setImageResource(R.drawable.default_image_red);
                    break;
            }
        }
        if(!contacto.getName().equals("")) {
            textViewName.setText(contacto.getName());
        }else{
            textViewName.setText(textNoAviable);
        }
        String phones="";
        for(int i=0;i<contacto.getNumbers().size();i++){
            phones = phones + contacto.getNumbers().get(i)+"\n";
        }
        if(!phones.equals("")) {
            textViewPhone.setText(phones);
        }else{
            textViewPhone.setText(textNoAviable);
        }
        if(!contacto.getBirth().equals("")) {
            textViewBirth.setText(contacto.getBirth());
        }else{
            textViewBirth.setText(textNoAviable);
        }
        if(!contacto.getEmail().equals("")) {
            textViewEmail.setText(contacto.getEmail());
        }else{
            textViewEmail.setText(textNoAviable);
        }

        textViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String phones[] = textViewPhone.getText().toString().split("\n");
                callIntent.setData(Uri.parse("tel:"+phones[0]));
                try {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                    startActivity(callIntent);
                }catch (SecurityException e){
                    e.getMessage();
                }
            }
        });
        textViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                Bundle bundle = new Bundle();
                bundle.putString(Intent.EXTRA_TEXT,textViewName.getText().toString()+"\n"+textViewPhone.getText().toString());
                shareIntent.putExtras(bundle);
                startActivity(shareIntent);
            }
        });

        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditContact.class);
                Bundle bundleSend = new Bundle();
                bundleSend.putParcelable("CONTACT_OBJ",contacto);
                bundleSend.putInt("INDEX",index);
                intent.putExtras(bundleSend);
                startActivity(intent);
            }
        });

        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.title_alert_dialog);
                alertDialog.setMessage(R.string.message_alert_dialog);
                alertDialog.setIcon(R.drawable.delete_icon_black);

                alertDialog.setPositiveButton(R.string.text_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        managerAdministrator.deleteContact(getActivity(),contacto,index);
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

                okButton.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                cancelButton.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
            }
        });
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!contacto.isFavorite()) {
                    managerAdministrator.addContactFavorite(getActivity(), contacto, index);
                }else{
                    managerAdministrator.removeContactFavorite(getActivity(),contacto,index);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ManagerAdministrator){
            managerAdministrator = (ManagerAdministrator) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        managerAdministrator = null;
    }
}

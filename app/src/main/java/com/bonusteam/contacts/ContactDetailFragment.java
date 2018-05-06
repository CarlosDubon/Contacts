package com.bonusteam.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ContactDetailFragment extends android.app.Fragment {
    private int PERMISSIONS_REQUEST_CALL_PHONE = 99;
    TextView textViewName,textViewPhone,textViewBirth,textViewEmail,textViewCall,textViewShare,editContact;
    ImageView imageViewContact;
    Contacto contacto;
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

        Bundle bundle = this.getArguments();
        if(bundle != null){
            contacto = bundle.getParcelable("CONTACT");
            index = bundle.getInt("INDEX");
        }

        imageViewContact.setImageURI(contacto.getImagen());
        textViewName.setText(contacto.getName());
        String phones="";
        for(int i=0;i<contacto.getNumbers().size();i++){
            phones = phones + contacto.getNumbers().get(i)+"\n";
        }
        textViewPhone.setText(phones);
        textViewBirth.setText(contacto.getBirth());
        textViewEmail.setText(contacto.getEmail());

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

        return view;
    }
}

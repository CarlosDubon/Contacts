package com.bonusteam.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ContactDetailFragment extends android.app.Fragment {
    TextView textViewName,textViewPhone,textViewBirth,textViewEmail,textViewCall,textViewShare;
    ImageView imageViewContact;
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

        Bundle bundle = this.getArguments();

        if(bundle != null){
            final InputStream imageStream;
            try {
                if(bundle.getString("CONTACT_IMAGE")!=null) {
                    imageStream = getActivity().getContentResolver().openInputStream(Uri.parse(bundle.getString("CONTACT_IMAGE")));
                    final Bitmap bm = BitmapFactory.decodeStream(imageStream);
                    imageViewContact.setImageBitmap(bm);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            textViewName.setText(bundle.getString("CONTACT_NAME"));
            if(!bundle.getString("CONTACT_PHONE").equals("")){
                textViewPhone.setText(bundle.getString("CONTACT_PHONE"));
            }
            if(!bundle.getString("CONTACT_EMAIL").equals("")) {
                textViewEmail.setText(bundle.getString("CONTACT_EMAIL"));
            }
            if (!bundle.getString("CONTACT_BIRTH").equals("")){
                textViewBirth.setText(bundle.getString("CONTACT_BIRTH"));
            }

        }

        textViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String phones[] = textViewPhone.getText().toString().split("\n");
                callIntent.setData(Uri.parse("tel:"+phones[0]));
                try {
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

        return view;
    }
}

package com.bonusteam.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {

    ImageView imageContact;
    FloatingActionButton loadImage;
    EditText nameContact,phoneContact,emailContact;
    TextView birth;

    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        loadImage = findViewById(R.id.btn_addImage_contact_add);
        imageContact = findViewById(R.id.img_contact_add);
        nameContact = findViewById(R.id.name_contact_add);
        phoneContact = findViewById(R.id.phone_contact_add);
        emailContact = findViewById(R.id.email_contact_add);
        birth = findViewById(R.id.birth_contact_add);

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageContact.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_contact_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contact_tb:
                Contacto contacto = new Contacto();
                //contacto.setImagen(convertImageViewToBitmap(imageContact));
                contacto.setName(nameContact.getText().toString());
                contacto.setNumber(phoneContact.getText().toString());
                contacto.setEmail(emailContact.getText().toString());
                contacto.setBirth(birth.getText().toString());
                Intent intent = new Intent(this,MainActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("NEW_CONTACT",contacto);
                intent.putExtra("CUSTOM_IMAGE",convertImageViewToBitmap(imageContact));
                intent.putExtras(b);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private byte[] convertImageViewToBitmap(ImageView v){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bm =((BitmapDrawable)v.getDrawable()).getBitmap();
        bm.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }
}

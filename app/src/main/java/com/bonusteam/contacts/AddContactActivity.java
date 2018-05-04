package com.bonusteam.contacts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

public class AddContactActivity extends AppCompatActivity {

    private ImageView imageContact;
    private FloatingActionButton loadImage;
    private EditText nameContact,phoneContact,emailContact,birth;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private Uri imageUri = null;

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

        birth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v,boolean hasFocus) {
                if(hasFocus) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(AddContactActivity.this, android.R.style.Theme_Holo_Light_Dialog, datePickerListener, year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String birthString = year +"-"+month+"-"+dayOfMonth;
                birth.setText(birthString);
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            imageUri = uri;
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
                contacto.setName(nameContact.getText().toString());
                contacto.setNumbers(phoneContact.getText().toString());
                contacto.setEmail(emailContact.getText().toString());
                contacto.setBirth(birth.getText().toString());
                Intent intent = new Intent(this,MainActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("NEW_CONTACT",contacto);
                if(imageUri != null) {
                    b.putString("Uri_IMAGE", imageUri.toString());
                }
                intent.putExtras(b);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

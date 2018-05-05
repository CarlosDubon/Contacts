package com.bonusteam.contacts;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddContactActivity extends AppCompatActivity {

    private ImageView imageContact;
    private FloatingActionButton loadImage;
    private EditText nameContact,phoneContact,emailContact,birth;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private Uri imageUri = null;
    private Button addField;
    private LinearLayout phonesContainer;
    private int extraPhones = 0;
    private ArrayList<EditText> editTextPhones = new ArrayList<>();
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
        addField = findViewById(R.id.add_new_phone_field);
        phonesContainer = findViewById(R.id.container_phones);


        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraPhones ++;
                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.new_field_phone,null);
                EditText editText = addView.findViewById(R.id.new_field);
                Button rmButton = addView.findViewById(R.id.rm_new_phone_field);
                editText.setId(extraPhones);
                editText.setHint("Phone "+extraPhones);
                final EditText editTextPhone = addView.findViewById(extraPhones);
                rmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextPhones.remove(editTextPhone);
                        extraPhones--;
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                });
                editTextPhones.add(editTextPhone);
                phonesContainer.addView(addView);
            }
        });

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
            imageContact.setImageURI(uri);
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
                contacto.setImagen(imageUri);
                contacto.setName(nameContact.getText().toString());
                contacto.setNumbers(phoneContact.getText().toString());
                for(int i=0;i<extraPhones;i++){
                    contacto.setNumbers(editTextPhones.get(i).getText().toString());
                }
                contacto.setEmail(emailContact.getText().toString());
                contacto.setBirth(birth.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("NEW_CONTACT",contacto);
                intent.putExtra("IMAGE_URI",imageUri);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

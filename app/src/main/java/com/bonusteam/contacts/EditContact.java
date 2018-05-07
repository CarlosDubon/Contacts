package com.bonusteam.contacts;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class EditContact extends AppCompatActivity {
    private int PERMISSIONS_READ_EXTERNAL_STORAGE = 97;
    private Contacto editContact;
    private EditText nameContact,phonesContact,emailContact,birthContact;
    private FloatingActionButton loadImage;
    private Button addFieldPhone;
    private ImageView imageContact;
    private LinearLayout phonesFields;
    private int extraPhones=0;
    private ArrayList<EditText> editTextPhones = new ArrayList<>();
    private int REQUEST_CODE = 1;
    private Uri imageUri;
    private int index;
    private DatePickerDialog.OnDateSetListener datePickerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Bundle reciverBundle = getIntent().getExtras();
        if(reciverBundle!=null){
            editContact = reciverBundle.getParcelable("CONTACT_OBJ");
            index =reciverBundle.getInt("INDEX");
            if(editContact.getImagen()!=null) {
                imageUri = Uri.parse(editContact.getImagen());
            }
        }
        addFieldPhone = findViewById(R.id.add_new_phone_field);
        phonesFields = findViewById(R.id.container_phones);
        imageContact = findViewById(R.id.img_contact_edit);
        nameContact = findViewById(R.id.name_contact_edit);
        phonesContact = findViewById(R.id.phone_contact_edit);
        emailContact = findViewById(R.id.email_contact_edit);
        birthContact = findViewById(R.id.birth_contact_edit);
        loadImage = findViewById(R.id.btn_addImage_contact_edit);

        if(editContact.getImagen()!=null) {
            imageContact.setImageURI(Uri.parse(editContact.getImagen()));
        }
        nameContact.setText(editContact.getName());
        phonesContact.setText(editContact.getNumbers().get(0));
        emailContact.setText(editContact.getEmail());
        birthContact.setText(editContact.getBirth());
        editTextPhones.add(phonesContact);
        for(int i =1;i<editContact.getNumbers().size();i++){
            extraPhones++;
            LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.new_field_phone,null);
            EditText editText = addView.findViewById(R.id.new_field);
            Button rmButton = addView.findViewById(R.id.rm_new_phone_field);
            editText.setId(extraPhones);
            editText.setText(editContact.getNumbers().get(i));
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
            phonesFields.addView(addView);
        }
        addFieldPhone.setOnClickListener(new View.OnClickListener() {
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
                phonesFields.addView(addView);
            }
        });

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_READ_EXTERNAL_STORAGE);
                }
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        birthContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(EditContact.this, android.R.style.Theme_Holo_Light_Dialog, datePickerListener, year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String birthString = year +"-"+month+"-"+dayOfMonth;
                birthContact.setText(birthString);
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
        switch (item.getItemId()){
            case R.id.add_contact_tb:
                Contacto modifyContact = new Contacto();
                modifyContact.setImagen(imageUri+"");
                modifyContact.setName(nameContact.getText().toString());
                for (int i=0;i<editTextPhones.size();i++){
                    if(!editTextPhones.get(i).getText().toString().equals("")) {
                        modifyContact.setNumbers(editTextPhones.get(i).getText().toString());
                    }
                }
                modifyContact.setEmail(emailContact.getText().toString());
                modifyContact.setBirth(birthContact.getText().toString());
                Bundle contactModify = new Bundle();
                Intent intent = new Intent(this,MainActivity.class);
                contactModify.putParcelable("MODIFY_CONTACT",modifyContact);
                contactModify.putInt("INDEX_OC",index);
                intent.putExtras(contactModify);
                startActivity(intent);
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}

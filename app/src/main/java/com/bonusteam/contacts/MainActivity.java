package com.bonusteam.contacts;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<Contacto> contactList = new ArrayList<>();
    private ArrayList<Contacto> contactFavList = new ArrayList<>();
    private ArrayList<Contacto> contactRecentList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private ContactAdapter contactFavAdapter;
    private ContactAdapter contactRecentAdapter;
    private FloatingActionButton addContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        addContact = findViewById(R.id.btn_add_contact);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        setAdapters();
        addContacts();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Contacto newContact = bundle.getParcelable("NEW_CONTACT");
            contactList.add(newContact);
            contactAdapter.notifyItemInserted(contactList.size());
        }

        viewPagerAdapter.addFragment(ContactRecentFragment.newIntance(contactRecentAdapter),"");
        viewPagerAdapter.addFragment(ContactFragment.newIntance(contactAdapter),"");
        viewPagerAdapter.addFragment(ContactFavFragment.newInstance(contactFavAdapter),"");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_access_time_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_people_black_24dp).select();
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite_black_24dp);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
                getApplicationContext().startActivity(intent)
                ;
            }
        });
    }
    public void setAdapters(){
        contactAdapter = new ContactAdapter(contactList,this) {
            @Override
            public void addFavorite(int index) {
                contactList.get(index).setFavorite(true);
                contactFavList.add(contactList.get(index));
                notifyDataSetChanged();
                notifyItemInserted(index);
                int i = contactFavList.indexOf(contactList.get(index));
                contactFavAdapter.notifyItemInserted(i);
            }

            @Override
            public void removeFavorite(int index) {
                contactList.get(index).setFavorite(false);
                contactFavList.remove(contactList.get(index));
                notifyDataSetChanged();
                int i = contactFavList.indexOf(contactList.get(index));
                contactFavAdapter.notifyItemRemoved(index);
                contactFavAdapter.notifyItemRangeRemoved(i,contactFavList.size());
            }

            @Override
            public void addRecents(int index) {
                contactRecentList.add(contactList.get(index));
                int i = contactRecentList.indexOf(contactList.get(index));
                contactRecentAdapter.notifyItemInserted(index);
                contactRecentAdapter.notifyItemRangeRemoved(i,contactRecentList.size());
            }
        };
        contactFavAdapter = new ContactAdapter(contactFavList,this) {
            @Override
            public void addFavorite(int index) {}

            @Override
            public void removeFavorite(int index) {
                int i = contactList.indexOf(contactFavList.get(index));
                Log.d("INDICE",i+" "+contactList.get(i));
                contactList.get(i).setFavorite(false);
                contactFavList.remove(index);
                notifyItemRemoved(index);
                notifyItemRangeRemoved(index,contactFavList.size());
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void addRecents(int index) {
                contactRecentList.add(contactFavList.get(index));
                int i = contactRecentList.indexOf(contactFavList.get(index));
                contactRecentAdapter.notifyItemInserted(index);
                contactRecentAdapter.notifyItemRangeRemoved(i,contactRecentList.size());
            }
        };
        contactRecentAdapter= new ContactAdapter(contactRecentList,this) {
            @Override
            public void addFavorite(int index) {

            }

            @Override
            public void removeFavorite(int index) {

            }

            @Override
            public void addRecents(int index) {

            }
        };
    }

    public void addContacts(){
        Contacto contacto;
        String phoneNumber = null;
        String email = null;
        String image_uri;
        String birth = null;
        Bitmap bitmap=null;



        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        String ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri phoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.DATA;

        Uri emailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String emailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;


        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI,null,null,null,null);

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                contacto = new Contacto();
                String contactID = cursor.getString(cursor.getColumnIndex(ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                image_uri = cursor
                        .getString(cursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (image_uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media .getBitmap(this.getContentResolver(), Uri.parse(image_uri));
                        Log.d("DATO",bitmap+"");
                        contacto.setImagen(bitmap);
                    }catch (FileNotFoundException e) {
                        e.printStackTrace(); }
                    catch (IOException e) {
                    e.printStackTrace(); }
                }


                if(hasPhoneNumber >0){
                    contacto.setName(name);
                    Log.d("DATO",name);
                    Cursor phoneCursor = contentResolver.query(phoneCONTENT_URI,null,Phone_CONTACT_ID+" =? ", new String[]{contactID},null);

                    while (phoneCursor.moveToNext()){
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contacto.setNumber(phoneNumber);
                        Log.d("DATO:",phoneNumber);
                    }
                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(emailCONTENT_URI,null,emailCONTACT_ID+" =? ",new String[]{contactID},null);

                    while (emailCursor.moveToNext()){
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        contacto.setEmail(email);
                    }
                    emailCursor.close();

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String named = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Cursor bdc = contentResolver.query(android.provider.ContactsContract.Data.CONTENT_URI, new String[] { ContactsContract.CommonDataKinds.Event.DATA }, android.provider.ContactsContract.Data.CONTACT_ID+" = "+id+" AND "+ ContactsContract.Data.MIMETYPE+" = '"+ ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE+"' AND "+ ContactsContract.CommonDataKinds.Event.TYPE+" = "+ ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY, null, android.provider.ContactsContract.Data.DISPLAY_NAME);
                    if (bdc.getCount() > 0) {
                        while (bdc.moveToNext()) {
                            birth = bdc.getString(0);
                            // now "id" is the user's unique ID, "name" is his full name and "birthday" is the date and time of his birth
                           contacto.setBirth(birth);
                        }
                    }
                    bdc.close();
                    contactList.add(contacto);

                }
            }
        }
    }
}

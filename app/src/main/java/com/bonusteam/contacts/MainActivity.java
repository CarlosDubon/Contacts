package com.bonusteam.contacts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements Serializable,ManagerAdministrator {

    private int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    private  TabLayout tabLayout;
    private  ViewPager viewPager;
    private  ViewPagerAdapter viewPagerAdapter;
    private static ArrayList<Contacto> contactList = new ArrayList<>();
    private static ArrayList<Contacto> contactFavList = new ArrayList<>();
    private static ArrayList<Contacto> contactRecentList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private ContactAdapter contactFavAdapter;
    private ContactAdapter contactRecentAdapter;
    private FloatingActionButton addContact;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            contactList = savedInstanceState.getParcelableArrayList("CONTACT_LIST");
            contactFavList = savedInstanceState.getParcelableArrayList("CONTACT__FAV_LIST");
            contactRecentList = savedInstanceState.getParcelableArrayList("CONTACT_RECENT_LIST");
        }
        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        addContact = findViewById(R.id.btn_add_contact);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        setAdapters();

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
                startActivityForResult(intent,4);
            }
        });

        if(getIntent().getExtras()!=null){
            Contacto modifyContact = (Contacto) getIntent().getExtras().getParcelable("MODIFY_CONTACT");
            int pos = getIntent().getExtras().getInt("INDEX_OC");
            if(modifyContact != null){
                contactList.set(pos,modifyContact);
                contactRecentAdapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(contactList.size()==0) {
            requestContacts();
            clearPhonesContacts();
        }
        sortContact();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4){
            Contacto newContacto;
            if(resultCode == RESULT_OK){
                newContacto = data.getParcelableExtra("NEW_CONTACT");
                contactList.add(newContacto);
                contactAdapter.notifyItemInserted(contactList.size());
                contactAdapter.notifyDataSetChanged();
                sortContact();
                clearPhonesContacts();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        final MenuItem item = menu.findItem(R.id.search);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                item.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<Contacto> filtr = filter(contactList,newText);
                contactAdapter.setFilter(filtr);
                return true;
            }
        });
        return true;
    }

    private ArrayList<Contacto> filter(ArrayList<Contacto> contactList, String query){
        query = query.toLowerCase();
        final ArrayList<Contacto> filterContactList = new ArrayList<>();
        for(Contacto model:contactList){
            final String text = model.getName().toLowerCase();
            if(text.startsWith(query) || text.contains(query)){
                filterContactList.add(model);
            }
        }
        return  filterContactList;
    }

    public void setAdapters(){
        contactAdapter = new ContactAdapter(contactList,this) {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }

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
                contactRecentList.add(0,contactList.get(index));
                int i = contactRecentList.indexOf(contactList.get(index));
                contactRecentAdapter.notifyItemInserted(index);
                contactRecentAdapter.notifyItemRangeRemoved(i,contactRecentList.size());
            }
        };
        contactFavAdapter = new ContactAdapter(contactFavList,this) {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }

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
                contactRecentList.add(0,contactFavList.get(index));
                int i = contactRecentList.indexOf(contactFavList.get(index));
                contactRecentAdapter.notifyItemInserted(index);
                contactRecentAdapter.notifyItemRangeRemoved(i,contactRecentList.size());
            }
        };
        contactRecentAdapter= new ContactAdapter(contactRecentList,this) {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }

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

                image_uri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (image_uri != null) {
                    contacto.setImagen(image_uri);
                }


                if(hasPhoneNumber >0){
                    contacto.setName(name);
                    Cursor phoneCursor = contentResolver.query(phoneCONTENT_URI,null,Phone_CONTACT_ID+" =? ", new String[]{contactID},null);

                    while (phoneCursor.moveToNext()){
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phoneNumber = phoneNumber.replace(" ","");
                        contacto.setNumbers(phoneNumber);
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

    public void clearPhonesContacts(){
        for(int i=0 ;i< contactList.size();i++){
            HashSet hs = new HashSet();
            hs.addAll(contactList.get(i).getNumbers());
            contactList.get(i).getNumbers().clear();
            contactList.get(i).getNumbers().addAll(hs);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("CONTACT_LIST",contactList);
        outState.putParcelableArrayList("CONTACT__FAV_LIST",contactFavList);
        outState.putParcelableArrayList("CONTACT_RECENT_LIST",contactRecentList);
        super.onSaveInstanceState(outState);
    }


    private void sortContact(){
        Collections.sort(contactList);
        contactAdapter.notifyDataSetChanged();
    }

    private void requestContacts(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
        }else{
            addContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_READ_CONTACTS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requestContacts();
            }else{
                Snackbar.make(viewPager,"Permissions denail, contacts cant be show",Snackbar.LENGTH_SHORT);
            }
        }

    }

    @Override
    public void deleteContact(final Contacto contacto, final int index) {
        contactList.remove(index);
        contactAdapter.notifyItemRemoved(index);
        contactAdapter.notifyItemRangeChanged(index,contactList.size());
        Snackbar.make(viewPager,contacto.getName()+" "+ getResources().getString(R.string.deleted_ok_snack),Snackbar.LENGTH_SHORT).show();

    }
}

package com.bonusteam.contacts;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<Contacto> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private RecyclerView recyclerViewContacts;

    //TODO: Importar contactos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        contactAdapter = new ContactAdapter(contactList);
        addContacts();


        viewPagerAdapter.addFragment(ContactFragment.newIntance(contactAdapter),"Contactos");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }

    public void addContacts(){
        contactList.add(new Contacto(R.drawable.ic_person_black_24dp,"Carlos","Minero","7507-9736","carlosminerodubon@gmail.com","Zaragoza","10/01/1998"));
    }
}

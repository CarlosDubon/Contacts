package com.bonusteam.contacts;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;

    public ContactFragment() {
    }

    public static ContactFragment newIntance(ContactAdapter contactAdapter){
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.setContactAdapter(contactAdapter);
        return contactFragment;
    }

    public void setContactAdapter(ContactAdapter contactAdapter){
        this.contactAdapter = contactAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts,container,false);
        //Recycler view para contactos
        recyclerView = view.findViewById(R.id.recyclerview_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(contactAdapter);
        //------------------------------------------
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

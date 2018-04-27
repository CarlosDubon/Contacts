package com.bonusteam.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactRecentFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    ContactAdapter contactAdapter;

    public ContactRecentFragment() {
    }

    public static ContactRecentFragment newIntance(ContactAdapter contactAdapter){
        ContactRecentFragment contactRecentFragment = new ContactRecentFragment();
        contactRecentFragment.setContactAdapter(contactAdapter);
        return contactRecentFragment;
    }

    public void setContactAdapter(ContactAdapter contactAdapter){
        this.contactAdapter = contactAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts_recents,container,false);
        recyclerView = view.findViewById(R.id.recyclerview_contacts_recent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contactAdapter);
        return view;
    }
}
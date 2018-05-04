package com.bonusteam.contacts;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactFavFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;

    public ContactFavFragment() {
    }

    public static ContactFavFragment newInstance(ContactAdapter contactAdapter){
        ContactFavFragment contactFavFragment = new ContactFavFragment();
        contactFavFragment.setContactAdapter(contactAdapter);
        return  contactFavFragment;
    }
    public void setContactAdapter(ContactAdapter contactAdapter){
        this.contactAdapter = contactAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            contactAdapter = (ContactAdapter) savedInstanceState.getParcelable("CONTACT_FAV_ADAPTER");
        }
        view = inflater.inflate(R.layout.fragment_contacts_fav,container,false);
        recyclerView = view.findViewById(R.id.recyclerview_contacts_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(contactAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("CONTACT_FAV_ADAPTER",contactAdapter);
        super.onSaveInstanceState(outState);
    }
}

package com.bonusteam.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolderContactAdapter> {

    ArrayList<Contacto> contactosList = new ArrayList<>();

    public ContactAdapter(ArrayList<Contacto> contactosList){
        this.contactosList = contactosList;
    }

    public static class ViewHolderContactAdapter extends RecyclerView.ViewHolder{

        public ViewHolderContactAdapter(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolderContactAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_contacts,parent,false);
        return new ViewHolderContactAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContactAdapter holder, int position) {

    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }
}

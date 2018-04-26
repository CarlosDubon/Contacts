package com.bonusteam.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolderContactAdapter> {

    @NonNull
    @Override
    public ViewHolderContactAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContactAdapter holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolderContactAdapter extends RecyclerView.ViewHolder{

        public ViewHolderContactAdapter(View itemView) {
            super(itemView);
        }
    }
}

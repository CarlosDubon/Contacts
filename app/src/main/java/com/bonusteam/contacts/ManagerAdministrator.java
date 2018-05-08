package com.bonusteam.contacts;

import android.content.Context;

public interface ManagerAdministrator {
    void deleteContact(Context context, Contacto contacto, int index);
    void addContactFavorite(Context context, Contacto contacto, int index);
}

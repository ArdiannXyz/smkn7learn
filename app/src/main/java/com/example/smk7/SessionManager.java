package com.example.smk7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    private static final String PREF_NAME = "UserData";
    private static final String KEY_ID_GURU = "id_guru";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String TAG = "SessionManager";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Reset semua data session
    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public void saveUserLoginData(String userId, String role, String nama, String email) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        // Jika role adalah guru, simpan userId sebagai id_guru
        if (role.equalsIgnoreCase("guru")) {
            try {
                int idGuru = Integer.parseInt(userId);
                editor.putInt(KEY_ID_GURU, idGuru);
                Log.d(TAG, "Saved ID Guru: " + idGuru);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing ID Guru: " + e.getMessage());
            }
        }

        editor.apply();
    }

    public int getIdGuru() {
        int idGuru = pref.getInt(KEY_ID_GURU, -1);
        Log.d(TAG, "Retrieved ID Guru: " + idGuru);
        return idGuru;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, "");
    }

    public void logout() {
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
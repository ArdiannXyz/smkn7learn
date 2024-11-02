package com.example.smk7.Admin;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smk7.LoginActivity;
import com.example.smk7.R;
import com.example.smk7.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;


public class UploadDataguruFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, nip;
    private Button signupButton;
    private TextView loginText;
    private FirebaseFirestore db;
    private ImageView backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_dataguru, container, false);

        backButton = view.findViewById(R.id.back_Button);
        signupEmail = view.findViewById(R.id.email_register);
        signupPassword = view.findViewById(R.id.pw_register);
        signupButton = view.findViewById(R.id.btn_register);
        nip = view.findViewById(R.id.Nipguru);

        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).viewPager2.setCurrentItem(4);
            }
        });

        signupButton.setOnClickListener(v -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String nipValue = nip.getText().toString().trim();

            if (!validateInputs(email, password, nipValue)) {
                return;
            }
            registerGuru(email, password, nipValue);
        });

        return view; // Menambahkan return view
    }

    private boolean validateInputs(String email, String password, String nipValue) {
        if (email.isEmpty()) {
            signupEmail.setError("Email tidak boleh kosong");
            signupEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Format email tidak valid");
            signupEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            signupPassword.setError("Password tidak boleh kosong");
            signupPassword.requestFocus();
            return false;
        }


        if (nipValue.isEmpty()) {
            nip.setError("NIP tidak boleh kosong");
            nip.requestFocus();
            return false;
        }

        return true;
    }

    private void registerGuru(String email, String password, String nipValue) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            // Data untuk collection users
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("uid", uid);
                            userData.put("email", email);
                            userData.put("role", "guru");
                            userData.put("createdAt", FieldValue.serverTimestamp());

                            // Data untuk collection teachers
                            Map<String, Object> teacherData = new HashMap<>();
                            teacherData.put("uid", uid);
                            teacherData.put("nip", nipValue);
                            teacherData.put("email", email);

                            // Menggunakan batch write
                            WriteBatch batch = db.batch();
                            batch.set(db.collection("users").document(uid), userData);
                            batch.set(db.collection("teachers").document(uid), teacherData);

                            batch.commit()
                                    .addOnSuccessListener(aVoid -> {
                                        if (getContext() != null) {
                                            Toast.makeText(getContext(),
                                                    "Guru berhasil didaftarkan",
                                                    Toast.LENGTH_SHORT).show();
                                            // Optional: Clear input fields
                                            clearInputs();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        if (getContext() != null) {
                                            Toast.makeText(getContext(),
                                                    "Error menyimpan data: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                            Log.e("Dataguru", "Error saving to Firestore", e);
                                        }
                                    });
                        }
                    } else {
                        if (getContext() != null) {
                            Toast.makeText(getContext(),
                                    "Registrasi gagal: " +
                                            (task.getException() != null ?
                                                    task.getException().getMessage() :
                                                    "Unknown error"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearInputs() {
        signupEmail.setText("");
        signupPassword.setText("");
        nip.setText("");
    }
}
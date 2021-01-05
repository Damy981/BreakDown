package com.example.android.arkanoid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etConfirmPassword;
    private EditText etMail;
    private EditText etPassword;
    private EditText etName;
    private FirebaseAuth mAuth;
    private Services services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        extractStrings();
        services = new Services(getSharedPreferences("com.example.android.arkanoid_preferences", MODE_PRIVATE));
    }

    private void extractStrings() {
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPass);
        etConfirmPassword = findViewById(R.id.etConfPass);
        etName = findViewById(R.id.etName);
    }

    public void register(View view) {
        String name = etName.getText().toString();
        String email = etMail.getText().toString();
        String password = etPassword.getText().toString();

        //validate input
        if(!validateMail(email)){
            showDialogBox("Email address not valid", "Error", android.R.drawable.ic_dialog_alert);
        }
        else if(!validatePassword(password)){
            showDialogBox("Password not valid, password must have at least 6 characters", "Error", android.R.drawable.ic_dialog_alert);
        }else {
            if (mAuth.getCurrentUser() == null)
                createFirebaseUser(email, password, name);
        }
    }

    private void createFirebaseUser(final String email, final String password, final String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegistrationActivity.this, "Registration success, please login.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String username = getSharedPreferences("com.example.android.arkanoid_preferences", MODE_PRIVATE).getString("userName", null);
                            if (!username.equals("GuestUser")) {
                                services.setSharedPreferences(name, 0, 1, user.getUid());
                            }
                            else {
                                services.setNameAndUserId(name, user.getUid());
                                services.updateDatabase();
                            }
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            showDialogBox("Registration failed, check your connection and try again", "Error", android.R.drawable.ic_dialog_alert);
                        }
                    }
                });
    }


    private boolean validateMail (String mail) {
        return mail.contains("@");
    }


    private boolean validatePassword (String password) {
        String confirmPasswordString = etConfirmPassword.getText().toString();
        return confirmPasswordString.equals(password) && password.length() >= 6;
    }


    private void showDialogBox(String message, String title, int icon) {

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(icon)
                .show();
    }


    public void btnBackClick(View view) {
        onBackPressed();
    }
}
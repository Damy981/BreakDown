package com.example.android.arkanoid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etConfirmPassword;
    private EditText etMail;
    private EditText etPassword;
    private EditText etName;
    private FirebaseAuth mAuth;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        extractStrings();
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
            else {
                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                createFirebaseUserFromGuest(credential, name);
            }
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
                            setName(name, user);
                            initializeProfile(user, name);
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            showDialogBox("Registration failed, please try again", "Error", android.R.drawable.ic_dialog_alert);
                        }
                    }
                });
    }

    private void setName(String name, FirebaseUser user) {

        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("setNome", "Name updated succesfully");
                }else{
                    Log.i("setNome", "Error");
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

    private void createFirebaseUserFromGuest ( AuthCredential credential, final String name) {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Registration success, please login.",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            setName(name, user);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String userId = user.getUid();
                            DatabaseReference myRef = database.getReference("Profiles");
                            myRef.child(userId).child("UserName").setValue(name);

                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            showDialogBox("Registration failed, please try again", "Error", android.R.drawable.ic_dialog_alert);
                        }
                    }
                });
    }

    private void showDialogBox(String message, String title, int icon) {

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(icon)
                .show();
    }

    static protected void initializeProfile(FirebaseUser user, String name) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = user.getUid();
        DatabaseReference myRef = database.getReference("Profiles");

        myRef.child(userId).child("LevelNumber").setValue(1);
        myRef.child(userId).child("Coins").setValue(0);
        if (user.isAnonymous())
            myRef.child(userId).child("UserName").setValue("GuestUser");
        else
            myRef.child(userId).child("UserName").setValue(name);
        //aggiungere i power up
    }
}
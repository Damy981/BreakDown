package com.example.android.arkanoid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity {

    EditText etConfirmPassword;
    EditText etMail;
    EditText etPassword;
    EditText etName;
    private FirebaseAuth mAuth;
    Context context = this;

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
            Toast.makeText(getApplicationContext(),"Email not valid", Toast.LENGTH_SHORT).show();
        }
        else if(!validatePassword(password)){
            Toast.makeText(getApplicationContext(),"Email address or password not valid, password must have at least 6 characters", Toast.LENGTH_SHORT).show();
        }else {
            createFirebaseUser(email, password, name);
        }
    }

    private void createFirebaseUser(final String email, final String password, final String nome){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegistrationActivity.this, "Registration success.",
                                    Toast.LENGTH_SHORT).show();
                            setName(nome);
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistrationActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setName(String name) {
        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        if (user != null) {
            user.updateProfile(changeRequest);
        }
    }

    private boolean validateMail (String mail) {
        return mail.contains("@");
    }


    private boolean validatePassword (String password) {
        String confirmPasswordString = etConfirmPassword.getText().toString();
        return confirmPasswordString.equals(password) && password.length() >= 6;
    }

}
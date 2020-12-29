package com.example.android.arkanoid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etMail;
    private EditText etPassword;
    private TextView notRegistered;
    private TextView forgotPassword;
    private Button login;
    private Button sendMail;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        extractUI();
    }

    private void extractUI() {
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPass);
        notRegistered = findViewById(R.id.tvNotRegistered);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        login = findViewById(R.id.btnLogin);
        sendMail = findViewById(R.id.btnForgotPass);
    }

    public void login(View view) {
        String email = etMail.getText().toString();
        String password = etPassword.getText().toString();
        if (email.contains("@") && password.length() >= 6)
            loginFirebaseUser(email, password);
        else
            Toast.makeText(LoginActivity.this, "Please insert mail and password.",
                    Toast.LENGTH_SHORT).show();
    }

    private void loginFirebaseUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user.isEmailVerified())
                                updateUI(user);
                            else {
                                Toast.makeText(LoginActivity.this, "Your email address is not verified, email has been sent. Verify and try again.",
                                        Toast.LENGTH_LONG).show();
                                user.sendEmailVerification();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void openRegisterActivity(View view) {

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void forgotPassword(View view) {

        notRegistered.setVisibility(View.GONE);
        forgotPassword.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        sendMail.setVisibility(View.VISIBLE);
    }

    public void sendResetPasswordMail(View view) {
        String email = etMail.getText().toString();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Mail sent.", Toast.LENGTH_LONG).show();
                            changeVisibility();
                        } else {
                            Toast.makeText(context, "This mail is not registered.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        changeVisibility();
    }

    private void changeVisibility() {
        notRegistered.setVisibility(View.VISIBLE);
        forgotPassword.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        etPassword.setVisibility(View.VISIBLE);
        sendMail.setVisibility(View.GONE);
    }

    public void guestLogin(View view) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
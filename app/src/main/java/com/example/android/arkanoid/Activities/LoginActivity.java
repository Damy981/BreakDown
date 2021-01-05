package com.example.android.arkanoid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etMail;
    private EditText etPassword;
    private TextView notRegistered;
    private TextView forgotPassword;
    private Button login;
    private Button sendMail;
    private Services services;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        services = new Services(getSharedPreferences("com.example.android.arkanoid_preferences", MODE_PRIVATE));
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
            showDialogBox("Please insert email address and password", "Error", android.R.drawable.ic_dialog_alert);
    }



    private void loginFirebaseUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()) {
                                retrieveProfileDataOnline();
                                loadingScreen();
                            }
                            else {
                                showDialogBox("Your email address is not verified, email has been sent. Verify and try again.", "Info", android.R.drawable.ic_dialog_info);
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

    private void updateUI() {
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
                            Toast.makeText(MainActivity.context, "Mail sent.", Toast.LENGTH_LONG).show();
                            changeVisibility();
                        } else {
                            showDialogBox("This email address is not registered", "Error", android.R.drawable.ic_dialog_alert);
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

        services.setSharedPreferences("GuestUser",0,1, null);
        showGuestAlert();
    }

    private void showDialogBox(String message, String title, int icon) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(icon)
                .show();
    }

    private void showGuestAlert () {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Logged as a guest, if you don't complete you registration from your profile you will lose all your progress if you uninstall this app")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateUI();
                    }
                });
        alert.show();
    }

    private void retrieveProfileDataOnline() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String userId = user.getUid();
        DatabaseReference myRef = database.getReference("Profiles").child(userId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int levelNumber = dataSnapshot.child("LevelNumber").getValue(int.class);
                int coins = dataSnapshot.child("Coins").getValue(int.class);
                String userName = dataSnapshot.child("UserName").getValue(String.class);
                services.setSharedPreferences(userName, coins, levelNumber, userId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadingScreen() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                updateUI();
            }
        }, 1200);
    }
}
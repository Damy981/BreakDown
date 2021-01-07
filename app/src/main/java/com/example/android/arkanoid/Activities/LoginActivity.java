package com.example.android.arkanoid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

/*Activity that manages the login of a registered user or a guest,
  the guest user can access and play offline
  but has limited functionality until he completes the registration.
*/
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etMail;
    private EditText etPassword;
    private TextView notRegistered;
    private TextView forgotPassword;
    private Button login;
    private Button sendMail;
    private Button guestButton;
    private Services services;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        services = new Services(getSharedPreferences(Services.SHARED_PREF_DIR, MODE_PRIVATE));
        extractUI();
    }
    private void extractUI() {
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPass);
        notRegistered = findViewById(R.id.tvNotRegistered);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        login = findViewById(R.id.btnLogin);
        sendMail = findViewById(R.id.btnForgotPass);
        guestButton = findViewById(R.id.guestButton);
    }


    public void login(View view) {
        //Get mail and password from the relative editText and check if they are valid
        String email = etMail.getText().toString();
        String password = etPassword.getText().toString();
        if (email.contains("@") && password.length() >= 6)
            loginFirebaseUser(email, password);
        else
            showDialogBox("Please insert email address and password", "Error", android.R.drawable.ic_dialog_alert);
    }


    //Use firebase to login with the entered data

    private void loginFirebaseUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, get current user
                            user = mAuth.getCurrentUser();
                            // if the logged user confirmed the email retrieve the profile from the database and handle that in a loading screen
                            if (user.isEmailVerified()) {
                                findViewById(R.id.btnLogin).setVisibility(View.INVISIBLE);
                                retrieveProfileDataOnline();
                                loadingScreen();
                            }
                            //if the logged user did not verify the email, show an error
                            else {
                                showDialogBox("Your email address is not verified. Verify and try again.", "Info", android.R.drawable.ic_dialog_info);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //opens menu activity
    private void updateUI() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    //updates the layout when user press "forgot password"
    public void forgotPassword(View view) {

        notRegistered.setVisibility(View.GONE);
        forgotPassword.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        guestButton.setVisibility(View.GONE);
        sendMail.setVisibility(View.VISIBLE);
    }
    //send email for password reset and show result
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
    //revert the layout to the original state
    private void changeVisibility() {
        notRegistered.setVisibility(View.VISIBLE);
        forgotPassword.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        etPassword.setVisibility(View.VISIBLE);
        guestButton.setVisibility(View.VISIBLE);
        sendMail.setVisibility(View.GONE);
    }

    //create and initialize a local profile for the guest user and show a dialogbox with relative informations
    public void guestLogin(View view) {
        services.setSharedPreferences("GuestUser",0,1, null, "5,5,5,5,5", "10,0,0,0,0");
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
    //show informations for guest user and when ok is clicked move to menu
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

    //connect to database and update local profile with data in the database using the userid as a key
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
                String prices = dataSnapshot.child("Prices").getValue(String.class);
                String quantities = dataSnapshot.child("Quantities").getValue(String.class);
                services.setSharedPreferences(userName, coins, levelNumber, userId, prices, quantities);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //show a loding screen while the data is being downloaded from database, and then move to menu
    private void loadingScreen() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                updateUI();
            }
        }, 1200);
    }
}
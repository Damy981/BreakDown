package com.example.android.arkanoid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.Fragments.GameModeMenuFragment;
import com.example.android.arkanoid.Fragments.ProfileFragment;
import com.example.android.arkanoid.Fragments.QuestFragment;
import com.example.android.arkanoid.R;
import com.example.android.arkanoid.Fragments.RankingFragment;
import com.example.android.arkanoid.Fragments.SettingsFragment;
import com.example.android.arkanoid.Fragments.ShopFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*Activity that manages the menu and show the various fragment. Builds the profile
  object from the local data and send that to the fragments. If internet is available
  updates the database.
*/

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Profile profile;
    private FragmentManager fm;
    private FragmentTransaction tx;
    private Fragment fragment;
    private ConstraintLayout menu;
    private Bundle bundle;
    private SharedPreferences preferences;
    private Services services;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        preferences = getSharedPreferences(Services.SHARED_PREF_DIR, MODE_PRIVATE);
        services = new Services(preferences);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //if user is a guest, hide the logout button
        if (user == null)
            findViewById(R.id.btnLogout).setVisibility(View.GONE);
        menu = findViewById(R.id.menu);
        fm = getSupportFragmentManager();
        //Builds the profile object from the local data
        retrieveProfileDataOffline();
        bundle = new Bundle();

    }

    //show the menu again if it was hidden by the fragment
    @Override
    public void onBackPressed() {
        if (menu.getVisibility() == View.VISIBLE)
            moveTaskToBack(true);
        else
            changeVisibility();
    }

    //if internet is available update the database and then sign out the user erasing all local data
    public void logout(View view) {
        if (isNetworkAvailable()) {
            services.updateDatabase();
            mAuth.signOut();
            user = null;
            preferences.edit().clear().commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else
            Toast.makeText(MainActivity.context, "Internet connection not available, check your connection and try again", Toast.LENGTH_LONG).show();
    }

    public void btnProfileClick(View view) {
        fragment = new ProfileFragment();
        initializeFragment(fragment);

    }

    public void btnShopClick(View view) {
        fragment = new ShopFragment();
        initializeFragment(fragment);
    }

    public void btnQuestClick(View view) {
        fragment = new QuestFragment();
        initializeFragment(fragment);
    }

    public void btnLevelEditorClick(View view) {
        Intent intentLevelEditor = new Intent(this, LevelEditorActivity.class);
        startActivity(intentLevelEditor);
    }

    public void btnRankingClick(View view) {
        fragment = new RankingFragment();
        initializeFragment(fragment);
    }

    public void btnSettingsClick(View view) {
        fragment = new SettingsFragment();
        initializeFragment(fragment);
    }

    public void startGameMenu(View view) {
        fragment = new GameModeMenuFragment();
        initializeFragment(fragment);
    }

    /* build the profile object using the local data and set
       a listener for rebuild it whenever there is a change.
       If internet is available also update the database   */
    private void retrieveProfileDataOffline() {
        if (isNetworkAvailable() && user != null) {
            services.updateDatabase();
        }
        profile = services.buildProfile();
        prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {

                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        profile = services.buildProfile();
                        if (isNetworkAvailable() && user != null)
                            services.updateDatabase();
                    }
                };
        if (preferences != null) {
            preferences.registerOnSharedPreferenceChangeListener(prefListener);
        }
    }

    /*if user was in a fragment destroy it and show menu again, if user is opening
      a fragment hide menu  */
    private void changeVisibility() {
        if (menu.getVisibility() == View.VISIBLE)
            menu.setVisibility(View.GONE);
        else {
            tx = fm.beginTransaction();
            tx.remove(fragment).commit();
            menu.setVisibility(View.VISIBLE);
        }
    }

    //return true if internet connection is available else return false
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    //send profile object to the fragment and hide the menu for create and show fragment layout
    private void initializeFragment(Fragment fragment) {
        bundle.putSerializable("profile", profile);
        fragment.setArguments(bundle);
        changeVisibility();
        tx = fm.beginTransaction();
        tx.add(R.id.fragment_place, fragment);
        tx.commit();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("cacca", "Portrait");
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("cacca", "Landscape");
        }
    }
}
package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

public class LevelEditorActivity extends AppCompatActivity {
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_editor);
        profile = (Profile) getIntent().getSerializableExtra("profile");

        ImageView btnBackLevelEditor = findViewById(R.id.ivBackLevelEditor);
        btnBackLevelEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
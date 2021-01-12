package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.arkanoid.R;

public class LevelEditorActivity extends AppCompatActivity {
    ImageView btnBackLeveEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_editor);
        btnBackLeveEditor = findViewById(R.id.ivBackLevelEditor);
        btnBackLeveEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.android.arkanoid.Classes.Adapters.BrickItemAdapter;
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

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        RecyclerView rvBricks = findViewById(R.id.rvBricks);
        rvBricks.setLayoutManager(layoutManager);

        int[] bricks = new int[]{R.drawable.brick_pink, R.drawable.brick_blue, R.drawable.brick_green, R.drawable.brick_grey, R.drawable.brick_light_orange, R.drawable.brick_orange, R.drawable.brick_red, R.drawable.brick_sky_blue, R.drawable.brick_violet, R.drawable.brick_yellow, R.drawable.brick_black, R.drawable.brick_nitro, R.drawable.brick_switch_on};

        BrickItemAdapter adapter = new BrickItemAdapter(getApplicationContext(), bricks);
        rvBricks.setAdapter(adapter);

        btnBackLevelEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
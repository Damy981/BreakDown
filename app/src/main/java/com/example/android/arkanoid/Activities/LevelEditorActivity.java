package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.arkanoid.Classes.Adapters.BrickItemAdapter;
import com.example.android.arkanoid.Classes.Brick;
import com.example.android.arkanoid.Classes.Level;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Quest;
import com.example.android.arkanoid.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LevelEditorActivity extends AppCompatActivity {
    private Profile profile;
    static private GridLayout glEditor;
    static private final ArrayList<Brick> brickList = new ArrayList<>();
    private Level level;
    private EditText etLevelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_editor);
        profile = (Profile) getIntent().getSerializableExtra("profile");
        etLevelName = findViewById(R.id.etLevelName);

        glEditor = findViewById(R.id.glEditor);
        glEditor.setRowCount(6);
        glEditor.setColumnCount(7);

        ImageView btnBackLevelEditor = findViewById(R.id.ivBackLevelEditor);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        RecyclerView rvBricks = findViewById(R.id.rvBricks);
        rvBricks.setLayoutManager(layoutManager);

        int[] bricks = new int[]{R.drawable.brick_empty, R.drawable.brick_pink, R.drawable.brick_blue, R.drawable.brick_green, R.drawable.brick_grey, R.drawable.brick_light_orange, R.drawable.brick_orange, R.drawable.brick_red, R.drawable.brick_sky_blue, R.drawable.brick_violet, R.drawable.brick_yellow, R.drawable.brick_black, R.drawable.brick_nitro, R.drawable.brick_switch_on};

        BrickItemAdapter adapter = new BrickItemAdapter(getApplicationContext(), bricks);
        rvBricks.setAdapter(adapter);

        btnBackLevelEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

   static public void setBrick(int id, int i, int j) {
        ImageView brick = new ImageView(MainActivity.context);
        brick.setImageResource(id);

        LinearLayout ll = new LinearLayout(MainActivity.context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(155, 155);
        ll.setLayoutParams(params);
        ll.addView(brick);

        glEditor.addView(ll);
        addBrickToList(id, i, j);
    }

    static public void addBrickToList(int id, int i, int j) {
        int level = 10;

        if (!(id == R.drawable.brick_empty)) {
            Brick b = new Brick(MainActivity.context, j * Level.BRICK_HORIZONTAL_DISTANCE, i * Level.BRICK_VERTICAL_DISTANCE, level, true);
            b.setSkin(id);

            if (id == R.drawable.brick_black)
                b.setHard(true);
            if (id == R.drawable.brick_nitro)
                b.createNitro();
            if (id == R.drawable.brick_switch_on)
                b.createSwitch();

            brickList.add(b);
        }
    }

    public void saveAndShareLevel(View view) {
        if (etLevelName.getText() != null || !etLevelName.getText().toString().equals("")) {
            level = new Level(brickList, profile.getUserName(), etLevelName.getText().toString());
            createLevelFile(getApplicationContext());
        }
    }

    public void createLevelFile(Context context) {
        try {
            FileOutputStream questFile = new FileOutputStream(context.getFilesDir() + "/"+ profile.getUserId() + "_" + level.getLevelName());
            try {
                ObjectOutputStream a = new ObjectOutputStream(questFile);
                a.writeObject(level);
                a.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
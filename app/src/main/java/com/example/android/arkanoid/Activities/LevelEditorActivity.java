package com.example.android.arkanoid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.arkanoid.Classes.Adapters.BrickItemAdapter;
import com.example.android.arkanoid.Classes.BitmapDataObject;
import com.example.android.arkanoid.Classes.Level;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Quest;
import com.example.android.arkanoid.Classes.SerializableBrick;
import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LevelEditorActivity extends AppCompatActivity {
    private Profile profile;
    static private GridLayout glEditor;
    static private final ArrayList<SerializableBrick> serializableBrickList = new ArrayList<>();
    private EditText etLevelName;
    private String levelFileName;

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
        if (!(id == R.drawable.brick_empty)) {

            BitmapDataObject bitmap = new BitmapDataObject();
            bitmap.setCurrentImage(BitmapFactory.decodeResource(MainActivity.context.getResources(), id));

            SerializableBrick b = new SerializableBrick( j * Level.BRICK_HORIZONTAL_DISTANCE, i * Level.BRICK_VERTICAL_DISTANCE, bitmap);

            serializableBrickList.add(b);
        }
    }

    public void saveAndShareLevel(View view) {
        if (etLevelName.getText() != null && !etLevelName.getText().toString().equals("")) {
            createLevelFile(getApplicationContext());

            Services services = new Services(profile.getUserId());
            Quest createLevelQuest = profile.getQuestsList().get(Quest.QUEST_CREATE_LEVEL);
            profile.getQuestsList().get(Quest.QUEST_CREATE_LEVEL).setProgress(createLevelQuest.getProgress() + 1);
            services.updateQuestsFile(getApplicationContext(), profile.getQuestsList());

            if (isNetworkAvailable())
                uploadLevelFile();
            onBackPressed();
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Name empty")
                    .setMessage("Please insert a name for your level")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void createLevelFile(Context context) {

        File directory = new File(context.getFilesDir(), "CustomLevels");
        directory.mkdir();

        String levelName = etLevelName.getText().toString();
        levelFileName = profile.getUserId() + "_" + levelName + ".bin";
        try {
            FileOutputStream levelFile = new FileOutputStream(context.getFilesDir() + "/CustomLevels/"+ levelFileName);
            try {
                ObjectOutputStream a = new ObjectOutputStream(levelFile);
                a.writeObject(serializableBrickList);
                a.writeObject(levelName);
                a.writeObject(profile.getUserName());

                a.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void uploadLevelFile() {
        Uri file = Uri.fromFile(new File(getApplicationContext().getFilesDir()+ "/CustomLevels/" + levelFileName));
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("file/CustomLevels/" + levelFileName);
        mStorageRef.putFile(file);
    }

    //return true if internet connection is available else return false
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
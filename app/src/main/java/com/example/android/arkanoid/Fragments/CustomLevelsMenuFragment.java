package com.example.android.arkanoid.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.android.arkanoid.Classes.Adapters.LevelItemAdapter;
import com.example.android.arkanoid.Classes.Brick;
import com.example.android.arkanoid.Classes.Level;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.SerializableBrick;
import com.example.android.arkanoid.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomLevelsMenuFragment extends Fragment {

    private Profile profile;
    private List<StorageReference> items;
    private ArrayList<Level> levels;
    private ArrayList<ArrayList<SerializableBrick>> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_levels_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        levels = new ArrayList<>();
        list = new ArrayList<>();
        profile = (Profile) getArguments().getSerializable("profile");
        ImageView btnBack = getView().findViewById(R.id.ivBackCustomLevels);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if(isNetworkAvailable())
            downloadLevels(getContext());
        getLevelFromFilesAndSetAdapter();
    }

    //download all level files on local storage
    private void downloadLevels(final Context context) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("file/CustomLevels/");

        mStorageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        items = listResult.getItems();
                        for (StorageReference ref : items) {
                            String fileName = ref.toString();
                            fileName = fileName.substring((fileName.lastIndexOf("/") + 1));
                            File localLevelFile;
                            localLevelFile = new File(context.getFilesDir() + "/CustomLevels/", fileName);
                            ref.getFile(localLevelFile);
                        }
                    }
                });
    }

    //get all levels data from files and build them
    private void getLevelsFromFiles(Context context) throws IOException, ClassNotFoundException {
        File directory = new File(context.getFilesDir() + "/CustomLevels/");
        File[] files = directory.listFiles();
        String levelName;
        String username;

        for (File file : files) {
            FileInputStream levelFile = new FileInputStream(file);
            ObjectInputStream l = new ObjectInputStream(levelFile);
            ArrayList<SerializableBrick> serializableBrickList = (ArrayList<SerializableBrick>) l.readObject();
            list.add(serializableBrickList);
            levelName = (String) l.readObject();
            username = (String) l.readObject();
            buildLevel(serializableBrickList, levelName, username);
            l.close();
        }
    }

    //convert all serializableBricks to normal bricks and build level object to add in arraylist
    private void buildLevel(ArrayList<SerializableBrick> serializableBrickList, String levelName, String username) {

        ArrayList<Brick> brickList = new ArrayList<>();
        for(int i = 0; i < serializableBrickList.size(); i++) {
            SerializableBrick b = serializableBrickList.get(i);
            brickList.add(new Brick(getContext(), b.getX(), b.getY(), b.getBitmap(), b.isHardBrick(), b.isNitroBrick(), b.isSwitchBrick()));
        }
        Level level = new Level(brickList, username, levelName);
        levels.add(level);
    }

    private void getLevelFromFilesAndSetAdapter() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                try {
                    getLevelsFromFiles(Objects.requireNonNull(getContext()));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ListView lvCustomLevels = getView().findViewById(R.id.lvCustomLevels);
                LevelItemAdapter adapter = new LevelItemAdapter(getContext(), levels, list, profile);
                lvCustomLevels.setAdapter(adapter);

                ProgressBar progressBar = getView().findViewById(R.id.pbCustomLevels);
                progressBar.setVisibility(View.GONE);
            }
        }, 2500);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.arkanoid.Activities.LevelEditorActivity;
import com.example.android.arkanoid.Classes.Level;
import com.example.android.arkanoid.R;

public class BrickItemAdapter extends RecyclerView.Adapter<BrickItemAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final int[] bricks;
    private int counter;
    int i;
    int j;

    public BrickItemAdapter(Context context, int[] bricks) {

        inflater = (LayoutInflater.from(context));
        this.bricks = bricks;
        counter = 0;
        i = Level.ROW_START;
        j = Level.COLUMN_START;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_brick, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.brick.setImageResource(bricks[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter < 42) {
                    ++counter;
                    if(counter % 7 == 0) {
                        i++;
                        j = Level.COLUMN_START;
                    }
                    else
                        j++;
                    LevelEditorActivity.setBrick(bricks[position], i, j);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bricks.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView brick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            brick = itemView.findViewById(R.id.ivBrick);
        }
    }
}

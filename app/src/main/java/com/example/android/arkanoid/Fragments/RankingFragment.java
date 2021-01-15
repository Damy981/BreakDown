package com.example.android.arkanoid.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.arkanoid.Classes.Adapters.RankItemAdapter;
import com.example.android.arkanoid.R;

public class RankingFragment extends Fragment {
    ImageView btnBackRanking;
    ListView lvRankItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBackRanking = getActivity().findViewById(R.id.ivBackRanking);
        btnBackRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        lvRankItem = getActivity().findViewById(R.id.lvRankItem);

        RankItemAdapter adapter = new RankItemAdapter(getContext());
        lvRankItem.setAdapter(adapter);
    }

}
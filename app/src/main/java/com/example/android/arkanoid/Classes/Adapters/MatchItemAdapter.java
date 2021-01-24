package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.android.arkanoid.Activities.GameActivity;
import com.example.android.arkanoid.Classes.OnlineMatch;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

public class MatchItemAdapter extends BaseAdapter {

    private Profile profile;
    private Button btnPlayMatch;
    private TextView tvPlayer1;
    private TextView tvPlayer2;
    private TextView tvStatus;
    private Context context;
    private ArrayList<OnlineMatch> matchList;
    private LayoutInflater inflater;

    public MatchItemAdapter (Context context, Profile profile, ArrayList<OnlineMatch> matchList){
        this.profile = profile;
        this.context = context;
        this.matchList = matchList;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_match, null);
        btnPlayMatch = convertView.findViewById(R.id.btnPlayMatch);
        tvPlayer1 = convertView.findViewById(R.id.tvPlayer1);
        tvPlayer2 = convertView.findViewById(R.id.tvPlayer2);
        tvStatus = convertView.findViewById(R.id.tvMatchStatus);

        tvPlayer1.setText(matchList.get(position).getPlayer1());
        tvPlayer2.setText(matchList.get(position).getPlayer2());
        tvStatus.setText(matchList.get(position).getStatus());

        final int counter = matchList.get(position).getCounter();

        if(counter == 3){
            btnPlayMatch.setEnabled(false);
        }

        setCheckBoxes(convertView, position);

        setPlayMatchListener(matchList.get(position), counter);

        final View convertView1 = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View popupView = LayoutInflater.from(context).inflate(R.layout.online_match_popup, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1300, true);
                TextView tvPopupPlayer1 = popupView.findViewById(R.id.tvPopupPlayer1);
                TextView tvPopupPlayer2 = popupView.findViewById(R.id.tvPopupPlayer2);
                TextView tvFirstMatchScoreP1 = popupView.findViewById(R.id.tvFirstMatchScoreP1);
                TextView tvSecondMatchScoreP1 = popupView.findViewById(R.id.tvSecondMatchScoreP1);
                TextView tvThirdMatchScoreP1 = popupView.findViewById(R.id.tvThirdMatchScoreP1);
                TextView tvFirstMatchScoreP2 = popupView.findViewById(R.id.tvFirstMatchScoreP2);
                TextView tvSecondMatchScoreP2 = popupView.findViewById(R.id.tvSecondMatchScoreP2);
                TextView tvThirdMatchScoreP2 = popupView.findViewById(R.id.tvThirdMatchScoreP2);
                TextView tvWinner = popupView.findViewById(R.id.tvWinner);
                ImageButton btnClose = popupView.findViewById(R.id.btnClose);
                long[] player1Score = matchList.get(position).getPlayer1Score();
                long[] player2Score = matchList.get(position).getPlayer2Score();

                tvPopupPlayer1.setText(matchList.get(position).getPlayer1());
                tvPopupPlayer2.setText(matchList.get(position).getPlayer2());

                if (matchList.get(position).getStatus().equals("Win"))
                    tvWinner.setText("WINNER: " + matchList.get(position).getPlayer1());
                else if (matchList.get(position).getStatus().equals("Lose"))
                    tvWinner.setText("WINNER: " + matchList.get(position).getPlayer2());
                else
                    tvWinner.setText("IN PROGRESS");

                for(int i = 0; i <= counter; i++) {
                    if(i == 1) {
                        tvFirstMatchScoreP1.setVisibility(View.VISIBLE);
                        tvFirstMatchScoreP2.setVisibility(View.VISIBLE);
                        tvFirstMatchScoreP1.setText(String.valueOf(player1Score[0]));
                        tvFirstMatchScoreP2.setText("Waiting opponent");
                        if(player2Score[0] != OnlineMatch.GAME_NOT_PLAYED) {
                            tvFirstMatchScoreP2.setText(String.valueOf(player2Score[0]));
                        }
                    }
                    if(i == 2) {
                        tvSecondMatchScoreP1.setVisibility(View.VISIBLE);
                        tvSecondMatchScoreP2.setVisibility(View.VISIBLE);
                        tvSecondMatchScoreP1.setText(String.valueOf(player1Score[1]));
                        tvSecondMatchScoreP2.setText("Waiting opponent");
                        if(player2Score[1] != OnlineMatch.GAME_NOT_PLAYED) {
                            tvSecondMatchScoreP2.setText(String.valueOf(player2Score[1]));
                        }
                    }
                    if(i == 3) {
                        tvThirdMatchScoreP1.setVisibility(View.VISIBLE);
                        tvThirdMatchScoreP2.setVisibility(View.VISIBLE);
                        tvThirdMatchScoreP1.setText(String.valueOf(player1Score[2]));
                        tvThirdMatchScoreP2.setText("Waiting opponent");
                        if(player2Score[2] != OnlineMatch.GAME_NOT_PLAYED) {
                            tvThirdMatchScoreP2.setText(String.valueOf(player2Score[2]));
                        }
                    }
                }
                popupWindow.setElevation(50);
                popupWindow.setOutsideTouchable(false);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
        return convertView;
    }

    public void setPlayMatchListener(final OnlineMatch match, final int counter) {
        btnPlayMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter < 3){
                    startGame(match);
                }
            }
        });
    }

    private void startGame(OnlineMatch match) {
        Intent intentGame = new Intent(context, GameActivity.class);
        intentGame.putExtra("profile", profile);
        intentGame.putExtra("match", match);
        context.startActivity(intentGame);
    }

    private void setCheckBoxes(View view, int i) {
        CheckBox cbRound1Player1 = view.findViewById(R.id.cbRound1Player1);
        CheckBox cbRound2Player1 = view.findViewById(R.id.cbRound2Player1);
        CheckBox cbRound3Player1 = view.findViewById(R.id.cbRound3Player1);
        CheckBox cbRound1Player2 = view.findViewById(R.id.cbRound1Player2);
        CheckBox cbRound2Player2 = view.findViewById(R.id.cbRound2Player2);
        CheckBox cbRound3Player2 = view.findViewById(R.id.cbRound3Player2);

        OnlineMatch match = matchList.get(i);
        long[] player1Score = match.getPlayer1Score();
        long[] player2Score = match.getPlayer2Score();

        int counter = match.getCounter();

        boolean match1Finished = false;
        boolean match2Finished = false;
        boolean match3Finished = false;

        if(counter > 0 && player2Score[0] != OnlineMatch.GAME_NOT_PLAYED) {
            match1Finished = true;
            cbRound1Player1.setVisibility(View.VISIBLE);
            cbRound1Player2.setVisibility(View.VISIBLE);
        }

        if(counter > 1 && player2Score[1] != OnlineMatch.GAME_NOT_PLAYED) {
            match2Finished = true;
            cbRound2Player1.setVisibility(View.VISIBLE);
            cbRound2Player2.setVisibility(View.VISIBLE);
        }

        if(counter > 2 && player2Score[2] != OnlineMatch.GAME_NOT_PLAYED) {
            match3Finished = true;
            cbRound3Player1.setVisibility(View.VISIBLE);
            cbRound3Player2.setVisibility(View.VISIBLE);
        }

        if (player1Score[0] > player2Score[0] && match1Finished) {
            cbRound1Player1.setChecked(true);
            cbRound1Player2.setChecked(false);
        }
        else if (match1Finished){
            cbRound1Player1.setChecked(false);
            cbRound1Player2.setChecked(true);
        }
        if (player1Score[1] > player2Score[1] && match2Finished) {
            cbRound2Player1.setChecked(true);
            cbRound2Player2.setChecked(false);
        }
        else if (match2Finished){
            cbRound2Player1.setChecked(false);
            cbRound2Player2.setChecked(true);
        }
        if (player1Score[2] > player2Score[2] && match3Finished) {
            cbRound3Player1.setChecked(true);
            cbRound3Player2.setChecked(false);
        }
        else if (match3Finished){
            cbRound3Player1.setChecked(false);
            cbRound3Player2.setChecked(true);
        }
    }
}

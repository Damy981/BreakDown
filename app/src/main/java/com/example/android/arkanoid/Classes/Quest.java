package com.example.android.arkanoid.Classes;

import java.io.Serializable;

public class Quest implements Serializable {
    private final String questText;
    private int progress;
    private final int target;
    private final int reward;
    private final boolean isDaily;
    private boolean isCompleted;
    private boolean isRewardRedeemed;
    public static final long serialVersionUID = 19924200649L;
    public static final int QUEST_DESTROY_BRICKS_100 = 0;
    public static final int QUEST_WIN_5 = 1;  //fatto
    public static final int QUEST_WIN_3_WITH_ALL_LIVES = 2;
    public static final int QUEST_DESTROY_BRICKS_10000 = 3;
    public static final int QUEST_WIN_50_MULTIPLAYER = 4;
    public static final int QUEST_CREATE_LEVEL = 5;  //non ora
    public static final int QUEST_DEFUSE_NITROS = 6;
    public static final int QUEST_TOTAL_NUMBER = 7;
    


    public Quest(String questText, int progress, int target, int reward, boolean isDaily) {
        this.questText = questText;
        this.progress = progress;
        this.target = target;
        this.reward = reward;
        this.isDaily = isDaily;
        isCompleted = false;
        isRewardRedeemed = false;

    }

    public void setProgress(int progress) {
        if(this.progress < this.target) {
            this.progress = progress;
            if (this.progress == this.target){
                this.isCompleted = true;
            }
        }
    }

    public void setCompleted(boolean b) {
        isCompleted = b;
    }

    public String getQuestText() {
        return questText;
    }

    public int getProgress() {
        return progress;
    }

    public int getTarget() {
        return target;
    }

    public int getReward() {
        return reward;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isRewardRedeemed() {
        return isRewardRedeemed;
    }

    public void setRewardRedeemed(boolean rewardRedeemed) {
        isRewardRedeemed = rewardRedeemed;
    }

    public void resetProgress() {
        progress = 0;
    }
}

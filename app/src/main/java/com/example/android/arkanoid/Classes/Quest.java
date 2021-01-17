package com.example.android.arkanoid.Classes;

import java.io.Serializable;

public class Quest implements Serializable {
    private String questText;
    private int progress;
    private int target;
    private int reward;
    private boolean isDaily;
    public static final long serialVersionUID = 19924200649L;
    public static final int QUEST_DESTROY_BRICKS_100 = 0;
    public static final int QUEST_WIN_5 = 1;
    public static final int QUEST_WIN_3_WITH_ALL_LIVES = 2;
    public static final int QUEST_DESTROY_BRICKS_10000 = 3;
    public static final int QUEST_WIN_50_MULTIPLAYER = 4;
    public static final int QUEST_CREATE_LEVEL = 5;
    public static final int QUEST_REGISTER_PROFILE = 6;
    public static final int QUEST_TOTAL_NUMBER = 7;

    public Quest(String questText, int progress, int target, int reward, boolean isDaily) {
        this.questText = questText;
        this.progress = progress;
        this.target = target;
        this.reward = reward;
        this.isDaily = isDaily;

    }

    public void setQuestText(String questText) {
        this.questText = questText;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void setReward(int reward) {
        this.reward = reward;
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


}

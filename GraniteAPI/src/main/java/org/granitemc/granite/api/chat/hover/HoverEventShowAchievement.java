package org.granitemc.granite.api.chat.hover;

import org.granitemc.granite.api.chat.HoverEvent;

public class HoverEventShowAchievement extends HoverEvent {
    String achievementName;

    public HoverEventShowAchievement(String achievementName) {
        this.achievementName = achievementName;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    @Override
    protected String getAction() {
        return "show_achievement";
    }

    @Override
    public Object getValue() {
        return achievementName;
    }
}

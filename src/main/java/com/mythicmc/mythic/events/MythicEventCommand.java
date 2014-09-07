package com.mythicmc.mythic.events;

import com.mythicmc.mythic.player.MythicPlayer;

public class MythicEventCommand extends MythicEventCancellable {
    public String[] parameters = new String[0];
    public MythicPlayer player = null;
}

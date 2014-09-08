package com.mythicmc.mythic;

import com.mythicmc.mythic.utils.Logger;
import com.mythicmc.mythic.utils.ServerComposite;

public class Main {

    public static void main(String[] args) {
    	new MythicStartupThread("MythicStartup", args).run();
    }
}

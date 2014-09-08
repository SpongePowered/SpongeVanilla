package org.granitemc.granite;

import org.granitemc.granite.utils.Logger;
import org.granitemc.granite.utils.ServerComposite;

public class Main {

    public static void main(String[] args) {
    	new MythicStartupThread("MythicStartup", args).run();
    }
}

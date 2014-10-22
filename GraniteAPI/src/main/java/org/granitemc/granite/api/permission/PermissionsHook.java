package org.granitemc.granite.api.permission;

import org.granitemc.granite.api.entity.player.Player;

public interface PermissionsHook {
    boolean hasPermission(Player player, String node);
}

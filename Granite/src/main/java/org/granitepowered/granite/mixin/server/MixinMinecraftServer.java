package org.granitepowered.granite.mixin.server;

import com.google.common.base.Optional;
import mc.ServerConfigurationManager;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.net.ChannelListener;
import org.spongepowered.api.net.ChannelRegistrationException;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NonnullByDefault
@Mixin(value = mc.MinecraftServer.class, remap = false)
public abstract class MixinMinecraftServer implements Server, ConsoleSource {

    @Shadow
    private String hostname;

    @Shadow
    private int serverPort;

    @Shadow
    private ServerConfigurationManager serverConfigManager;

    @Shadow
    private int tickCount;

    @Shadow
    private boolean onlineMode;

    @Shadow
    private String motd;

    @Override
    public Collection<Player> getOnlinePlayers() {
        throw new NotImplementedException("");
    }

    @Override
    public int getMaxPlayers() {
        return this.serverConfigManager.maxPlayers;
    }

    @Override
    public Optional<Player> getPlayer(UUID uuid) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Player> getPlayer(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<World> getWorlds() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<World> getWorld(UUID uuid) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<World> getWorld(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<World> loadWorld(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean unloadWorld(World world) {
        throw new NotImplementedException("");
    }

    @Override
    public World createWorld(String s, WorldGenerator worldGenerator, long l) {
        throw new NotImplementedException("");
    }

    @Override
    public World createWorld(String s, WorldGenerator worldGenerator) {
        throw new NotImplementedException("");
    }

    @Override
    public World createWorld(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public int getRunningTimeTicks() {
        return this.tickCount;
    }

    @Override
    public void broadcastMessage(Message message) {
        for (Player player : getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    @Override
    public Optional<InetSocketAddress> getBoundAddress() {
        return Optional.fromNullable(new InetSocketAddress(this.hostname, this.serverPort));
    }

    @Override
    public boolean hasWhitelist() {
        return this.serverConfigManager.whiteListEnforced;
    }

    @Override
    public void setHasWhitelist(boolean whitListEnforced) {
        this.serverConfigManager.whiteListEnforced = whitListEnforced;
    }

    @Override
    public boolean getOnlineMode() {
        return this.onlineMode;
    }

    @Override
    public Message getMotd() {
        return Messages.of(this.motd).builder().build();
    }

    @Override
    public void shutdown(Message message) {
        throw new NotImplementedException("");
    }

    @Override
    public void registerChannel(Object o, ChannelListener channelListener, String s) throws ChannelRegistrationException {
        throw new NotImplementedException("");
    }

    @Override
    public List<String> getRegisteredChannels() {
        throw new NotImplementedException("");
    }
}

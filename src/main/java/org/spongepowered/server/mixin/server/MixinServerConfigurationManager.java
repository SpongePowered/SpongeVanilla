package org.spongepowered.server.mixin.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ServerConfigurationManager.class, priority = 999)
public abstract class MixinServerConfigurationManager {

    @Shadow @Final MinecraftServer mcServer;

    // Note: This is needed to add Forge NMS-only method so common can overwrite
    public void transferPlayerToDimension(EntityPlayerMP playerIn, int dimension, Teleporter teleporter) {

    }

    public void transferEntityToWorld(Entity entityIn, int fromDimensionId, WorldServer fromWorld, WorldServer toWorld, net.minecraft.world.Teleporter teleporter) {

    }

    @Overwrite
    public void transferPlayerToDimension(EntityPlayerMP playerMP, int dimension) {
        this.transferPlayerToDimension(playerMP, dimension, mcServer.worldServerForDimension(dimension).getDefaultTeleporter());
    }

    @Overwrite
    public void transferEntityToWorld(Entity entityIn, int fromDimensionId, WorldServer fromWorld, WorldServer toWorld) {
        this.transferEntityToWorld(entityIn, fromDimensionId, fromWorld, toWorld, toWorld.getDefaultTeleporter());
    }
}

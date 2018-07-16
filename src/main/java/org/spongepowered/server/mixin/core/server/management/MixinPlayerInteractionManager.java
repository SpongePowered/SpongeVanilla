package org.spongepowered.server.mixin.core.server.management;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImplHooks;
import org.spongepowered.common.event.SpongeCommonEventFactory;
import org.spongepowered.common.interfaces.entity.player.IMixinEntityPlayerMP;
import org.spongepowered.common.util.VecHelper;

@Mixin(PlayerInteractionManager.class)
public abstract class MixinPlayerInteractionManager {

    @Shadow public EntityPlayerMP player;

    @Inject(method = "onBlockClicked", at = @At("HEAD"), cancellable = true)
    public void onBlockClicked(BlockPos pos, EnumFacing side, CallbackInfo ci) {
        final BlockSnapshot blockSnapshot = new Location<>((World) this.player.world, VecHelper.toVector3d(pos)).createSnapshot();
        final RayTraceResult result = SpongeImplHooks.rayTraceEyes(this.player, SpongeImplHooks.getBlockReachDistance(this.player));

        if (SpongeCommonEventFactory.callInteractBlockEventPrimary(this.player, blockSnapshot, EnumHand.MAIN_HAND, side, VecHelper.toVector3d(result.hitVec)).isCancelled()) {
            ((IMixinEntityPlayerMP) this.player).sendBlockChange(pos, this.player.world.getBlockState(pos));
            // Copied from Forge PlayerInteractionManager
            player.world.notifyBlockUpdate(pos, player.world.getBlockState(pos), player.world.getBlockState(pos), 3);
            ci.cancel();
        }
    }

}

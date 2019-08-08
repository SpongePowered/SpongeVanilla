package org.spongepowered.server.mixin.core.item.crafting;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingManager.class)
public abstract class CraftingManagerMixin_Vanilla {

    @Inject(method = "register(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/item/crafting/IRecipe;)V", at = @At(value = "NEW"), cancellable = true)
    private static void vanilla$register(final ResourceLocation name, final IRecipe recipe, final CallbackInfo ci) {
        final int id = CraftingManager.REGISTRY.getIDForObject(CraftingManager.REGISTRY.getObject(name));
        CraftingManager.REGISTRY.register(id, name, recipe);
        ci.cancel();
    }

}

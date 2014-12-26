package org.granitepowered.granite.mc;

@Implement(name = "Enchantment")
public interface MCEnchantment extends MCInterface {

    String fieldGet$name();

    int fieldGet$weight();

    int getMinLevel();

    int getMaxLevel();

    int getMinEnchantability(int level);

    int getMaxEnchantability(int level);

    boolean canApply(MCItemStack itemStack);

    boolean canApplyTogether(MCEnchantment enchantment);

}

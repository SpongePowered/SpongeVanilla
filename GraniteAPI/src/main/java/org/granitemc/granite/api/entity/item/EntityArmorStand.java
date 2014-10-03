package org.granitemc.granite.api.entity.item;

import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Rotations;

public interface EntityArmorStand {

    ItemStack getHeldItem();

    ItemStack getEquipmentInSlot(int slot);

    ItemStack getCurrentArmor(int slot);

    void setCurrentItemOrArmor(int slot, ItemStack itemStack);

    ItemStack[] getInventory();

    boolean canBePushed();

    // TODO: Enable when DamageSource has been made
    /*boolean attackEntityFrom(DamageSource source, float amount);*/

    void playParticles();

    void dropBlock();

    void dropContents();

    void setInvisible(boolean invisible);

    boolean isChild();

    void kill();

    boolean isInvisible();

    void setSmall(boolean size);

    boolean isSmall();

    void setNoGravity(boolean gravity);

    boolean hasNoGravity();

    void setShowArms(boolean showArms);

    boolean getShowArms();

    void setNoBasePlate(boolean basePlate);

    boolean hasNoBasePlate();

    void setHeadRotation(Rotations rotations);

    void setBodyRotation(Rotations rotations);

    void setLeftArmRotation(Rotations rotations);

    void setRightArmRotation(Rotations rotations);

    void setLeftLegRotation(Rotations rotations);

    void setRightLegRotation(Rotations rotations);

    Rotations getHeadRotation();

    Rotations getBodyRotation();

    Rotations getLeftArmRotation();

    Rotations getRightArmRotation();

    Rotations getLeftLegRotation();

    Rotations getRightLegRotation();

}

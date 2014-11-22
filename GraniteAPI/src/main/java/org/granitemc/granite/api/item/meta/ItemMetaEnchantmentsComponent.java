package org.granitemc.granite.api.item.meta;

import org.granitemc.granite.api.item.EnchantmentType;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.nbt.NBTCompound;
import org.granitemc.granite.api.utils.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ItemMetaEnchantmentsComponent extends ItemMetaComponent {
    private List<EnchantmentWithLevel> enchantments;

    private boolean hasGlow;

    public ItemMetaEnchantmentsComponent(ItemMeta meta) {
        super(meta);
    }

    public void addGlow() {
        hasGlow = true;
        save();
    }

    public void removeGlowAndAllEnchantments() {
        enchantments.clear();
        hasGlow = false;
        save();
    }

    public List<EnchantmentWithLevel> getEnchantments() {
        return enchantments;
    }

    public boolean hasGlow() {
        return hasGlow;
    }

    @Override
    public void save(NBTCompound nbt) {
        if (!hasGlow) {
            nbt.removeNBTTag("ench");
        } else {
            List<NBTCompound> ench = new ArrayList<>();

            for (EnchantmentWithLevel ewl : enchantments) {
                NBTCompound comp = new NBTCompound();
                comp.setShort("lvl", (short) ewl.getLevel());
                comp.setShort("id", (short) ewl.getEnchantmentType().getId());

                ench.add(comp);

                ewl.setParent(this);
            }

            nbt.setList("ench", ench);
        }
    }

    @Override
    public void load(NBTCompound nbt) {
        ObservableList.Listener listener = new ObservableList.Listener() {
            @Override
            public void update() {
                hasGlow = true;
                save();
            }
        };

        if (!nbt.hasKey("ench")) {
            enchantments = new ObservableList<>(listener);
            hasGlow = false;
        } else {
            List<NBTCompound> nbtEnch = nbt.getList("ench");

            List<EnchantmentWithLevel> enchNotObservable = new ArrayList<>();

            for (NBTCompound comp : nbtEnch) {
                EnchantmentType thisType = null;

                for (EnchantmentType type : EnchantmentType.values()) {
                    if (type.getId() == comp.getShort("id")) {
                        thisType = type;
                    }
                }

                enchNotObservable.add(new EnchantmentWithLevel(thisType, comp.getShort("lvl")));
            }

            enchantments = new ObservableList<>(enchNotObservable, listener);
        }
    }

    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return true;
    }

    public static class EnchantmentWithLevel {
        private EnchantmentType enchantmentType;
        private int level;

        private ItemMetaEnchantmentsComponent parent;

        public EnchantmentWithLevel(EnchantmentType enchantmentType, int level) {
            this.enchantmentType = enchantmentType;
            this.level = level;
        }

        public EnchantmentType getEnchantmentType() {
            return enchantmentType;
        }

        public int getLevel() {
            return level;
        }

        public void setEnchantmentType(EnchantmentType enchantmentType) {
            this.enchantmentType = enchantmentType;
            parent.save();
        }

        public void setLevel(int level) {
            this.level = level;
            parent.save();
        }

        void setParent(ItemMetaEnchantmentsComponent parent) {
            this.parent = parent;
        }
    }
}

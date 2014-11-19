package org.granitemc.granite.api.item.meta;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.chat.FormattingOutputType;
import org.granitemc.granite.api.chat.TextComponent;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.nbt.NBTCompound;
import org.granitemc.granite.api.item.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class ItemMeta {

    protected ItemStack itemStack;

    public ItemMeta(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean hasDisplayName() throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        if (nbtCompound.hasKey("display")) {
            if (nbtCompound.getNBTCompound("display").hasKey("Name")) {
                return true;
            }
        }
        return false;
    }

    public String getDisplayName() throws IllegalAccessException, InstantiationException {
        if (!hasDisplayName()) return null;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        return nbtCompound.getNBTCompound("display").getString("Name");
    }

    public void setDisplayName(ChatComponent name) throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        if (!hasDisplayName()) {
            nbtCompound.setNBTCompound("display", new NBTCompound());
        }
        nbtCompound.getNBTCompound("display").setString("Name", name.toPlainText(FormattingOutputType.MINECRAFT));
        itemStack.setNBTCompound(nbtCompound);
    }

    public void removeDisplayName() throws IllegalAccessException, InstantiationException {
        if (!hasDisplayName()) return;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.getNBTCompound("display").removeNBTTag("Name");
        itemStack.setNBTCompound(nbtCompound);
    }

    public boolean hasEnchantmentGlow() throws IllegalAccessException, InstantiationException {
        return itemStack.getNBTCompound().hasKey("ench");
    }

    public void addEnchantmentGlow() throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        if (!nbtCompound.hasKey("ench")) {
            nbtCompound.setList("ench", new ArrayList());
            itemStack.setNBTCompound(nbtCompound);
        }
    }

    public boolean hasEnchantments() throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        List enchantmentNBTCompound = nbtCompound.getList("ench");
        return nbtCompound.hasKey("ench") && !enchantmentNBTCompound.isEmpty();
    }

    public List<? extends String> getEnchantments() throws IllegalAccessException, InstantiationException {
        if (!hasEnchantments()) return new ArrayList<String>();
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        List<String> enchantmentsList = new ArrayList<>();
        List<NBTCompound> enchantments = nbtCompound.getList("ench");
        for (NBTCompound nbt : enchantments) {
            for (Enchantment enchantment : Enchantment.values()) {
                if (nbt.getShort("id") == enchantment.getId()) {
                    enchantmentsList.add("Name: " + enchantment + " Level: " + nbt.getShort("lvl"));
                }
            }
        }
        return enchantmentsList;
    }

    public void addEnchantment(Enchantment enchantment, int level) throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        List<NBTCompound> enchantments = new ArrayList<>();
        NBTCompound enchantmentCompound = new NBTCompound();
        enchantmentCompound.setShort("id", (short) enchantment.getId());
        enchantmentCompound.setShort("lvl", (short) ((byte) level));
        enchantments.add(enchantmentCompound);
        if (!hasEnchantments()) {
            nbtCompound.setList("ench", enchantments);
            itemStack.setNBTCompound(nbtCompound);
        } else {
            List currentEnchantments = nbtCompound.getList("ench");
            currentEnchantments.add(enchantments);
            nbtCompound.setList("ench", currentEnchantments);
            itemStack.setNBTCompound(nbtCompound);
        }
    }

    public void removeEnchantment(Enchantment enchantment) throws IllegalAccessException, InstantiationException {
        if (!hasEnchantments()) return;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        List<NBTCompound> enchantments = nbtCompound.getList("ench");
        if (enchantments.size() == 1) {
            nbtCompound.removeNBTTag("ench");
        } else {
            for (int i = 0; i < enchantments.size(); i++) {
                if (enchantments.get(i).getShort("id") == enchantment.getId()) enchantments.remove(i);
            }
            nbtCompound.setList("ench", enchantments);
        }
        itemStack.setNBTCompound(nbtCompound);
    }

    public void removeAllEnchantments() throws IllegalAccessException, InstantiationException {
        if (!hasEnchantments()) return;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.removeNBTTag("ench");
        itemStack.setNBTCompound(nbtCompound);
    }

    public boolean hasLore() throws IllegalAccessException, InstantiationException {
        return itemStack.getNBTCompound().hasKey("display") && itemStack.getNBTCompound().getNBTCompound("display").hasKey("Lore");
    }

    public void addLoreLine(ChatComponent lore, int line) throws IllegalAccessException, InstantiationException {
        if (line == 0) line = 1;
        List<ChatComponent> loreList = getLore();
        loreList.add(line - 1, lore);
        setLore(loreList);
    }

    public List<ChatComponent> getLore() throws IllegalAccessException, InstantiationException {
        if (!hasLore()) return new ArrayList<>();
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        List<ChatComponent> lore = new ArrayList<>();
        for (Object string : nbtCompound.getNBTCompound("display").getList("Lore")) {
            lore.add(new TextComponent((String) string));
        }
        return lore;
    }

    public void setLore(List<ChatComponent> lore) throws IllegalAccessException, InstantiationException {
        if (lore.isEmpty()) removeAllLore();
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        NBTCompound displayCompound = new NBTCompound();
        List<String> loreList = new ArrayList<>();
        for (ChatComponent string : lore) {
            loreList.add(string.toPlainText(FormattingOutputType.MINECRAFT));
        }
        displayCompound.setList("Lore", loreList);
        nbtCompound.setNBTCompound("display", displayCompound);
        itemStack.setNBTCompound(nbtCompound);
    }

    public void removeLoreLine(int line) throws IllegalAccessException, InstantiationException {
        if (!hasLore()) return;
        if (line == 0) line = 1;
        List<ChatComponent> loreList = getLore();
        loreList.remove(line - 1);
        setLore(loreList);
    }

    public void removeAllLore() throws IllegalAccessException, InstantiationException {
        if (!hasLore()) return;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.getNBTCompound("display").removeNBTTag("Lore");
        itemStack.setNBTCompound(nbtCompound);
    }

}

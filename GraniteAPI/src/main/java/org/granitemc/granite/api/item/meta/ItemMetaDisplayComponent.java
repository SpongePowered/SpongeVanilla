package org.granitemc.granite.api.item.meta;

import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.chat.FormattingOutputType;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.nbt.NBTCompound;
import org.granitemc.granite.api.utils.ObservableList;

import java.util.List;

public class ItemMetaDisplayComponent extends ItemMetaComponent {
    private String name;
    private List<String> lore;

    public ItemMetaDisplayComponent(ItemMeta meta) {
        super(meta);
    }

    public String getName() {
        return name == null ? getItemStack().getName() : name;
    }

    public void setName(String name) {
        this.name = name;
        save();
    }

    public void setName(ChatComponent component) {
        setName(component.toPlainText(FormattingOutputType.MINECRAFT));
    }

    public List<String> getLore() {
        return lore;
    }

    @Override
    public void save(NBTCompound nbt) {
        NBTCompound display = new NBTCompound();

        //if (!getName().equals(getItemStack().getName())) {
            display.setString("Name", name);
        //}

        if (!getLore().isEmpty()) {
            display.setList("Lore", lore);
        }

        nbt.setNBTCompound("display", display);
    }

    @Override
    public void load(NBTCompound nbt) {
        if (!nbt.hasKey("display")) nbt.setNBTCompound("display", new NBTCompound());
        NBTCompound display = nbt.getNBTCompound("display");
        name = display.getString("Name");

        List tempLore = display.getList("Lore");

        ObservableList.Listener listener = new ObservableList.Listener() {
            @Override
            public void update() {
                save();
            }
        };

        if (tempLore == null) {
            lore = new ObservableList<>(listener);
        } else {
            lore = new ObservableList<>(tempLore, listener);
        }
    }

    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return true;
    }
}

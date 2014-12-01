package org.granitemc.granite.api.item.meta;

import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.chat.TextComponent;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.item.ItemTypes;
import org.granitemc.granite.api.nbt.NBTCompound;
import org.granitemc.granite.api.utils.ObservableList;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

public class ItemMetaBookComponent extends ItemMetaComponent {
    private List<ChatComponent> pages;
    private String title;
    private String author;

    public ItemMetaBookComponent(ItemMeta meta) {
        super(meta);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        save();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        save();
    }

    public List<ChatComponent> getPages() {
        return pages;
    }

    @Override
    public void save(NBTCompound nbt) {
        if (title == null) {
            nbt.removeNBTTag("title");
        } else {
            nbt.setString("title", title);
        }

        if (author == null) {
            nbt.removeNBTTag("author");
        } else {
            nbt.setString("author", author);
        }

        List<String> pagesList = new ArrayList<>();

        for (ChatComponent page : pages) {
            pagesList.add(page.toJson());
        }

        nbt.setList("pages", pagesList);
    }

    @Override
    public void load(NBTCompound nbt) {
        if (nbt.hasKey("title")) {
            title = nbt.getString("title");
        } else {
            title = null;
        }

        if (nbt.hasKey("author")) {
            author = nbt.getString("author");
        } else {
            author = null;
        }

        ObservableList.Listener listener = new ObservableList.Listener() {
            @Override
            public void update() {
                save();
            }
        };

        if (nbt.hasKey("pages")) {
            List<String> pagesList = nbt.getList("pages");

            List<ChatComponent> pagesTemp = new ArrayList<>();

            for (String pageStr : pagesList) {
                try {
                    while (JSONValue.parse(pageStr) instanceof String) {
                        pageStr = (String) JSONValue.parse(pageStr);
                    }

                    pagesTemp.add(ChatComponent.fromJson(pageStr, false));
                } catch (Exception e) {
                    if (e.getMessage() != null && e.getMessage().contains("Invalid tag encountered")) {
                        pagesTemp.add(new TextComponent(pageStr));
                    } else {
                        throw e;
                    }
                }
            }

            pages = new ObservableList<>(pagesTemp, listener);
        } else {
            pages = new ObservableList<>(listener);
        }
    }

    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return itemStack.getType().equals(ItemTypes.writable_book) || itemStack.getType().equals(ItemTypes.written_book);
    }
}

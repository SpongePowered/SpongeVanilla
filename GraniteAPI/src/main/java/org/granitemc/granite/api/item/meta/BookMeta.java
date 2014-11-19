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

import java.util.ArrayList;
import java.util.List;

public class BookMeta extends ItemMeta {

    public BookMeta(ItemStack itemStack) {
        super(itemStack);
    }

    //People may want this if they are going to use RAW NBT that we have not implemented. But will leave private for now!
    private void setResolved() throws InstantiationException, IllegalAccessException {
        if (itemStack.getNBTCompound().hasKey("resolved")) return;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.setByte("resolved", (byte) 1);
        itemStack.setNBTCompound(nbtCompound);
    }

    public boolean hasTitle() throws InstantiationException, IllegalAccessException {
        return itemStack.getNBTCompound().hasKey("title") && !itemStack.getNBTCompound().getString("title").equals("");
    }

    public ChatComponent getTitle() throws IllegalAccessException, InstantiationException {
        if (!itemStack.getNBTCompound().hasKey("title")) return new TextComponent("");
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        return new TextComponent(nbtCompound.getString("title"));
    }

    public void setTitle(ChatComponent title) throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.setString("title", title.toPlainText(FormattingOutputType.MINECRAFT));
        itemStack.setNBTCompound(nbtCompound);
        setResolved();
    }

    public void removeTitle() throws IllegalAccessException, InstantiationException {
        if (!itemStack.getNBTCompound().hasKey("title")) return;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.removeNBTTag("title");
        itemStack.setNBTCompound(nbtCompound);
        setResolved();
    }

    public boolean hasAuthor() throws InstantiationException, IllegalAccessException {
        return itemStack.getNBTCompound().hasKey("author");
    }

    public ChatComponent getAuthor() throws IllegalAccessException, InstantiationException {
        if (!itemStack.getNBTCompound().hasKey("author")) return new TextComponent("");
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        return new TextComponent(nbtCompound.getString("author"));
    }

    public void setAuthor(ChatComponent author) throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.setString("author", author.toPlainText(FormattingOutputType.MINECRAFT));
        itemStack.setNBTCompound(nbtCompound);
        setResolved();
    }

    public void removeAuthor() throws IllegalAccessException, InstantiationException {
        if (!itemStack.getNBTCompound().hasKey("author")) return;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.removeNBTTag("author");
        itemStack.setNBTCompound(nbtCompound);
    }

    public boolean hasPages() throws InstantiationException, IllegalAccessException {
        return itemStack.getNBTCompound().hasKey("pages");
    }

    public void addPage(ChatComponent page, int pageNumber) throws IllegalAccessException, InstantiationException {
        if (pageNumber == 0) pageNumber = 1;
        List<ChatComponent> pages = getPages();
        pages.add(pageNumber - 1, page);
        setPages(pages);
    }

    public List getPages() throws IllegalAccessException, InstantiationException {
        if (!itemStack.getNBTCompound().hasKey("pages")) return new ArrayList<>();
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        List pages = nbtCompound.getList("pages");
        List chatComponents = new ArrayList();
        for (Object page : pages) {
            chatComponents.add(new TextComponent((String) page));
        }
        return chatComponents;
    }

    public void setPages(List<ChatComponent> pages) throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        List<String> bookPages = new ArrayList();
        for (ChatComponent bookPage : pages) {
            bookPages.add("\"" + bookPage.toPlainText(FormattingOutputType.MINECRAFT) + "\"");
        }
        nbtCompound.setList("pages", bookPages);
        itemStack.setNBTCompound(nbtCompound);
        setResolved();
    }

    public void removePage(int pageNumber) throws IllegalAccessException, InstantiationException {
        if (pageNumber == 0) pageNumber = 1;
        List<ChatComponent> pages = getPages();
        pages.remove(pageNumber - 1);
        setPages(pages);
    }

    public void removeAllPages() throws IllegalAccessException, InstantiationException {
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        if (!nbtCompound.hasKey("pages"))
            return;
        nbtCompound.removeNBTTag("pages");
        itemStack.setNBTCompound(nbtCompound);
    }

    public boolean isCopied() throws InstantiationException, IllegalAccessException {
        return itemStack.getNBTCompound().hasKey("generation");
    }

    public Integer getCopyVersion() throws InstantiationException, IllegalAccessException {
        if (isCopied()) return 0;
        return itemStack.getNBTCompound().getInt("generation");
    }

    public void setCopyVersion(Integer version) throws IllegalAccessException, InstantiationException {
        if (version == 0) return;
        NBTCompound nbtCompound = itemStack.getNBTCompound();
        nbtCompound.setInt("generation", version);
        itemStack.setNBTCompound(nbtCompound);
        setResolved();
    }

    @Override
    public void setDisplayName(ChatComponent name) throws InstantiationException, IllegalAccessException {
        setTitle(name);
    }

}

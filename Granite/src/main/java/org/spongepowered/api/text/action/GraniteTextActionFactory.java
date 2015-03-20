/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

package org.spongepowered.api.text.action;

import org.granitepowered.granite.impl.text.action.GraniteClickAction;
import org.granitepowered.granite.impl.text.action.GraniteHoverAction;
import org.granitepowered.granite.impl.text.action.GraniteShiftClickAction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.message.Message;

import java.net.URL;

public class GraniteTextActionFactory implements TextActionFactory {

    @Override
    public ClickAction.OpenUrl createOpenUrl(URL url) {
        return new GraniteClickAction.GraniteOpenUrl(url);
    }

    @Override
    public ClickAction.RunCommand createRunCommand(String command) {
        return new GraniteClickAction.GraniteRunCommand(command);
    }

    @Override
    public ClickAction.ChangePage createChangePage(int page) {
        return new GraniteClickAction.GraniteChangePage(page);
    }

    @Override
    public ClickAction.SuggestCommand createSuggestCommand(String command) {
        return new GraniteClickAction.GraniteSuggestCommand(command);
    }

    @Override
    public HoverAction.ShowText createShowText(Message text) {
        return new GraniteHoverAction.GraniteShowText(text);
    }

    @Override
    public HoverAction.ShowItem createShowItem(ItemStack item) {
        return new GraniteHoverAction.GraniteShowItem(item);
    }

    @Override
    public HoverAction.ShowAchievement createShowAchievement(Object achievement) {
        return new GraniteHoverAction.GraniteShowAchievement(achievement);
    }

    @Override
    public HoverAction.ShowEntity createShowEntity(Entity entity) {
        return new GraniteHoverAction.GraniteShowEntity(entity);
    }

    @Override
    public ShiftClickAction.InsertText createInsertText(String text) {
        return new GraniteShiftClickAction.GraniteInsertText(text);
    }
}

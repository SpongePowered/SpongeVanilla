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

package org.granitepowered.granite.impl.block.data;

import org.apache.commons.lang3.NotImplementedException;
import mc.MCIChatComponent;
import mc.MCTileEntitySign;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.block.data.Sign;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.text.message.Message;

public class GraniteSign extends GraniteTileEntity<MCTileEntitySign> implements Sign {

    public GraniteSign(Object obj) {
        super(obj);
    }

    @Override
    public DataContainer toContainer() {
        // TODO
        throw new NotImplementedException("");
    }

    @Override
    public Message[] getLines() {
        Message[] ret = new Message[4];

        MCIChatComponent[] fieldGet$signText = obj.signText;
        for (int i = 0; i < fieldGet$signText.length; i++) {
            MCIChatComponent component = fieldGet$signText[i];
            ret[i] = MinecraftUtils.minecraftToGraniteMessage(component);
        }

        return ret;
    }

    @Override
    public void setLines(Message... lines) {
        for (int i = 0; i < lines.length; i++) {
            Message line = lines[i];

            if (i < 4) {
                obj.signText[i] = MinecraftUtils.graniteToMinecraftChatComponent(line);
            }
        }
    }

    @Override
    public Message getLine(int index) throws IndexOutOfBoundsException {
        if (index >= 4) {
            throw new IndexOutOfBoundsException();
        }
        return MinecraftUtils.minecraftToGraniteMessage(obj.signText[index]);
    }

    @Override
    public void setLine(int index, Message text) throws IndexOutOfBoundsException {
        if (index >= 4) {
            throw new IndexOutOfBoundsException();
        }
        obj.signText[index] = MinecraftUtils.graniteToMinecraftChatComponent(text);
    }
}

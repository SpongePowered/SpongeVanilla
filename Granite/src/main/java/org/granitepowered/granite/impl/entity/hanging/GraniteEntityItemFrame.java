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

package org.granitepowered.granite.impl.entity.hanging;

import com.google.common.base.Optional;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.mc.MCEntityItemFrame;
import org.granitepowered.granite.mc.MCItemStack;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.hanging.ItemFrame;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.rotation.Rotation;

public class GraniteEntityItemFrame extends GraniteEntityHanging<MCEntityItemFrame> implements ItemFrame {

    public GraniteEntityItemFrame(MCEntityItemFrame obj) {
        super(obj);
    }

    @Override
    public Optional<ItemStack> getItem() {
        return Optional
                .fromNullable((ItemStack) MinecraftUtils.wrap((MCItemStack) obj.fieldGet$dataWatcher().getWatchedObject(8).fieldGet$watchedObject()));
    }

    @Override
    public void setItem(ItemStack itemStack) {
        obj.setDisplayedItem((MCItemStack) MinecraftUtils.unwrap(itemStack));
    }

    @Override
    public Rotation getItemRotation() {
        return Granite.getInstance().getRegistry().getRotationFromDegree((int) obj.fieldGet$dataWatcher().getWatchedObject(9).fieldGet$watchedObject()).get();
    }

    @Override
    public void setRotation(Rotation rotation) {
        obj.setItemRotation(rotation.getAngle());
    }
}

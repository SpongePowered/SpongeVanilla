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

package org.granitepowered.granite.impl.entity.living.monster;

import com.google.common.base.Optional;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.block.GraniteBlockState;
import org.granitepowered.granite.impl.block.GraniteBlockType;
import org.granitepowered.granite.mc.MCEntityEnderman;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.monster.Enderman;

public class GraniteEntityEnderman extends GraniteEntityMonster<MCEntityEnderman> implements Enderman {

    public GraniteEntityEnderman(MCEntityEnderman obj) {
        super(obj);
    }

    @Override
    public Optional<BlockState> getCarriedBlock() {
        GraniteBlockType blockType = (GraniteBlockType) Granite.getInstance().getRegistry().getBlock("air").get();
        return Optional.fromNullable((BlockState) MinecraftUtils
                .wrap(blockType.obj.getStateFromId((short) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 65535)));
    }

    @Override
    public void setCarriedBlock(BlockState carriedBlock) {
        GraniteBlockType blockType = (GraniteBlockType) Granite.getInstance().getRegistry().getBlock("air").get();
        obj.fieldGet$dataWatcher().updateObject(16, (short) (
                blockType.obj.getStateId(MinecraftUtils.unwrap((GraniteBlockState) carriedBlock).fieldGet$block().fieldGet$defaultBlockState())
                & 65535));

    }

    @Override
    public boolean isScreaming() {
        return (byte) obj.fieldGet$dataWatcher().getWatchedObject(18).fieldGet$watchedObject() > 0;
    }

    @Override
    public void setScreaming(boolean screaming) {
        obj.fieldGet$dataWatcher().updateObject(18, (byte) (screaming ? 1 : 0));
    }
}

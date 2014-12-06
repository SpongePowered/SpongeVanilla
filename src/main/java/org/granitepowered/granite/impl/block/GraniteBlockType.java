/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

package org.granitepowered.granite.impl.block;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.text.translation.Translation;

public class GraniteBlockType extends Composite implements BlockType {

    public GraniteBlockType(Object parent) {
        super(parent);
    }

    @Override
    public String getId() {
        Object registry = null;
        try {
            registry = Mappings.getField("Block", "blockRegistry").get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Object resourceLocation = Mappings.invoke(registry, "getNameForObject", parent);
        return (String) fieldGet(resourceLocation, "resourcePath");
    }

    @Override
    public BlockState getDefaultState() {
        return (BlockState) MinecraftUtils.wrapComposite(fieldGet("defaultBlockState"));
    }

    @Override
    public BlockState getStateFromDataValue(byte b) {
        return (BlockState) MinecraftUtils.wrapComposite(invoke("getStateFromMeta", b));
    }

    @Override
    public Translation getTranslation() {
        // TODO: Wait for Message API (see Block.getUnlocalizedName() and Block.getLocalizedName(), make sure not to use the field as the method is overridden
        throw new NotImplementedException("");
    }
}

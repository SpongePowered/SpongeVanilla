/*
 * This file is part of Granite, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.spongepowered.granite.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.translation.Translation;

public class GraniteEntityType implements EntityType {

    private final String id;
    private final int entityTypeId;
    private final Class<? extends Entity> entityClass;

    public GraniteEntityType(String id) {
        this.id = id;
        this.entityTypeId = EntityList.getIDFromString(id);
        this.entityClass = EntityList.getClassFromID(EntityList.getIDFromString(id));
    }

    public GraniteEntityType(String id, Class<? extends Entity> entityClass) {
        this.id = id;
        this.entityTypeId = EntityList.getIDFromString(id);
        this.entityClass = entityClass;
    }

    public int getEntityTypeId() {
        return this.entityTypeId;
    }

    @Override
    public String getId() {
        return "minecratf:" + this.id;
    }

    @Override
    public Class<? extends org.spongepowered.api.entity.Entity> getEntityClass() {
        return (Class<? extends org.spongepowered.api.entity.Entity>) this.entityClass;
    }

    @Override
    public Translation getTranslation() {
        throw new NotImplementedException("");
    }
}


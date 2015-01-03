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

package org.granitepowered.granite.impl.entity.living;

import org.granitepowered.granite.mc.MCEntityLiving;

public class GraniteLiving extends GraniteLivingBase<MCEntityLiving> {

    public GraniteLiving(MCEntityLiving obj) {
        super(obj);
    }

    @Override
    public boolean isPersistent() {
        return obj.fieldGet$persistenceRequired();
    }

    @Override
    public void setPersistent(boolean b) {
        obj.fieldSet$persistenceRequired(b);
    }

    @Override
    public boolean getCanPickupItems() {
        return obj.fieldGet$canPickUpLoot();
    }

    @Override
    public void setCanPickupItems(boolean canPickupItems) {
        obj.fieldSet$canPickUpLoot(canPickupItems);
    }

    @Override
    public boolean isLeashed() {
        return obj.fieldGet$isLeashed();
    }

    @Override
    public void setLeashed(boolean leashed) {
        obj.fieldSet$isLeashed(leashed);
    }
}

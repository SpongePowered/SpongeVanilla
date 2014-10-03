package org.granitemc.granite.api.entity.item;

/*****************************************************************************************
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
 ****************************************************************************************/

import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.item.ItemStack;

public interface EntityItem extends Entity {

    void searchForOtherItemsNearby();

    void setAgeToCreativeDespawnTime();

    void dealFireDamage(int amount);

    String getName();

    ItemStack getEntityItem();

    void setEntityItemStack(ItemStack itemStack);

    String getOwner();

    void setOwner(String owner);

    String getThrower();

    void setThrower(String thrower);

    //func_174872_o()
    int getAge();

    void setDefaultPickupDelay();

    void setNoPickupDelay();

    void setInfinitePickupDelay();

    void setPickupDelay(int ticks);

    //func_174874_s()
    boolean canPickUp();

}

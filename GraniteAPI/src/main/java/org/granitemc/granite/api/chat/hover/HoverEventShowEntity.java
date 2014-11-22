package org.granitemc.granite.api.chat.hover;

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

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.chat.HoverEvent;
import org.granitemc.granite.api.entity.Entity;

public class HoverEventShowEntity extends HoverEvent {
    String type;
    String name;
    int id;

    public HoverEventShowEntity(Entity entity) {
        setEntity(entity);
    }

    public HoverEventShowEntity(String type, String name, int id) {
        this.type = type;
        this.name = name;
        this.id = id;
    }

    public void setEntity(Entity entity) {
        type = entity.getType();
        name = entity.getName();
        id = entity.getEntityId();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    protected String getAction() {
        return "show_entity";
    }

    @Override
    public Object getValue() {
        return Granite.getAPIHelper().getEntityNBTCompoundString(type, name, id+"");
    }
}

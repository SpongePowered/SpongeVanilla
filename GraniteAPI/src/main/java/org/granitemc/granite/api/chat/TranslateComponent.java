package org.granitemc.granite.api.chat;

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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslateComponent extends ChatComponent {
    String key;
    List<ChatComponent> values;

    public TranslateComponent(String key, ChatComponent... values) {
        this.key = key;
        this.values = Arrays.asList(values);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<ChatComponent> getValues() {
        return values;
    }

    public void setValues(List<ChatComponent> values) {
        this.values = values;
    }

    @Override
    public String getValue() {
        /*String args = "";

        for (ChatComponent with : values) {
            args += with.getValue() + ",";
        }
        return key + "(" + args.substring(0, args.length() - 1) + ")";*/
        return Granite.getAPIHelper().getTranslation(key, (Object[]) ChatComponent.unwrapStrings(values.toArray(new ChatComponent[values.size()])));
    }

    @Override
    public JSONObject toConfigObject() {
        JSONObject obj = super.toConfigObject();

        obj.put("translate", key);

        List<Object> withs = new ArrayList<>();
        for (ChatComponent with : values) {
            withs.add(with.toConfigObject());
        }

        obj.put("with", withs);
        return obj;
    }

    static TranslateComponent fromConfigObjectLocal(JSONObject obj) {
        List<ChatComponent> withs = new ArrayList<>();

        if (obj.containsKey("with")) {
            for (Object val : (JSONArray) obj.get("with")) {
                withs.add(ChatComponent.fromConfigObject((JSONObject) val));
            }
        }
        return new TranslateComponent((String) obj.get("translate"), withs.toArray(new ChatComponent[]{}));
    }
}

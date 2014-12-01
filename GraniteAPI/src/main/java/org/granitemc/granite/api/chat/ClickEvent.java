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

import org.granitemc.granite.api.chat.click.ClickEventChangePage;
import org.granitemc.granite.api.chat.click.ClickEventOpenURL;
import org.granitemc.granite.api.chat.click.ClickEventRunCommand;
import org.granitemc.granite.api.chat.click.ClickEventSuggestCommand;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public abstract class ClickEvent {
    protected abstract String getAction();

    public abstract Object getValue();

    JSONObject toConfigObject() {
        JSONObject obj = new JSONObject();
        obj.put("action", getAction());
        obj.put("value", getValue());

        return obj;
    }

    static ClickEvent fromConfigObject(JSONObject obj) {
        String action = (String) obj.get("action");

        if (Objects.equals(action, "change_page")) {
            return new ClickEventChangePage(Integer.valueOf(obj.get("value") + ""));
        } else if (Objects.equals(action, "open_url")) {
            try {
                return new ClickEventOpenURL(new URL((String) obj.get("value")));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (Objects.equals(action, "run_command")) {
            return new ClickEventRunCommand((String) obj.get("value"));
        } else if (Objects.equals(action, "suggest_command")) {
            return new ClickEventSuggestCommand((String) obj.get("value"));
        }
        return null;
    }
}

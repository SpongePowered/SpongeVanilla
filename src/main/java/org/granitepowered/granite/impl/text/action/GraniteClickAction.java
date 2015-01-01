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

package org.granitepowered.granite.impl.text.action;

import org.spongepowered.api.text.action.ClickAction;

import java.net.URL;

public abstract class GraniteClickAction<R> extends GraniteTextAction<R> implements ClickAction<R> {
    public static class GraniteOpenUrl extends GraniteClickAction<URL> implements OpenUrl {
        URL url;

        public GraniteOpenUrl(URL url) {
            this.url = url;
        }

        @Override
        public String getId() {
            return "open_url";
        }

        @Override
        public URL getResult() {
            return url;
        }
    }

    public static class GraniteRunCommand extends GraniteClickAction<String> implements RunCommand {
        String command;

        public GraniteRunCommand(String command) {
            this.command = command;
        }

        @Override
        public String getId() {
            return "run_command";
        }

        @Override
        public String getResult() {
            return command;
        }
    }

    public static class GraniteChangePage extends GraniteClickAction<Integer> implements ChangePage {
        int page;

        public GraniteChangePage(int page) {
            this.page = page;
        }

        @Override
        public String getId() {
            return "change_page";
        }

        @Override
        public Integer getResult() {
            return page;
        }
    }

    public static class GraniteSuggestCommand extends GraniteClickAction<String> implements SuggestCommand {
        String command;

        public GraniteSuggestCommand(String command) {
            this.command = command;
        }

        @Override
        public String getId() {
            return "suggest_command";
        }

        @Override
        public String getResult() {
            return command;
        }
    }
}

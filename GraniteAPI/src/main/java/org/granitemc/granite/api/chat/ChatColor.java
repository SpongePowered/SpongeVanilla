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

import org.fusesource.jansi.Ansi;

public enum ChatColor {
    BLACK('0', "black", false, Ansi.Color.BLACK),
    DARK_BLUE('1', "dark_blue", false, Ansi.Color.BLUE),
    DARK_GREEN('2', "dark_green", false, Ansi.Color.GREEN),
    DARK_AQUA('3', "dark_aqua", false, Ansi.Color.CYAN),
    DARK_RED('4', "dark_red", false, Ansi.Color.RED),
    DARK_PURPLE('5', "dark_purple", false, Ansi.Color.MAGENTA),
    GOLD('6', "gold", false, Ansi.Color.YELLOW),
    GRAY('7', "gray", false, Ansi.Color.WHITE),
    DARK_GRAY('8', "dark_gray", true, Ansi.Color.BLACK),
    BLUE('9', "blue", true, Ansi.Color.BLUE),
    GREEN('a', "green", true, Ansi.Color.GREEN),
    AQUA('b', "aqua", true, Ansi.Color.CYAN),
    RED('c', "red", true, Ansi.Color.RED),
    LIGHT_PURPLE('d', "light_purple", true, Ansi.Color.MAGENTA),
    YELLOW('e', "yellow", true, Ansi.Color.YELLOW),
    WHITE('f', "white", true, Ansi.Color.WHITE);
    //RESET('r', "reset");
    private final String name;
    private final char id;
    private final boolean bright;
    private final Ansi.Color ansiColor;

    ChatColor(char id, String name, boolean bright, Ansi.Color ansi) {
        this.id = id;
        this.name = name;
        this.bright = bright;
        this.ansiColor = ansi;
    }

    public String getName() {
        return name;
    }

    public char getId() {
        return id;
    }

    public boolean isBright() {
        return bright;
    }

    public Ansi.Color getAnsiColor() {
        return ansiColor;
    }
}

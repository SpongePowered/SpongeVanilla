package org.granitemc.granite.utils;

/**
 * License (MIT)
 *
 * Copyright (c) 2014. avarisc
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

public class ChatFormatCodes {

    //Colour Codes
    public static String black = "§0";
    public static String blue = "§1";
    public static String green = "§2";
    public static String aqua = "§3";
    public static String red = "§4";
    public static String purple = "§5";
    public static String gold = "§6";
    public static String gray = "§7";
    public static String darkgray = "§8";
    public static String lightblue = "§9";
    public static String lightgreen = "§a";
    public static String cyan = "§b";
    public static String lightred = "§c";
    public static String magenta = "§d";
    public static String yellow = "§e";
    public static String white = "§f";

    //Format Codes
    public static String bold = "§l";
    public static String underline = "§n";
    public static String italic = "§o";
    public static String magic = "§k";
    public static String strikethrough = "§m";
    public static String reset = "§r";
    public static String newline = "\n";

    //Unicode Symbols
    public static String hearts = "\u2665";
    public static String diamonds = "\u2666";
    public static String clubs = "\u2663";
    public static String spades = "\u2660";
    public static String fullblock = "\u2588";
    public static String square = "\u25A0";
    public static String smallsquare = "\u25AA";
    public static String bullet = "\u25CF";
    public static String arrowright = "\u25BA";

    public static String preFormatInfo() {
        return lightgreen + bold + smallsquare + " " + reset + gold;
    }

    public static String preFormatError() {
        return red + bold + smallsquare + " " + reset + lightred;
    }
}

package org.granitemc.granite.utils;

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

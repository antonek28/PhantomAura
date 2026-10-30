package com.phantomaura;

public final class AuraSettings {

    private AuraSettings() {}

    public static boolean autoTarget = true;
    public static boolean targetESP = true;

    public static boolean hostileMobs = true;
    public static boolean passiveMobs = false;
    public static boolean players = false;

    public static double range = 6.0;
    public static double aimSpeed = 5.0;

    public static void reset() {
        autoTarget = true;
        targetESP = true;
        hostileMobs = true;
        passiveMobs = false;
        players = false;
        range = 6.0;
        aimSpeed = 5.0;
    }
}

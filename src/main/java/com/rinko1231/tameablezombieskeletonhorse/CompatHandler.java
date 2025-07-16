package com.rinko1231.tameablezombieskeletonhorse;

public class CompatHandler {
    private static CompatHandler instance;
    public final boolean iafLoaded;
    public static CompatHandler getInstance() {
        if (instance == null) instance = new CompatHandler();
        return instance;
    }
    public CompatHandler() {
        iafLoaded = getViaCfgAndClass();
    }
    private boolean getViaCfgAndClass() {
        try {
            Class.forName("com.github.alexthe666.iceandfire.IceAndFire", false, this.getClass().getClassLoader());
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }
}
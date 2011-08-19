package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

public class Op implements Entry {

    private final boolean positive;

    public Op(boolean positive) {
        this.positive = positive;
    }

    @Override
    public boolean isPositive() {
        return this.positive;
    }

    @Override
    public boolean match(Player player) {
        return player.isOp();
    }

}

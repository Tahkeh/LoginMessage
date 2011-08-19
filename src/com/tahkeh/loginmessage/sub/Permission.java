package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.perm.PermissionsChecker;

public class Permission extends DefaultEntry {

    private final PermissionsChecker checker;

    public Permission(String permission, PermissionsChecker checker) {
        super(permission);
        this.checker = checker;
    }

    @Override
    public boolean match(Player player) {
        return this.checker.has(player, this.value);
    }

}

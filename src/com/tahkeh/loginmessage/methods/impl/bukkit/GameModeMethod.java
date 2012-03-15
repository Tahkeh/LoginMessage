package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.bukkit.CasePlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class GameModeMethod extends CasePlayerMethod {

	private final Message message;

	public GameModeMethod(final Message message) {
		super(0, "mode");
		this.message = message;
	}

	@Override
	protected String call(Player player, final Parameter[] preValues, PlayerVariables globalParameters) {
		return this.message.getGameModeText(player.getGameMode());
	}

}

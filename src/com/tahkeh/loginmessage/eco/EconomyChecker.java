package com.tahkeh.loginmessage.eco;

import org.bukkit.entity.Player;

import com.iConomy.iConomy;

import cosine.boseconomy.BOSEconomy;

public abstract class EconomyChecker {
	
	public static class iConomyChecker extends EconomyChecker {
		
		@Override
		public String getBalance(Player p) {
			return iConomy.getAccount(p.getName()).getHoldings().toString();
		}
	}
	
	public static class BOSEconomyChecker extends EconomyChecker {
		
		private final BOSEconomy bose;
		
		public BOSEconomyChecker(BOSEconomy bose) {
			this.bose = bose;
		}
		
		@Override
		public String getBalance(Player p) {
			return Double.toString(bose.getBankMoneyDouble(p.getName()));
		}
		
	}

	public abstract String getBalance(Player p);
}

package de.koppy.shopkeepers;

import org.bukkit.Material;

public class PriceList {

	public static Double getPriceOF(Material m) {
		if(m.toString().toLowerCase().contains("boat")) {
			//Preis Boot
			return 0.0;
		}else if(m.toString().toLowerCase().contains("planks")) {
			//Preis Planks
			return 0.0;
		}else if(m.toString().toLowerCase().contains("wood") && m.isBlock()) {
			//Preis Wood
			return 0.0;
		}else if(m.toString().toLowerCase().contains("log") && m.isBlock()) {
			//Preis Logs
			return 0.0;
		}else if(m.toString().toLowerCase().contains("log") && m.isBlock()) {
			//Preis Logs
			return 0.0;
		}else if(m.toString().toLowerCase().contains("leaves") && m.isBlock()) {
			//Preis Logs
			return 0.0;
		}else {
			return 0.0;
		}
	}
	
	public static Double getPriceSellOF(Material m) {
		Double buy = getPriceOF(m);
		return buy * 90/100;
	}
	
}

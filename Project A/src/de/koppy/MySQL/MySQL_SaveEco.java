package de.koppy.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class MySQL_SaveEco {

//	try {
//		PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS LobbyPoints (UUID VARCHAR(100),Spielername VARCHAR(100),Points INT(100))");
//		ps.executeUpdate();
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
	
	public static boolean isUserExist(UUID uuid) {
		
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Amount FROM Economy WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static boolean isUserExist(String uuid) {
		
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Amount FROM Economy WHERE UUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static boolean hasEnoughPoints(UUID uuid, double amount) {
		double points = getAmount(uuid);
		if(points >= amount) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public static boolean hasEnoughPoints(String uuid, double amount) {
		double points = getPoints(uuid);
		if(points >= amount) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public static void removePoints(UUID uuid, String spielername, double amount) {
		if(isUserExist(uuid)) {
		double points = getAmount(uuid);
		if(hasEnoughPoints(uuid, amount)) {
			
			amount = points-amount;
			
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Economy SET Amount = ? WHERE UUID = ?");
				ps.setDouble(1, amount);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	}
	
	public static void setAmount(UUID uuid, double amount) {
		if(isUserExist(uuid)) {
			
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Economy SET Amount = ? WHERE UUID = ?");
					ps.setDouble(1, amount);
					ps.setString(2, uuid.toString());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
		}else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Economy (UUID,Amount) VALUES(?,?)");
				ps.setString(1, uuid.toString());
				ps.setDouble(2, amount);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addPoints(UUID uuid, double amount) {
		if(isUserExist(uuid)) {
			double points = getAmount(uuid);
				
				amount = points+amount;
				
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Economy SET Amount = ? WHERE UUID = ?");
					ps.setDouble(1, amount);
					ps.setString(2, uuid.toString());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
		}else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Economy (UUID,Amount) VALUES(?,?)");
				ps.setString(1, uuid.toString());
				ps.setDouble(2, amount);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void removePoints(String uuid, String spielername, double amount) {
		if(isUserExist(uuid)) {
		double points = getPoints(uuid);
		if(hasEnoughPoints(uuid, amount)) {
			
			amount = points-amount;
			
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Economy SET Amount = ? WHERE UUID = ?");
				ps.setDouble(1, amount);
				ps.setString(2, uuid);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	}
	
	public static void setAmount(String uuid, double amount) {
		if(isUserExist(uuid)) {
			
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Economy SET Amount = ? WHERE UUID = ?");
					ps.setDouble(1, amount);
					ps.setString(2, uuid);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
		}else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Economy (UUID,Amount) VALUES(?,?)");
				ps.setString(1, uuid);
				ps.setDouble(2, amount);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addPoints(String uuid, String spielername, double amount) {
		if(isUserExist(uuid)) {
			double points = getPoints(uuid);
				
				amount = points+amount;
				
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Economy SET Amount = ? WHERE UUID = ?");
					ps.setDouble(1, amount);
					ps.setString(2, uuid);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
		}else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Economy (UUID,Amount) VALUES(?,?)");
				ps.setString(1, uuid);
				ps.setDouble(2, amount);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void delete(UUID uuid) {
		if(isUserExist(uuid)) {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE * FROM Economy WHERE UUID = ?");
				ps.setString(1, uuid.toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("[MySQL] Der Spieler mit der UUID: " + uuid.toString() + " existiert nicht in der Datenbank.");
		}
	}
	
	public static Double getAmount(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Amount FROM Economy WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getDouble("Amount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (double) -1;
	}
	
	public static Double getPoints(String uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Amount FROM Economy WHERE UUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getDouble("Amount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (double) -1;
	}
	
}

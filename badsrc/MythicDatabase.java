import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MythicDatabase {
	private boolean available = false;
	private Connection c = null;
	public MythicDatabase(){
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:mythic.db");
	      c.setAutoCommit(false);
	    } catch ( Exception e ) {
	      MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
	      return;
	    }
	    MythicLogger.info("MythicDB ready.");
	    available = true;
	}
	
	public boolean isAvailable(){
		return available;
	}
	
	public void install(String modPrefix, boolean usesGlobalString, boolean usesPlayerString, boolean usesGlobalFloat, boolean usesPlayerFloat, boolean usesGlobalInt, boolean usesPlayerInt, boolean usesGlobalBool, boolean usesPlayerBool){
		if(!available){
			MythicLogger.error("Database not available. Could not install mod.");
			return;
		}
		Statement stmt = null;
		try {
			if(usesGlobalString){
				stmt = c.createStatement();
				String sql = "CREATE TABLE IF NOT EXISTS " + modPrefix + "_global_strings (ID CHAR(255) PRIMARY KEY NOT NULL, ELEMENT TEXT NOT NULL)";
				stmt.executeUpdate(sql);
				stmt.close();
			}
			if(usesPlayerString){
				stmt = c.createStatement();
				String sql = "CREATE TABLE IF NOT EXISTS " + modPrefix + "_player_strings (ID CHAR(255) PRIMARY KEY NOT NULL, ELEMENT TEXT NOT NULL)";
				stmt.executeUpdate(sql);
				stmt.close();
			}
			if(usesGlobalFloat){
				stmt = c.createStatement();
				String sql = "CREATE TABLE IF NOT EXISTS " + modPrefix + "_global_floats (ID CHAR(255) PRIMARY KEY NOT NULL, ELEMENT FLOAT NOT NULL)";
				stmt.executeUpdate(sql);
				stmt.close();
			}
			if(usesPlayerFloat){
				stmt = c.createStatement();
				String sql = "CREATE TABLE IF NOT EXISTS " + modPrefix + "_player_floats (ID CHAR(255) PRIMARY KEY NOT NULL, ELEMENT FLOAT NOT NULL)";
				stmt.executeUpdate(sql);
				stmt.close();
			}
			if(usesGlobalInt){
				stmt = c.createStatement();
				String sql = "CREATE TABLE IF NOT EXISTS " + modPrefix + "_global_ints (ID CHAR(255) PRIMARY KEY NOT NULL, ELEMENT INT NOT NULL)";
				stmt.executeUpdate(sql);
				stmt.close();
			}
			if(usesPlayerInt){
				stmt = c.createStatement();
				String sql = "CREATE TABLE IF NOT EXISTS " + modPrefix + "_player_ints (ID CHAR(255) PRIMARY KEY NOT NULL, ELEMENT INT NOT NULL)";
				stmt.executeUpdate(sql);
				stmt.close();
			}
		} catch ( Exception e ) {
			MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public String getGlobalString(String modPrefix, String dataName, String defaultValue){
		if(!available){
			MythicLogger.error("Database not available. Returning default for query.");
			return defaultValue;
		}
		Statement stmt = null;
		try {
		  String element = defaultValue;
		  stmt = c.createStatement();
		  String sql = "SELECT * FROM " + modPrefix + "_global_strings WHERE ID = \"" + dataName + "\";";
		  ResultSet rs = stmt.executeQuery( sql );
	      if(rs.next()){
	         element = rs.getString("ELEMENT");
	      }
	      rs.close();
	      stmt.close();
		  return element;
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		  return defaultValue;
		}
	}
	
	public void setGlobalString(String modPrefix, String dataName, String value){
		if(!available){
			MythicLogger.error("Database not available. Could not store string.");
			return;
		}
		Statement stmt = null;
		try {
		  stmt = c.createStatement();
		  String sql = "INSERT OR REPLACE INTO " + modPrefix + "_global_strings (ID, ELEMENT) values (\"" + dataName + "\", \"" + value + "\")";
		  stmt.executeUpdate(sql);
		  c.commit();
		  stmt.close();
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public String getPlayerString(String modPrefix, String dataName, String playerUUID, String defaultValue){
		if(!available){
			MythicLogger.error("Database not available. Returning default for query.");
			return defaultValue;
		}
		Statement stmt = null;
		try {
		  String element = defaultValue;
		  stmt = c.createStatement();
		  String sql = "SELECT * FROM " + modPrefix + "_player_strings WHERE ID = \"" + dataName + "." + playerUUID + "\";";
		  ResultSet rs = stmt.executeQuery( sql );
	      if(rs.next()){
	         element = rs.getString("ELEMENT");
	      }
	      rs.close();
	      stmt.close();
		  return element;
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		  return defaultValue;
		}
	}
	
	public void setPlayerString(String modPrefix, String dataName, String playerUUID, String value){
		if(!available){
			MythicLogger.error("Database not available. Could not store string.");
			return;
		}
		Statement stmt = null;
		try {
		  stmt = c.createStatement();
		  String sql = "INSERT OR REPLACE INTO " + modPrefix + "_player_strings (ID, ELEMENT) values (\"" + dataName + "." + playerUUID + "\", \"" + value + "\")";
		  stmt.executeUpdate(sql);
		  c.commit();
		  stmt.close();
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public float getGlobalFloat(String modPrefix, String dataName, float defaultValue){
		if(!available){
			MythicLogger.error("Database not available. Returning default for query.");
			return defaultValue;
		}
		Statement stmt = null;
		try {
		  float element = defaultValue;
		  stmt = c.createStatement();
		  String sql = "SELECT * FROM " + modPrefix + "_global_strings WHERE ID = \"" + dataName + "\";";
		  ResultSet rs = stmt.executeQuery( sql );
	      if(rs.next()){
	         element = rs.getFloat("ELEMENT");
	      }
	      rs.close();
	      stmt.close();
		  return element;
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		  return defaultValue;
		}
	}
	
	public void setGlobalFloat(String modPrefix, String dataName, float value){
		if(!available){
			MythicLogger.error("Database not available. Could not store string.");
			return;
		}
		Statement stmt = null;
		try {
		  stmt = c.createStatement();
		  String sql = "INSERT OR REPLACE INTO " + modPrefix + "_global_strings (ID, ELEMENT) values (\"" + dataName + "\", \"" + value + "\")";
		  stmt.executeUpdate(sql);
		  c.commit();
		  stmt.close();
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public float getPlayerFloat(String modPrefix, String dataName, String playerUUID, float defaultValue){
		if(!available){
			MythicLogger.error("Database not available. Returning default for query.");
			return defaultValue;
		}
		Statement stmt = null;
		try {
		  float element = defaultValue;
		  stmt = c.createStatement();
		  String sql = "SELECT * FROM " + modPrefix + "_player_strings WHERE ID = \"" + dataName + "." + playerUUID + "\";";
		  ResultSet rs = stmt.executeQuery( sql );
	      if(rs.next()){
	         element = rs.getFloat("ELEMENT");
	      }
	      rs.close();
	      stmt.close();
		  return element;
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		  return defaultValue;
		}
	}
	
	public void setPlayerFloat(String modPrefix, String dataName, String playerUUID, float value){
		if(!available){
			MythicLogger.error("Database not available. Could not store string.");
			return;
		}
		Statement stmt = null;
		try {
		  stmt = c.createStatement();
		  String sql = "INSERT OR REPLACE INTO " + modPrefix + "_player_strings (ID, ELEMENT) values (\"" + dataName + "." + playerUUID + "\", \"" + value + "\")";
		  stmt.executeUpdate(sql);
		  c.commit();
		  stmt.close();
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public int getGlobalInt(String modPrefix, String dataName, int defaultValue){
		if(!available){
			MythicLogger.error("Database not available. Returning default for query.");
			return defaultValue;
		}
		Statement stmt = null;
		try {
		  int element = defaultValue;
		  stmt = c.createStatement();
		  String sql = "SELECT * FROM " + modPrefix + "_global_strings WHERE ID = \"" + dataName + "\";";
		  ResultSet rs = stmt.executeQuery( sql );
	      if(rs.next()){
	         element = rs.getInt("ELEMENT");
	      }
	      rs.close();
	      stmt.close();
		  return element;
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		  return defaultValue;
		}
	}
	
	public void setGlobalInt(String modPrefix, String dataName, int value){
		if(!available){
			MythicLogger.error("Database not available. Could not store string.");
			return;
		}
		Statement stmt = null;
		try {
		  stmt = c.createStatement();
		  String sql = "INSERT OR REPLACE INTO " + modPrefix + "_global_strings (ID, ELEMENT) values (\"" + dataName + "\", \"" + value + "\")";
		  stmt.executeUpdate(sql);
		  c.commit();
		  stmt.close();
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public int getPlayerInt(String modPrefix, String dataName, String playerUUID, int defaultValue){
		if(!available){
			MythicLogger.error("Database not available. Returning default for query.");
			return defaultValue;
		}
		Statement stmt = null;
		try {
		  int element = defaultValue;
		  stmt = c.createStatement();
		  String sql = "SELECT * FROM " + modPrefix + "_player_strings WHERE ID = \"" + dataName + "." + playerUUID + "\";";
		  ResultSet rs = stmt.executeQuery( sql );
	      if(rs.next()){
	         element = rs.getInt("ELEMENT");
	      }
	      rs.close();
	      stmt.close();
		  return element;
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		  return defaultValue;
		}
	}
	
	public void setPlayerInt(String modPrefix, String dataName, String playerUUID, int value){
		if(!available){
			MythicLogger.error("Database not available. Could not store string.");
			return;
		}
		Statement stmt = null;
		try {
		  stmt = c.createStatement();
		  String sql = "INSERT OR REPLACE INTO " + modPrefix + "_player_strings (ID, ELEMENT) values (\"" + dataName + "." + playerUUID + "\", \"" + value + "\")";
		  stmt.executeUpdate(sql);
		  c.commit();
		  stmt.close();
		} catch ( Exception e ) {
		  MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public void close(){
		try {
			c.close();
		} catch (SQLException e) {
		    MythicLogger.error( e.getClass().getName() + ": " + e.getMessage() );
		}
	}

}

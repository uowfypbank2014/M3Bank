package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

/**
 * This class manages database connection and provides SQL Statements and PreparedStatements to other database layer classes.
 */
public class DBManager {
	
	/**
	 * create a single connection and always use this one.
	 */
	private static Connection connection;
	
	public static Connection getConnection() throws Exception {
		if (connection == null) {
			Properties properties = new Properties();
			properties.load(DBManager.class.getClassLoader().getResourceAsStream("database/config.properties"));
			
			Class.forName(properties.getProperty("database.driver"));
			connection = DriverManager.getConnection(properties.getProperty("databse.connectionurl"), properties.getProperty("database.user"), properties.getProperty("database.password"));
		}
		return connection;
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static PreparedStatement getPreparedStatement(String sql) throws Exception {
		return DBManager.getConnection().prepareStatement(sql);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Statement getStatement() throws Exception {
		return DBManager.getConnection().createStatement();
	}
}

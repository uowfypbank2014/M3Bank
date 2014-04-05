package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import rsa.RSAUtil;

import login.OTP;
import login.User;

/**
 * The class deals with database calls relating to sso table.
 */
public class SSO {
	
	private static final String TABLE_NAME_SSO = "sso";
	private static final Logger logger = Logger.getLogger(SSO.class.getSimpleName());
	
	/**
	 * Validates the userId and the pin. Returns User pojo if a user with specified userId and matching pin is found.
	 * @param userId
	 * @param pin
	 * @return
	 * @throws Exception
	 */
	public static User validateCredentials(String userId, String pin) throws Exception {
		logger.info("Validating user credentials - " + userId);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("SELECT * FROM " + TABLE_NAME_SSO + " WHERE userID=?");
		preparedStatement.setString(1, userId);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			String encryptedPin = resultSet.getString("pin");
			if (!RSAUtil.decryptFromHexString(encryptedPin).equals(pin)) {
				return null;
			}
			User user = new User();
			user.setUserId(resultSet.getString("userID"));
			user.setLoginAttempts(resultSet.getInt("loginAttempts"));
			user.setAccountLocked(resultSet.getBoolean("accountLocked"));
			user.setLastLogin(resultSet.getTimestamp("lastLogin"));
			user.setMobileNumber(resultSet.getString("mobileNumber"));
			logger.info("Credentials matched - " + userId);
			return user;
		}
		logger.info("Incorrect credentials - " + userId);
		return null;
	}
	
	/**
	 * Increments the loginAttempts by 1 for user identified by useerId. Returns the new value.
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public static int incrementLoginAttempts(String userId) throws Exception {
		logger.info("Incrementing login attempts for user - " + userId);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("UPDATE " + TABLE_NAME_SSO + " SET loginAttempts = loginAttempts + 1 WHERE userID=?");
		preparedStatement.setString(1, userId);
		preparedStatement.executeUpdate();
		
		preparedStatement = DBManager.getPreparedStatement("SELECT loginAttempts FROM " + TABLE_NAME_SSO + " WHERE userID=?");
		preparedStatement.setString(1, userId);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getInt(1);
		}
		return 0;
	}
	
	/**
	 * Checks whether a user with given userID exists.
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static boolean userExists(String userId) throws Exception {
		logger.info("Checking user exists - " + userId);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("SELECT COUNT(*) FROM " + TABLE_NAME_SSO + " WHERE userID=?");
		preparedStatement.setString(1, userId);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			logger.debug("User exists - " + userId);
			return resultSet.getInt(1) > 0;//return true count > 0
		}
		logger.debug("User does not exists - " + userId);
		return false;
	}
	
	/**
	 * Update the lastLogin time for user, also resets the loginAttemps count to 0.
	 * @param userId
	 * @throws Exception 
	 */
	public static void updateLastLogin(String userId) throws Exception {
		logger.info("Updating loasLogin time and clearing loginAttempts for user - " + userId);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("UPDATE " + TABLE_NAME_SSO + " SET loginAttempts = 0, lastLogin=?, otpExpiry=? WHERE userID=?"); 
		preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
		preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));//Also expire the OTP
		preparedStatement.setString(3, userId);
		int updateCount = preparedStatement.executeUpdate();
		if (updateCount != 1) {
			logger.error("Update lastLogin failed for user - " + userId);
			throw new Exception("Failed to update lastLogin and loginAttempts.");
		}
	}
	
	/**
	 * Validates OTP for user and returns the User pojo if OTP is valid.
	 * @param userId
	 * @param otp
	 * @return
	 * @throws Exception
	 */
	public static User validateOTP(String userId, String otp) throws Exception {
		//1 DB call is saved here - Single call will serve purpose of validation and fetch details.
		
		logger.info("Validating OTP along with OTPexpiry for user - " + userId);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("SELECT * FROM " + TABLE_NAME_SSO + " WHERE userID=? AND otp=? AND otpExpiry > ? ");
		preparedStatement.setString(1, userId);
		preparedStatement.setString(2, otp);
		preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			User user = new User();
			user.setUserId(resultSet.getString("userID"));
			user.setLoginAttempts(resultSet.getInt("loginAttempts"));
			user.setAccountLocked(resultSet.getBoolean("accountLocked"));
			user.setLastLogin(resultSet.getTimestamp("lastLogin"));
			user.setMobileNumber(resultSet.getString("mobileNumber"));
			logger.info("OTP validation successful - " + userId);
			return user;
		}
		logger.info("Invalid OTP - " + userId);
		return null;
	}
	
	/**
	 * Locks/Unlocks user account.
	 * @param username
	 * @param lock
	 * @throws Exception
	 */
	public static void lockUserAccount(String username, boolean lock) throws Exception {
		logger.info("Lock/Unlock user - " + username + " Lock:" + lock);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("UPDATE " + TABLE_NAME_SSO + " SET accountLocked=? WHERE userID=?");
		preparedStatement.setString(1, lock ? "true" : "false");
		preparedStatement.setString(2, username);
		int updateCount = preparedStatement.executeUpdate();
		if (updateCount != 1) {
			throw new Exception("Failed to lock user account.");
		}
	}
	
	/**
	 * 
	 * @param username
	 * @throws Exception 
	 */
	public static boolean isAccountLocked(String username) throws Exception {
		logger.info("Checking account locked state for user - " + username);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("SELECT accountLocked FROM " + TABLE_NAME_SSO + " WHERE userID=?");
		preparedStatement.setString(1, username);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			logger.info("Account is in locked state user - " + username);
			return "true".equals(resultSet.getString(1));
		}
		logger.info("Account is in unlocked state user - " + username);
		return false;
	}
	
	/**
	 * Updates the otp for the user in database.
	 * @param username
	 * @param otp
	 * @throws Exception
	 */
	public static void updateOTP(String username, String otp) throws Exception {
		logger.info("Updating OTP for user - " + username);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("UPDATE " + TABLE_NAME_SSO + " SET otp=?, otpExpiry = ? WHERE userID=?");
		preparedStatement.setString(1, otp);
		preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis() + OTP.OTP_EXPIRY_MILLIS));
		preparedStatement.setString(3, username);
		int updateCount = preparedStatement.executeUpdate();
		if (updateCount != 1) {
			logger.error("Update OTP for user failed.");
			throw new Exception("Failed to update otp for user:" + username);
		}
	}
	
	/**
	 * Fetches a User entry by username.
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public static User fetchEntry(String username) throws Exception {
		logger.info("Fetching user entry - " + username);
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("SELECT * FROM " + TABLE_NAME_SSO + " WHERE userID=?");
		preparedStatement.setString(1, username);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			User user = new User();
			user.setUserId(resultSet.getString("userID"));
			user.setLoginAttempts(resultSet.getInt("loginAttempts"));
			user.setAccountLocked(resultSet.getBoolean("accountLocked"));
			user.setLastLogin(resultSet.getTimestamp("lastLogin"));
			user.setMobileNumber(resultSet.getString("mobileNumber"));
			return user;
		}
		logger.info("Fetching user entry unsuccessful - " + username);
		return null;
	}
}

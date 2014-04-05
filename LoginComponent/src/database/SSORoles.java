package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * The class deals with database calls relating to ssoroles table.
 */
public class SSORoles {
	private static final String TABLE_NAME_SSOROLES = "ssoroles";
	
	/**
	 * Fetches the roles for user.
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static String getUserRoles(String userId) throws Exception {
		PreparedStatement preparedStatement = DBManager.getPreparedStatement("SELECT Roles FROM " + TABLE_NAME_SSOROLES + " WHERE UserID=?");
		preparedStatement.setString(1, userId);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getString(1);
		}
		return null;
	}
}

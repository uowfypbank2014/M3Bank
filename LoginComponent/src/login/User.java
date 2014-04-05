package login;

import java.util.Date;

/**
 * Simple POJO class to denote a User entity.
 */
public class User {
	private String userId;
	private String otp;
	private int loginAttempts;
	private boolean accountLocked;
	private Date lastLogin;
	private String mobileNumber;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public int getLoginAttempts() {
		return loginAttempts;
	}
	public void setLoginAttempts(int i) {
		this.loginAttempts = i;
	}
	public boolean getAccountLocked() {
		return accountLocked;
	}
	public void setAccountLocked(boolean b) {
		this.accountLocked = b;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
}

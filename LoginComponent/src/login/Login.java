package login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sms.SMSSender;
import database.SSO;

/**
 * Login servlet.
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final int MAX_LOGIN_ATTEMPTS = 3;
	private static final Logger logger = Logger.getLogger("login");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try {
			logger.debug("User loging in - " + username);
			if (SSO.isAccountLocked(username)) {
				logger.info("User - " + username + " cannot login, since it is locked.");
				//If User is locked show Login.jsp with an error saying 'User is Locked.'
				request.setAttribute("errorMessage", "User is Locked.");
				request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
				return;
			}
			User user = SSO.validateCredentials(username, password);
			if (user != null) {
				logger.info("User credentials validated successfully - " + username);
				//If User credentials are correct, show OTP.jsp
				sendOTP(user);
				request.setAttribute("username", user.getUserId());
				request.getRequestDispatcher("/jsp/OTP.jsp").forward(request, response);
				return;
			}
			else {
				//Incorrect credentials.
				logger.info("User credentials incorrect - " + username);
				
				//Increment loginAttempts
				int loginAttempts = SSO.incrementLoginAttempts(username);
				if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
					//If 3 incorrect attempts made, show Login.jsp with error saying - 'User Locked.'
					SSO.lockUserAccount(username, true);
					logger.info("User - " + username + " reached max loginAttempts, acount locked.");
					request.setAttribute("errorMessage", "User Locked. Maximum login attempts made.");
					request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
				}
				else {
					//else show Login.jsp with error saying - 'Invalid credentials'
					request.setAttribute("errorMessage", "Invalid credentials.");
					request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
				}
				return;
			}
		} catch (Exception e) {
			logger.error("Error while user login:" + username, e);
			request.setAttribute("errorMessage", "An unexpected erorr has occured.");
			request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
		}
	}
	
	/**
	 * Sends OTP password to the User's mobile number.
	 * @param username
	 * @throws Exception 
	 */
	public static void sendOTP(User user) throws Exception {
		logger.info("Sending OTP for user - " + user.getUserId() + " no.:" + user.getMobileNumber());
		Integer otppw = SMSSender.generateOTP();
		SSO.updateOTP(user.getUserId(), otppw.toString());
		SMSSender.sendSMS(user.getMobileNumber(), otppw);
	}
}

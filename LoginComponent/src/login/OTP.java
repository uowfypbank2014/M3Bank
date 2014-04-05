package login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import database.SSO;
import database.SSORoles;

/**
 * OTP servlet.
 */
@WebServlet("/OTP")
public class OTP extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger("otp");
	public static long OTP_EXPIRY_MILLIS = 2 * 60 * 1000;//2min
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OTP() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String otppw = request.getParameter("otppw");
		String resendOTP = request.getParameter("resendOTP");
		try {
			if ("true".equals(resendOTP)) {
				//Resend the OTP.
				User user = SSO.fetchEntry(username);
				if (user != null) {
					Login.sendOTP(user);
					request.setAttribute("errorMessage", "New OTP sent. Try again.");
				}
				else {
					request.setAttribute("errorMessage", "Incorrect user.");
				}
				request.setAttribute("username", username);
				request.getRequestDispatcher("/jsp/OTP.jsp").forward(request, response);
				return;
			}
			
			//Validate the OTP for User.
			User user = SSO.validateOTP(username, otppw);
			if(user != null) {
				//User OTP matched. Fetch the ssoroles for user.
				String roles = SSORoles.getUserRoles(user.getUserId());
				if ("user".equals(roles)) {
					//if roles = 'user' redirect to UserHome.jsp. Also insert the time stamp to "lastLogin" in DB.
					SSO.updateLastLogin(user.getUserId());
					request.getRequestDispatcher("/jsp/UserHome.jsp").forward(request, response);
					return;
				}
				else {
					//roles not matched
					request.setAttribute("errorMessage", "No matching roles associated to user.");
					request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
					return;
				}
			}
			else {
				//User OTP didn't match or expired.
				request.setAttribute("username", username);
				request.setAttribute("errorMessage", "Incorrect or expired OTP.");
				request.getRequestDispatcher("/jsp/OTP.jsp").forward(request, response);
				return;
			}
		} catch (Exception e) {
			logger.error("Error while OTP verification for user : " + username, e);
			request.setAttribute("errorMessage", "An unexpected erorr has occured.");
			request.getRequestDispatcher("/jsp/OTP.jsp").forward(request, response);
		}
	}
}

<!DOCTYPE HTML>
<%@page import="org.apache.log4j.Logger"%>
<% Logger log = Logger.getLogger("otp"); %>
<% log.debug("Showing OTP page."); %>
<html>
	<head>
		<title>OTP Authentication</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta http-equiv="refresh" content="300;url=/LoginComponent/jsp/Login.jsp" /><!-- If within 5min user doesn't submit OTP then redirect to Login page. -->
		<link href="/LoginComponent/css/style.css" rel="stylesheet" type="text/css">
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600' rel='stylesheet' type='text/css'>
	</head>
	<body>
		<div class="login_header">
			<img src="/LoginComponent/images/logo.png" alt="M3BANK" class="logo" >
		</div>
		
		<div class="login_body">
			<img src="/LoginComponent/images/background.jpg" alt="" class="bg">
			<div class="login">
				<div class="login_inner">
					Please key in the One Time Password
					<br><br>
					<% String error = (String)request.getAttribute("errorMessage"); %>
					<% if(error != null) { %>
						<div class="error" style="align:left; float:left; margin-left:20px;"><%=error%></div>
					<% } %>
					<form id="otplogin" name="otplogin" action="/LoginComponent/otp" method="post">
						<input type="password" name="otppw" class="password" placeholder="OTP">
						<% String username = (String)request.getAttribute("username"); %>
						<%if(username != null) { %>
							<input type="hidden" name="username" value="<%=username%>" />
						<% } %>
						<input type="hidden" id="resendOTP" name="resendOTP" value="false" />
						<br><br>
						<div style="align:right; float:right; margin-right:20px; margin-bottom: 5px;">
							<a class="small_link" href="#" onclick="submitOTPForm(true);">Resend OTP</a>
						</div>
						<button>Sign in</button>
					</form>
					<script type="text/javascript">
					function submitOTPForm(reqNewOTP) {
						if(reqNewOTP) {
							document.getElementById('resendOTP').value='true';
						}
						else {
							document.getElementById('resendOTP').value='false';
						}
						document.getElementById('otplogin').submit();
					}
					</script>
				</div>
			</div>
		</div>
		
		<div class="login_footer">
		</div>
	</body>
</html>
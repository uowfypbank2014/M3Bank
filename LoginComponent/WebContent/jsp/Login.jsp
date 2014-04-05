<!DOCTYPE HTML>
<%@page import="org.apache.log4j.Logger"%>
<% Logger log = Logger.getLogger("login"); %>
<% log.debug("Showing Login page."); %>
<html>
	<head>
		<title>Login</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
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
					<% String error = (String)request.getAttribute("errorMessage"); %>
					<% if(error != null) { %>
						<div class="error" style="align:left; float:left; margin-left:20px;"><%=error%></div>
					<% } %>
					<form method="post" action="/LoginComponent/login">
						<input class="username" type="text" placeholder="Username" name="username">
						<br><br>
						<input class="password" type="password" placeholder="Pin" name="password">
						<br><br>
						<button>Sign in</button>
					</form>
				</div>
			</div>
		</div>
		
		<div class="login_footer">
		</div>
	</body>
</html>
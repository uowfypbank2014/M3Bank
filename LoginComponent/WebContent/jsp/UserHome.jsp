<!DOCTYPE HTML>
<html>
	<head>
		<title>User Home</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<link href="/LoginComponent/css/style.css" rel="stylesheet" type="text/css">
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600' rel='stylesheet' type='text/css'>
	</head>
	<body>
		<div id="header">
			<div class="header">
				<!-- USER LOGIN/LOGOUT -->
				<div class="inner">
				<div class="information">
					<img src="/LoginComponent/images/divider.png" class="divider" alt="|">
					<img src="/LoginComponent/images/human_icon.png" alt="">
					Welcome, <strong>Vince</strong>
					<img src="/LoginComponent/images/divider.png" alt="|" class="divider">
					<button class="logout">Logout</button>
				</div>
				</div>
			</div>	
			
			<!-- LOGO AND NAVIGATION -->
			<div id="nav">
			<div class="inner">
				<img src="/LoginComponent/images/logo.png" alt="M3BANK">
				<ul>
					<li><a href="#">Funds<br>Transfer</a></li>
					<li>
						<a href="#">Account<br>Information</a>
						<ul>
							<li class="hidden"></li>
							<li><a href="#">Balance Enquiry</a></li>
							<li><a href="#">Transaction History</a></li>
						</ul>
					</li>
					<li>
						<a href="#">Loans</a>
						<ul>
							<li class="hidden"></li>
							<li><a href="#">Request Personal Loan</a></li>
							<li><a href="#">Request Car Loan</a></li>
							<li><a href="#">Request House Loan</a></li>
						</ul>
					</li>
					<li>
						<a href="#">Investments</a>
						<ul>
							<li class="hidden"></li>
							<li><a href="#">Purchase&nbsp;Currency</a></li>
							<li><a href="#">Sell Currency</a></li>
							<li><a href="#">Purchase Gold</a></li>
							<li><a href="#">Sell Gold</a></li>
						</ul>
					</li>
					<li>
						<a href="#">Goals<br>Forecasting</a>
						<ul>
							<li class="hidden"></li>
							<li><a href="#">Deposit Fund</a></li>
						</ul>
					</li>
				</ul>
			</div>
			</div>

			<div class="border"></div>
		</div>
		<div id="content">
			<div class="break">
				<div class="box">
					<img src="/LoginComponent/images/edit.png" style="position: absolute; top: 0.5em; right: 0.5em" alt="edit">
					
					<div style="padding: 0.5em 1em; font: 1.2em 'Open Sans Semibold'">Saving Goal</div>
					<!-- PROGRESS BARS -->
					<div id="progress" style="margin: 1em 0.5em">
						<!-- OVERALL PROGRESS -->
						<div class="outerbar">
							<div class="innerbar">
								<span>You have saved <span style="font-size: 1.2em">$25,350</span></span>
							</div>
						</div>
						
						<!-- DETAILED PROGRESS BARS -->
						<div class="detailed_progress" style="width: 40%">
							<img src="/LoginComponent/images/progress_tick.png" alt="">
							<div class="bar" style="margin: 0 !important">
								<img src="/LoginComponent/images/airplaneico.png">
								<span class="amount">$10,000</span>
								<span class="reason">(Europe Trip)</span>
							</div>
						</div>
						<div class="detailed_progress" style="width: 60%">
							<img src="/LoginComponent/images/progress_tick.png" alt="">
							<div class="bar">
								<img src="/LoginComponent/images/heartico.png">
								<span class="amount">$60,000</span>
								<span class="reason">(Wedding)</span>
							</div>
						</div>
						
						<br style="clear: both">
					</div>
				</div>
			</div>
			<div class="break">
				<!-- SUMMARY BOX -->
				<div id="overview" class="box" style="padding: 0.25em 1em">
					Account Overview <span style="color: white; font-size: 1em"></span>
				</div>
				
				<div id="account" class="box">
					<!-- FIRST ACCOUNT -->
					<div class="account">
						<div class="name">
							<span style="font: 1.2em 'Open Sans Semibold'">
								Smart Saver Account
							</span>
							<br>
							<span style="font: 0.9em 'Open Sans Semibold'; color: #222">
								062-516751-001
							</span>
						</div>
						<div class="amount">
							<span style="font: 1.2em 'Open Sans Semibold'">
								SGD $7,500.00
							</span>
							<br>
							<span style="font: 0.9em 'Open Sans Semibold'; color: #ff7e00">
								Total Available
							</span>
						</div>
					</div>
					
					<!-- SECOND ACCOUNT -->
					<div class="account">
						<div class="name">
							<span style="font: 1.2em 'Open Sans Semibold'">
								savings Account
							</span>
							<br>
							<span style="font: 0.9em 'Open Sans Semibold'; color: #222">
								012-31948-002
							</span>
						</div>
						<div class="amount">
							<span style="font: 1.2em 'Open Sans Semibold'">
								SGD $14,982.00
							</span>
							<br>
							<span style="font: 0.9em 'Open Sans Semibold'; color: #ff7e00">
								Total Available
							</span>
						</div>
					</div>
					<br style="clear:both" >
				</div>
				
				<!--<div class="footer"></div>-->
			</div>
		</div>
		<div class="footer" style="position: absolute; width: 100%; bottom: 0; height: 100px; left: 0"></div>
	</body>
</html>
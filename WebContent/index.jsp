<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style.css" />
<title>WebDemo</title>

</head>
<body>
     
	<div id="wrapper">

		<div id="searchbox">
			<div id="logo">
				<a href="index.jsp"> <img alt="WebDemo"
					src="main.jpg" style="height: 180px; width: 180px; ">
				</a>
			</div>
			<form action="search.jsp"   name="testForm"    method="post"  >
				<div id="queryinputtext">
					<table>
						<tr>
							<td><input type="text" name="querytext" id="queryinput">
							</td>
							
							<td><input type="submit" value="Search" id="submit">
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>

		<div class="push"></div>

	</div>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div id="footer">
     <p>Web实验Demo&nbsp;&nbsp;&nbsp;&nbsp;Copyright &copy; 2013 LAB508, All Rights Reserved.</p> 
</div>

</body>
</html>
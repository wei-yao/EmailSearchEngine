<%@ page  import="java.util.*"  contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
 <jsp:directive.page import="com.example.search.*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/Style.css" />
<title>WebDemo</title>
<div id="wrapper">

<%  String  originQuery=request.getParameter("querytext");
String keyword = new String(request.getParameter("querytext") .getBytes("ISO-8859-1"),"UTF-8"); %>
		<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

	



</head>

<body>

	

<div id="wrapper">
	<div id="topbox">
			<form action="result.jsp">
				<div id="logo-mini">
					<a href="index.jsp"> 
					</a>
				</div>
				<div id="queryinputtext">
					<table>
						<tr>
							<td><input type="text" name="querytext" id="queryinput" 
								value=<%=originQuery %>></td>
							<td><input type="submit" value="Search" id="submit"></td>
						</tr>
					</table>
				</div>

			</form>
		</div>

		<div id="resultbox"></div>
<%  
 System.out.println("keyword: "+keyword+"  "+request.getParameter("querytext"));
 ArrayList<SearchResult> results = SearchFiles.find(keyword,0); 
 for(SearchResult result : results) 
 { 
 %> 
 <h2><a href=<%=result.url%>><%=result.title%></a></h2> 
 <p><%=result.content%><p> 
 <p><%=result.url%> &nbsp;&nbsp;&nbsp; <%=result.score%><p> 
 <%  
 } 
 %>  
		<div class="push"></div>

	</div>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<div id="footer">
     <p>Web实验Demo&nbsp;&nbsp;&nbsp;&nbsp;Copyright &copy; 2013 LAB508, All Rights Reserved.</p> 
</div>



</body>
</html>
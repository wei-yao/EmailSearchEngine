<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
 <jsp:directive.page import="com.example.search.SearchFiles" /> 
 <jsp:directive.page import="com.example.search.SearchResult" />
 <% 
 String path = request.getContextPath(); 
 System.out.println("path:"+path);
 String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
 System.out.println("basePath:" +basePath);
 String keyword;
 String  query=request.getParameter("querytext");
 if(query!=null)
         {
	 keyword = new String(request.getParameter("querytext") .getBytes("ISO-8859-1"),"UTF-8");
         }
 else
	 keyword="";
 String  hiddenNum=request.getParameter("hiddenVar");
 int  pageNo=0;
 if(hiddenNum!=null)
  pageNo=Integer.valueOf(hiddenNum);
 System.out.println(pageNo);
 %> 

 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
 <html> 
  <head> 
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <base href="<%=basePath%>"> 
    <title>搜索 ： <%=keyword %></title> 
    
    <style> 
 #search{ 
 width:78px; 
 height:28px; 
 font:14px "宋体"
 } 
 #textArea{ 
 width:300px; 
 height:30px; 
 font:14px "宋体"
 } 
 </style> 
 
  </head> 
  
  <body> 
    <form action="search.jsp" name="searchInput"   id="searchBox"     method="post"> 
 <table border="0" height="30px" width="450px" align="center"> 
 <tr>  
 <td width ="66%"><input name="querytext" type="text" maxlength="100" id="textArea"  value=<%=keyword %>   ></td> 
 <td height="20" align="center"><input type="submit" value="搜索" id = "search"    ></td> 
  <td> <input type="hidden" id="hiddenValue"  name="hiddenVar" value="0"/></td>
 </tr> 
 </table> 
      </form>    
 <%  
 System.out.println("keyword: "+keyword);
 ArrayList<SearchResult> results = SearchFiles.find(keyword,pageNo); 
 for(SearchResult result : results) 
 { 
 %> 
 <h2><a href=<%=result.url%>><%=result.title%></a></h2> 
 <p><%=result.content%><p> 
 <p><%=result.url%> &nbsp;&nbsp;&nbsp; <%=result.score%><p> 
 <%  
 } 
 %>
 <script type="text/javascript">
 function   myNextPage(){
  if(<%=SearchResult.hasNextPage%>){
               document.getElementById("hiddenValue").value=<%=pageNo%>+10;             
           searchBox.submit();
    	//    alert("下一页");
  }
 }
 function  previousPage(){
	// document.getElementById("hiddenValue").value =0;
	if(<%=pageNo%>-10>=0){
	      document.getElementById("hiddenValue").value=<%=pageNo%>-10;
	    searchBox.submit();
	      // alert("前一页");
 }
 }
 </script>  
 <table >
<tr>
<td><button type="button" id="prePage"  onclick="previousPage()" >Previous</button></td>
<td><button type="button"  id="nextPage"  onclick="myNextPage()"> &nbsp;&nbsp;&nbsp;Next&nbsp;&nbsp;&nbsp;  </button></td>
</tr>
</table>
 </body> 
 </html>

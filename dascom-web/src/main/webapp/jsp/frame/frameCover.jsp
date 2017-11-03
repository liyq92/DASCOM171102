<%@ page contentType="text/html; charset=UTF-8" %>
<%-- <%@ page import="com.dascom.init.ConfigMgr"%> --%>
<html>
<head>
<%-- <title><%=ConfigMgr.getSrvicename() %>管理系统</title> --%>
<title>管理系统</title>
<script type="text/javascript" src="js/jquery.js"></script>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<script>

function cover()
{
	document.getElementById('popId').style.display='block';
}

function uncover()
{
	document.getElementById('popId').style.display='none';
}
	
</script>

<style>
html,body{ height:100%; margin:0; padding:0} 
.popId{height:100%; width:100%; position:fixed; _position:absolute; top:0; z-index:1000; } 
.opacity{ opacity:0.3; filter: alpha(opacity=30); background-color:#000; } 
</style>

</head>
  
 <body style="margin:0;padding:0;overflow-x:hidden;overflow-y:hidden;" >
 
  <div class='popId' id="popId" style="display:none;"></div>
<!--   <iframe src="frame.do"  width=100% height=100% marginwidth=0 id="mainFrame" name="mainFrame"></iframe>   -->
   <%@ include file="/jsp/frame/frame.jsp" %>
  </body>
</html>

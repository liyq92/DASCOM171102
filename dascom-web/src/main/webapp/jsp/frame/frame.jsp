<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%-- <%@ page import="com.dascom.init.ConfigMgr"%> --%>
<html>
<head>
<%-- <title><%=ConfigMgr.getSrvicename() %>管理系统</title> --%>
<title>管理系统</title>
<script  type="text/JavaScript">

function createXMLHttpRequest(){
 	if(window.XMLHttpRequest){
		XMLHttpReq = new XMLHttpRequest();
	}
	else if(window.ActiveXObject){
		try{
			XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e){
			try{
				XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e){}
		}
	}
 }
 
 function sendRequest(){
 	createXMLHttpRequest();
 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl = "type=5";

	XMLHttpReq.send(vurl);
}

function processResponse(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessage();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }
 
function parseMessage()
{
	var ssmnall=XMLHttpReq.responseText;
	if(ssmnall == '')//失败
	{
	}
	else//成功
	{
	}
}

</script>
</head>

<!-- <iframe name="mainFrame" id="main" frameborder="0" src="frameTop.do" style="margin:0 auto;"> -->
<!-- 	<iframe name="rightFrame" id="rightFrame" frameborder="no" src="frameMain.do" style="margin:0 auto;" ></iframe> -->
<!-- 	<iframe name="leftFrame" id="leftFrame" frameborder="no" src="leftMenu.do" style="margin:0 auto;"></iframe> -->
<!-- </iframe> -->

<frameset rows="88,*" cols="*" frameborder="no" border="0" framespacing="0"  id="main">
  <frame src="frameTop.do" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" title="topFrame" />
  <frameset rows="*" cols="187,*" frameborder="no" border="0" framespacing="0">
    <frame src="leftMenu.do" name="leftFrame" scrolling="no" noresize="noresize" id="leftFrame" title="leftFrame" />
    <frame src="frameMain.do" name="rightFrame" id="rightFrame" title="rightFrame" />
  </frameset>
</frameset>
<noframes>

<body >

</body>
</noframes>
</html>

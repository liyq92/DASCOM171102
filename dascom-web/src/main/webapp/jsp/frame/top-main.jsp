<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat"%>
<%-- <%@page import="com.dascom.init.ConfigMgr"%> --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>

<script type="text/javascript">
$(function(){	
	//顶部导航切换
	$(".nav li a").click(function(){
		$(".nav li a.selected").removeClass("selected")
		$(this).addClass("selected");
	})	
})	
</script>
</head>

<style type="text/css">
#div3{
height:25px;
width:100%;
border:0px;
margin:0px;
border-collapse:collapse; 
}

#font1{
margin-left:0%;
color:#fff;
font-size:15px;
}
</style>

<body style="background:url(images/topbg.gif) repeat-x;">

    <div class="topleft">
    	<a href="javascript:return false;" target="_parent"><img src="images/logo.png" title="系统首页" /></a>
    </div>
    <ul class="nav" style="margin-top:35px;">
    	<font id="font1">${ope.openo}：您好，今天是
		  <%=new SimpleDateFormat("yyyy年MM月dd日，E").format(new Date())%>，欢迎使用号盾（地产版）管理系统！
		</font> 
    </ul>    
    <div class="topright" style="width:173px;">    
	    <ul>
		    <li style="margin-top:22px;"><a href="operatorLogout.do" onclick= "if(confirm( '是否确定要退出？')==false) return false;" target="_parent"><font id="font1">退出</font></a></li>
	    </ul>
    </div>
</body>
</html>

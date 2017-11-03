<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html; charset=UTF-8" %>
<%@include file="/jsp/include/taglib.jsp" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="js/jquery.js"></script>

<script type="text/javascript">
$(function(){	
	//导航切换
	$(".menuson li").click(function(){
		$(".menuson li.active").removeClass("active")
		$(this).addClass("active");
	});
	
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('ul').slideUp();
		if($ul.is(':visible')){
			$(this).next('ul').slideUp();
		}else{
			$(this).next('ul').slideDown();
		}
	});
})	
	
</script>


</head>

<body style="background:#f0f9fd;">
<%java.util.List authlist = (java.util.List)request.getAttribute("authMethodList");%>
	<div class="lefttop"></div>
    <dl class="leftmenu">
      
    <%if(authlist != null && authlist.contains("querycdrinfo")){%>
	    <dd>
	    <div class="title">
	    <span><img src="images/leftico02.png" /></span>通讯管理
	    </div>
	    	<ul class="menuson">
	    	<%if(authlist != null && (authlist.contains("querycdrinfo"))){%> 
	        	<li><cite></cite><a href="cdr/querycdrinfo.do?stype=first" target="rightFrame">语音查询</a><i></i></li>
	        <%} %>
	        </ul>     
	    </dd> 
    <%} %>
    
    <%if(authlist != null && authlist.contains("ssmnnumberManager")){%>
    	<dd>
		   <div class="title">
		   <span><img src="images/leftico03.png" /></span>副号码管理
		   </div>
		   	<ul class="menuson">
		   	<%if(authlist != null && (authlist.contains("ssmnnumberManager"))){%> 
		       	<li><cite></cite><a href="querySsmnNumber.do?stype=first" target="rightFrame">副号码查询</a><i></i></li>
		       	<li><cite></cite><a href="insertSsmnNumber.do?stype=first" target="rightFrame">新增副号码</a><i></i></li>
<%--		       	<li><cite></cite><a href="deletessmnnuber.do?stype=first" target="rightFrame">删除副号码</a><i></i></li>--%>
<%--		       	<li><cite></cite><a href="updatessmnnuber.do?stype=first" target="rightFrame">更改副号码</a><i></i></li>--%>
		       <%} %>
		       </ul>     
	   	</dd>
    <%} %>
    
    </dl>
</body>
</html>

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
    
    <%if(authlist != null && ((authlist.contains("modifyssmnnumber")) ||(authlist.contains("manageagent")))){%>  
	    <dd>
	    <div class="title">
	    <span><img src="images/leftico01.png" /></span>信息管理
	    </div>
	    	<ul class="menuson">
	    		<%if(authlist != null && (authlist.contains("manageagent"))){%> 
	        		<li><cite></cite><a href="manageagent.do?method=queryagent" target="rightFrame">经纪人管理</a><i></i></li>
	        	<%} %>
	        	<%if(authlist != null && (authlist.contains("modifyssmnnumber"))){%> 
	        		<li><cite></cite><a href="modifyssmnnumber.do?method=list" target="rightFrame">副号码管理</a><i></i></li>
	        	<%} %>
	        </ul>    
	    </dd>
    <%} %>    
    
    <%if(authlist != null && ((authlist.contains("queryssmninfo")) || (authlist.contains("statchannelcallin")) )){%>
	    <dd>
	    <div class="title">
	    <span><img src="images/leftico02.png" /></span>通讯管理
	    </div>
	    	<ul class="menuson">
	    	<%if(authlist != null && (authlist.contains("queryssmninfo"))){%> 
	        	<li><cite></cite><a href="queryssmninfo.do?type=1" target="rightFrame">语音查询</a><i></i></li>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("statchannelcallin"))){%>
	        	<li><cite></cite><a href="statchannelcallin.do" target="rightFrame">渠道统计</a><i></i></li>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("callManage"))){%>
	        	<li><cite></cite><a href="callmanage.do" target="rightFrame">呼叫管理</a><i></i></li>
	        <%} %>
	        </ul>     
	    </dd> 
    <%} %>
    
    <%if(authlist != null && (authlist.contains("queryinfo"))){%>
    <dd>
    	<div class="title">
    		<span><img src="images/leftico03.png" /></span> 
    		<cite></cite><a href="queryinfo.do" target="rightFrame">统计报表下载</a><i></i>
    	</div>
    </dd>  
    <%} %>
    
    <%if(authlist != null && (authlist.contains("downloadCDRinfo"))){%>
    <dd>
    	<div class="title">
    		<span><img src="images/leftico03.png" /></span> 
    		<cite></cite><a href="downloadCDRinfo.do?type=1&calltype=1" target="rightFrame">通话统计</a><i></i>
    	</div>
    </dd>  
    <%} %>
    
    <%if(authlist != null && ((authlist.contains("modifyoperatorinfo")) || (authlist.contains("searchfeedback")) )){%>
	    <dd><div class="title"><span><img src="images/leftico04.png" /></span>我的信息</div>
	    	<ul class="menuson">
	    	<%if(authlist != null && (authlist.contains("modifyoperatorinfo"))){%>
	        	<li><cite></cite><a href="modifyoperatorinfo.do?type=1" target="rightFrame">密码修改</a><i></i></li>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("searchfeedback"))){%>
	        	<li><cite></cite><a href="searchfeedback.do" target="rightFrame">信息反馈</a><i></i></li>
	        <%} %>
	    </ul>
	    </dd>  
	<%} %>
	
		<%
			if(authlist != null && ((authlist.contains("operatorloginsearch")) || 
			  (authlist.contains("operatorSearchManage")) || 
			  (authlist.contains("levelManage")) || (authlist.contains("resetpassword")) ||
			  (authlist.contains("addblackcallnumber")) || (authlist.contains("OPgroupSearch")) )){
		%>
	    <dd><div class="title"><span><img src="images/leftico04.png" /></span>操作员管理</div>
	    <ul class="menuson">
	    	<%if(authlist != null && (authlist.contains("operatorloginsearch"))){%>
	        	<li><cite></cite><a href="OperatorLoginSearch.do" target="rightFrame">登录详情查询</a><i></i></li>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("operatorSearch"))){%>
	        	<li><cite></cite><a href="operatorSearch.do" target="rightFrame">操作员管理</a><i></i></li>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("levelManage"))){%>
	        	<li><cite></cite><a href="levelmanage.do" target="rightFrame">架构管理</a><i></i></li>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("OPgroupSearch"))){%>
	        	<li><cite></cite><a href="OPgroupSearch.do" target="rightFrame">权限管理</a><i></i></li>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("resetpassword"))){%>
	        	<li><cite></cite><a href="modifyoperatorinfo.do?type=4" target="rightFrame">密码重置</a><i></i></li>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("addblackcallnumber"))){%>
	        	<li><cite></cite><a href="addblackcallnumber.do" target="rightFrame">黑名单管理</a><i></i></li>
	        <%} %>
	    </ul>
	    </dd>  
	<%} %>
    
    </dl>
</body>
</html>

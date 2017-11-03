<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/include/taglib.jsp"%>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ page import="com.dascom.init.ConfigMgr"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title><%=ConfigMgr.getSrvicename() %>管理系统</title>
<script src="${ctx}/js/cloud.js" type="text/javascript"></script>

<script language="javascript">
	$(function(){
    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
	$(window).resize(function(){  
    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
    })  
});  

function check(){
   if ($("#nameLogin").val()=='') {
	     alert("管理员帐号不能为空");  
	     $("#nameLogin").focus();
	     return false;
    }
	if ($("#password").val()=='') {
	     alert("密码不能为空");  
	      $("#password").focus();
	     return false;
    }

  	var obj = document.forms[0].name;
	var exp = new RegExp("^[A-Za-z0-9_]*$");

	if (!exp.test(obj.value)) {
        alert("管理员帐号必须是普通英文字符、数字或下划线");
		return false;
	}
    return true;
 }
 
 function remberPassword()
 {
 	var checkedRe = document.getElementById('rempaw').value;
 	if(checkedRe == '1')
 		document.getElementById('rempaw').value='0';
 	else
 		document.getElementById('rempaw').value='1';
 }
</script> 

</head>

<body style="background-color:#1c77ac; background-image:url(images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;"  >

<div id="mainBody">
  <div id="cloud1" class="cloud"></div>
  <div id="cloud2" class="cloud"></div>
</div>  

<div class="logintop">    
    <span>欢迎登录号盾（地产版）管理系统</span>    
</div>

<%
	// 得到所有的Cookie
	Cookie cookies[] = request.getCookies();
	String cookieName ="";
	String cookiePass ="";
	if(cookies != null) 
	{  
		// 如果已经设置了cookie ， 则得到它的值，并保存到变量pName中
		for(int i=0; i<cookies.length; i++) 
		{
			if(cookies[i].getName().equals("Name"))
				cookieName = cookies[i].getValue();
			if(cookies[i].getName().equals("Password"))
				cookiePass = cookies[i].getValue();
		}
	}
		
%>
    
<div class="loginbody">
<center>
</center>
    <span class="systemlogo"></span>   
    <center>
		<c:if test="${not empty(msg)}">
			<font color="red">${msg}</font>
		</c:if>
	</center>
    <form method="post" action="operatorLogin.do" onsubmit=" return check();" name="mainfm">
	    <div class="loginbox">
		    <ul>
		    <li>
		    	<c:if test="${name !=null }" >
		    		<input type="text" id="nameLogin" name="nameLogin" value="${name }" class="loginuser" value="admin"   maxlength="20"/>
		    	</c:if>
		    	<c:if test="${name ==null }">
		    		<input type="text" id="nameLogin" name="nameLogin" value="<%=cookieName %>" class="loginuser" value="admin"  maxlength="20"/>
		    	</c:if> 
		    </li>
		    <li>
		    	<c:if test="${password !=null }" >
		    		<input type="password" id="password" name="password" value="${password }" class="loginpwd" maxlength="20"/>
		    	</c:if>
		    	<c:if test="${password ==null }">
		    		<input type="password" id="password" name="password" value="<%=cookiePass %>" class="loginpwd" maxlength="20"/>
		    	</c:if>
		    </li>
		    <li>
		    	<input name="" type="submit" class="loginbtn" value="登录" />
			    	<label>
			    		<input name="rempaw" id="rempaw" type="checkbox" value="1" checked="checked" onclick="remberPassword();"/>记住密码
			    		<!--<c:if test="${rempaw =='1' }">
			    			<input name="rempaw" id="rempaw" type="checkbox" value="1" checked="checked" onclick="remberPassword();"/>记住密码
			    		</c:if>
			    		<c:if test="${rempaw !='1' }">
			    			<input name="rempaw" id="rempaw" type="checkbox" value="0"  onclick="remberPassword();"/>记住密码
			    		</c:if>-->
			    	</label>
		    		<label><a href="#" onclick="javascript:alert('请联系系统管理员');">忘记密码？</a></label>
		    </li>
		    </ul>
	    </div>
    </form>
</div>
<div class="loginbm"><font color="#0066CC" size="2"><b>(京ICP备10207939号) 版权所有</b></font></div>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Date;"%>
<%@ include file="/jsp/include/taglib.jsp" %>
<html>
<head>
<title></title>
<link rel="stylesheet" href="css/newstyle.css" type="text/css">
</head>

<body topmargin="0" leftmargin="0">
<script type="text/javascript">
<!--
function show2(){

if (!document.all)

return

var Digital=new Date()

var years=Digital.getYear()

var months=Digital.getMonth()+1

var days=Digital.getDate();

var hours=Digital.getHours()

var minutes=Digital.getMinutes()

var seconds=Digital.getSeconds()

if (minutes<=9)

minutes="0"+minutes

if (seconds<=9)

seconds="0"+seconds

var ctime=years+"-"+months+"-"+days+"  "+hours+":"+minutes+":"+seconds

tick2.innerHTML=ctime

setTimeout("show2()",1000)

}

window.onload=show2

//-->
</script> 
<table width="100%" height="31" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="60%" height="31" class="t1"><span id=tick2></span>&nbsp;&nbsp;&nbsp;&nbsp;欢迎您&nbsp;${loginOperator.loginName}</td>
        <td width="19%" class="t1" align="right"><a href='frameMain.do' target='mainFrame' class="link1">首页</a> |<a href='operatorLogout.do' target='_parent' class="link1"> 退出系统</a></td>
      </tr>
    </table>
</body>
</html>

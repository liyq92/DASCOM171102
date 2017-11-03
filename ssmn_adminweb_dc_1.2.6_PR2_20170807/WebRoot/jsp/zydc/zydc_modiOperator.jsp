<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ page import="com.dascom.ssmn.util.Constants" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />

<script language="JavaScript" type="text/JavaScript">
<!--
var wBox;

function cancel(){
	wBox.close();
}

function init(msg,zipfilename){
	if(msg != ''){
		wBox=jq("#wbox10").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
	if(zipfilename != '')
	{
	
		javascript:document.location.href='${ctx}/<%=Constants.RECORD_ZIP_FOLDER_NAME%>/<%="recordZip"%>${sdate}.zip';
	}
}

function download(path){
 var sUrl = "${ctx}/jsp/zydc/download.jsp?path="+path;
 location.href = sUrl;
}

function check()
{
	var op = document.getElementById('opepwd').value;
	if(op == '')
	{
		alert('请输入原密码!')
		return false;
	}
	
	var opnew = document.getElementById('newopepwd').value;
	if(opnew == '')
	{
		alert('请输入新密码!')
		return false;
	}
	
	var opok = document.getElementById('opepwdok').value;
	if(opok == '')
	{
		alert('请输入确认密码!')
		return false;
	}
}

//-->
</script>
<style>
label
{
	width:80px;
	text-align:right;
	display:inline-block; 
	font-size: 12px;
}
.dfinput
{
	width:248px;
}
tr
{
	height:40px;
}
</style>
</head>

<body topmargin="0" onLoad="init('${msg}','${zipfilename }');">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">账号密码修改</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;"> 
<form method="post" action="modifyoperatorinfo.do?type=2" onsubmit="return check();" name="mainfm">
<table width="100%" align="center" style="border-style:none;margin-left:50px;margin-top:30px;">
    <tr>
    	<td width="90px;"><label>账号：</label></td>
		<td>${loginname}</td>
    </tr>
    <tr style="">
    	<td><label>原密码：</label></td>
    	<td>
    		<input class="dfinput" id="opepwd" type="password" name="opepwd" value="${opepwd}" type="text" size="20" maxlength="20" />
    	</td>    
    </tr>
    <tr>
		<td><label>新密码：</label></td>
    	<td>
    		<input class="dfinput" id="newopepwd" type="password"  name="newopepwd" value="${newopepwd}" type="text" size="20" maxlength="20" />
    	</td>
    </tr>
    <tr>
    	<td><label>确认密码：</label></td>
    	<td>
    		<input class="dfinput" id="opepwdok" type="password"  name="opepwdok" value="${opepwdok}" type="text" size="20" maxlength="20" />
    	</td>
    </tr> 
    <tr> 
    	<td>&nbsp;</td>
    	<td>
    		<input style="margin-left:8%;" class="scbtn" type="submit" value="保存" />
    	</td>
    </tr>
    <tr>
        <td></td>
    </tr> 
</table>
</form>
<br/>

</div>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>

<script language="JavaScript" type="text/JavaScript">	
var wBox;
var ssmnall;//取到的副号码
 
function cancel(){
	wBox.close();
}

function check()
{
	var vname = document.getElementById('uname').value;
	if(vname == '')
	{
		alert('请输入姓名!');
		return;
	}
	
	var vmsisdn = document.getElementById('umsisdn').value;
	if(vmsisdn == '')
	{
		alert('请输入手机号!');
		return;
	}
	var vcontent = document.getElementById('xh_editor').value;
	if(vcontent == '')
	{
		alert('请输入反馈内容!');
		return;
	}

}

function addfeedback()
{
	var vname = document.getElementById('uname').value;
	if(vname == '')
	{
		alert('请输入姓名!');
		return;
	}
	
	var vmsisdn = document.getElementById('umsisdn').value;
	if(vmsisdn == '')
	{
		alert('请输入手机号!');
		return;
	}
	var vcontent = document.getElementById('xh_editor').value;
	if(vcontent == '')
	{
		alert('请输入反馈内容!');
		return;
	}
	
	var vopera = document.getElementById('opera').value;
	
	document.location.href="searchfeedback.do?type=add&opera="+vopera+"&uname="+vname+"&umsisdn="+vmsisdn+"&fcontent="+vcontent;
}

function fback()
{
	var vcontent = document.getElementById('feedcontent').value;
	var vstarttime = document.getElementById('startdate').value;
	var vendtime = document.getElementById('enddate').value;
	document.location.href='searchfeedback.do?feedcontent='+vcontent+"&startdate="+vstarttime+"&enddate="+vendtime;

}
</script>
<style>
label
{
	width:80px;
	text-align:right;
	display:inline-block; 
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

<body topmargin="0" onLoad="init('${msg}');" >
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">添加信息反馈</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<input type="hidden" name="feedcontent" id="feedcontent" value="${feedcontent}" />
<input type="hidden" name="startdate" id="startdate" value="${startdate}" />
<input type="hidden" name="enddate" id="enddate" value="${enddate}" />
<input type='hidden' name='opera' id='opera' value='${opera}' />
<div class="forminfo">
<form method="post" action="searchfeedback.do?type=add" onsubmit="return check();" name="mainfm">
<table width="100%" align="center" style="border-style:none;margin-left:30px;margin-top:30px;font-size:12px;">
		<tr>
			<td width="90px;"><label>账号：</label></td>
			<td>${opera}</td>
		</tr>
		<tr>
			<td><label>${branName }：</label></td>
			<td>${branch}</td>
		</tr>
		<tr>
			<td><label>姓名：</label></td>
			<td >
				<input class="dfinput" id="uname" name="uname" value="${uname}" type="text" size="20" maxlength="20"/><font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td><label>手机号：</label></td>
			<td>
				<input class="dfinput" id="umsisdn" name="umsisdn" value="${umsisdn}" type="text" size="20" maxlength="20"/><font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td><label>反馈内容：</label></td>
			<td>
				<textarea style="margin-top:5px;" class="textinput" name="fcontent" id="xh_editor" rows="10" cols="60">${fcontent}</textarea>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input style="margin-left:8%;" class="scbtn" type="button" value="提交" onclick="addfeedback()"; /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    		<input class="scbtn" type="button" value="返回"  onClick="fback();"/>
			</td>
		</tr>
</table>
</form>

</div>

<br/>

</div>
</body>
</html>

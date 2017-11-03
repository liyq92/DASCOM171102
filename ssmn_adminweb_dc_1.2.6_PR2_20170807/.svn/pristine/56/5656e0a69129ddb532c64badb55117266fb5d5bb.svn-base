<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ page import="com.dascom.ssmn.util.Constants" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script language="JavaScript" type="text/JavaScript">
var wBox;
var jq=jQuery.noConflict();
var opt;
 
function cancel(){
	wBox.close();
}

function init(msg){
	if(msg != ''){
		wBox=jQuery("#wbox10").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
	
}

function download(path){
 var sUrl = "${ctx}/jsp/zydc/download.jsp?path="+path;
 location.href = sUrl;
}

function check()
{
	opt = document.getElementById('openoo').value;
	if(opt == '')
	{
		alert('请输入账号!')
		return false;
	}

	if(opt != '')
	{
		wBox=jQuery("#wbox10").wBox({
			title: "",
			html: 	"<div style='width:300px;height:100px;'>"+
					"<table align='center' style='margin-top:20px;width:100%;'>"+
						"<tr><td align='center'><div style='font-family:Arial;font-size:14px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>确认是否重置"+opt+"账号的密码?</div></td>"+
						"</tr>"+
					"</table><div style='margin-bottom:5px;margin-top:10px;'>"+
					"<table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
					"<div> <input class='cancel' style='margin-top:8px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />&nbsp;&nbsp;&nbsp;&nbsp;"+
					"	   <input class='sure'  style='margin-top:8px;margin-left:10px;width:30%;' type='button' value='确定' onclick='resetpwd();' /> </div></div>"
			});
			wBox.showBox();
	}
	
}

function resetpwd()
{
	if(opt != '')
		document.location.href="modifyoperatorinfo.do?type=3&openoo="+opt;
}

//-->
</script>
</head>

<body topmargin="0" onLoad="init('${msg}');">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">密码重置</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<form method="post" action="modifyoperatorinfo.do?type=3" onsubmit="return check();" name="mainfm">
<table width="100%" align="center">
    <tr style="height:40px;">
    	<td  class="t6" style="text-indent:10px;">账号：</td>
		<td class="t7" style="width:190px;height:40px;border-right:none;" >
			<input style="width:148px;" class="scinput" id="openoo"  name="openoo"  value="${openoo}" type="text" size="20" maxlength="20" />
		</td>
		<td class="t7" style="border-left:none;">&nbsp;</td>
    </tr>
    <tr>
        <td height="6" colspan="3" bgcolor="ECECF5"></td>
    </tr> 
    <tr>
    	<td colspan="3">&nbsp;</td>
    </tr>
    <tr>
    	<td colspan="3"  align=center>
    		<input class="scbtn" type="button" value="重置"  onclick="check();" />
    	</td>
    </tr>
</table>
</form>
<br/>
</div>

</body>
</html>

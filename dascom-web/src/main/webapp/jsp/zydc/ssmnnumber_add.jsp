<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%-- <%@ page import="com.dascom.ssmn.util.Constants" %> --%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript">
$(document).ready(function(e) {
    $(".select1").uedSelect({
		width : 345			  
	});
	$(".select2").uedSelect({
		width : 167  
	});
	$(".select3").uedSelect({
		width : 150
	});
});
</script>
<script language="JavaScript" type="text/JavaScript">

var wBox;
var clear = ""; 
var isIELow =0;//判断是否是IE的低版本
var isIE=0;//判断是否是IE
function cancel(){
	wBox.close();
}

function checkIE()
{
	if (!!window.ActiveXObject || "ActiveXObject" in window)
		isIE =1;//是IE
	else
		isIE =0;
}

function checkIEVersion()
{
	if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/6./i)=="6."){ 
		isIELow=1;//IE6
	} 
	else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/7./i)=="7."){ 
		isIELow=1;//IE7
	} 
	else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i)=="8."){ 
		isIELow=1;//IE8
	} 
	else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/9./i)=="9."){ 
		isIELow=1;//IE9 
	}else
		isIELow=0;
}

function init(msg,zipfilename){
	
	checkIE();
	checkIEVersion();

	if(msg != ''){
		wBox=jQuery("#wbox10").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
	if(zipfilename != '')
	{
	
<%-- 		javascript:document.location.href='${ctx}/<%=Constants.RECORD_ZIP_FOLDER_NAME%>/<%="recordZip"%>${sdate}.zip'; --%>
	}
}

function exportInfo()
{
	window.top.document.getElementById("popId").style.display='block';
	wBox=jQuery("#wbox10").wBox({
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>正在导出，请稍后...</td></tr></table></div>",
		noTitle:true
		});
		wBox.showBox();
	sendRequestCDR();
}

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
 
function sendRequestCDR(){
 	createXMLHttpRequest();
 	var startdate = document.getElementById("startdate").value;
 	var starttime = document.getElementById("starttime").value;
 	var enddate = document.getElementById("enddate").value;
 	var endtime = document.getElementById("endtime").value;
 	var caller = document.getElementById("caller").value;   
 	var callee = document.getElementById("callee").value;
 	var callId = document.getElementById("callId").value;
 	var calleeDisplay = document.getElementById("calleeDisplay").value;
	var callremis = document.getElementById("callReMis");//已接/未接
	var recordcount = document.getElementById("recordCount").value;
	var callingType = document.getElementById("callingType").value;
	var endReason = document.getElementById("endReason").value;
	var callerDisplay = document.getElementById("callerDisplay").value;
 	
 	XMLHttpReq.open("POST","${ctx}/exportcdrinfo",true);
	XMLHttpReq.onreadystatechange = processResponseCDR;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl = "stype=1&startdate="+startdate+"&starttime="+starttime+"&enddate="+enddate+"&endtime="
	+endtime+"&caller="+caller+"&callee="+callee+"&callId="+callId+"&calleeDisplay="+calleeDisplay+
	"&callingType="+callingType+"&endReason="+endReason+"&callerDisplay="+callerDisplay;
	
	if(callremis.selectedIndex !=0)
		vurl +="&callremis="+callremis.value;

	XMLHttpReq.send(vurl);
}

function processResponseCDR(type){
	
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageCDR();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }
 
function parseMessageCDR()
{
	window.top.document.getElementById("popId").style.display='none';
	wBox.close();
	var ssmnall=XMLHttpReq.responseText;
	//alert(ssmnall);
	if(ssmnall == '')//导出失败
	{
		alert('生成报表失败!');
	}
	else//成功，把名称反回来
	{
		var sre = "${ctx}/exportrecordFiles/"+ssmnall;
		//alert(sre);
		document.location.href=sre;
		//var sUrl = "http://192.168.21.188:8080/ssmn_adminweb_dc/exportrecordFiles/(dascom)_150612141049.xlsx";
 	//location.href = sUrl;
	
	}
	
}

function check()
{
/*	var startdate = document.getElementById("startdate").value;
	var starttime = document.getElementById("starttime").value;
 	var enddate = document.getElementById("enddate").value;
 	var endtime = document.getElementById("endtime").value;

	//时间设置成必选项
	if(startdate == '')
	{
		alert('请输入查询起始时间!');
		return;
	}	
	if(enddate == '')
	{
		alert('请输入查询终止时间!');
		return;
	}*/
}
</script>
<style>
.tablelist td 
{
	text-indent:0px;
}
.tablelist th 
{
	text-indent:0px;
}
</style>
</head>

<body topmargin="0" onLoad="init('${msg}','${zipfilename }');">
<%--<%java.util.List authlist = (java.util.List)request.getAttribute("authMethodList");%>--%>
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">新增副号码</a></li>
	</ul>
		<span>${resMsg }</span>
</div>
<br />
<div class="formbody" style="text-align: center;"> 
<form method="post" action="insertSsmnNumber.do" onsubmit="return check();" name="mainfm" enctype="multipart/form-data">
	<table width="80%" style="text-align: center;">
		<tr style="height:60px;">
			<td class="t6" style="text-indent:20px;">副号码：</td>
	    	<td class="t7" style="width:180px;height:40px;">
	    		<input style="width:500px;" class="scinput" id="ssmnnumber" name="ssmnnumber" value="" type="text"  />
				<span>(若批量添加，号码间用逗号分开如：158***1460,131***0456)</span>	    		
	    	</td>   
	    	<td>
	    		<input id="filePath" name="filePath" type="file"/>
	    	</td>       	
    	</tr>
    <tr>
        <td height="6" colspan="8" bgcolor="ECECF5"></td>
    </tr> 
    <tr><td>&nbsp;</td></tr>
    	<tr>
    		<td class="t6" style="text-indent:20px;">状态：</td>
    		<td class="t7" rowspan="2" style="width:180px;height:60px;">
	    		<select class="select3" id='ssmnnumberstate' name='ssmnnumberstate' style='width:20px ; height:20px;'>
				<option value='0'>全部</option>
					<option value='2' <c:if test="${state=='2'}">selected='true'</c:if>>保留</option>
					<option value='1' <c:if test="${state=='1'}">selected='true'</c:if>>使用中</option>
					<option value='0' <c:if test="${state=='0'}">selected='true'</c:if>>空闲</option>
				</select>
			</td>
    	</tr>
    <tr>
        <td height="6" colspan="8" bgcolor="ECECF5"></td>
    </tr> 
    <tr><td>&nbsp;</td></tr>
    	<tr>
    		<td class="t7"  style="text-align: center;" colspan="8" >
	        	<input class="scbtn" type="submit" value="添加" />
    		</td>
    	</tr>
    	<tr>
        <td height="6" colspan="8" bgcolor="ECECF5"></td>
    </tr> 
    <tr><td>&nbsp;</td></tr>
	</table>
</form>

<div align="center" class="showNumber"></div>
<br/>
<br/>
<br/>
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>

<script type="text/javascript">
	$('.tablelist1 tbody tr:odd').addClass('odd');
</script>

</body>
</html>

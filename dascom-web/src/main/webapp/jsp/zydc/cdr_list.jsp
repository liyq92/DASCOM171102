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

	if(isIE ==1 && isIELow ==1)//低版本IE
	{
		div_player.style.display="block";
		document.getElementById("audioDiv").style.display="none";
	}else
	{
		div_player.style.display="none";
		document.getElementById("audioDiv").style.display="block";
	}
	
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

function download(path){
 var sUrl = "${ctx}/jsp/zydc/download.jsp?path="+path;
 location.href = sUrl;
}

function playRecord(path){
	
	var html = '<object width="90%" height="64" type="video/x-ms-asf">'
		+ '<param name="filename" value="'+path+'" />'
		+ '<param name="autostart" value="1" />'
		+ '<param name="playcount" value="3" />'
		+ '<param name="ShowStatusBar" value="1" />'
		+ '<param name="wmode" value="transparent" />'
		+ '<embed type="application/x-mplayer2" width="90%" height="64" src="'+path+'"'
		+ '	autostart="true" playcount="true" wmode="Opaque" />'
		+ '</object>';
	
	jQuery("#div_player").html(html);
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

function InsertLog(streamnumber ,ssmnnuber)
{
	sendRequestLog(streamnumber,ssmnnuber);
	document.getElementById(streamnumber).style.color="#c3272b";
}

//记录日志,听录音
function sendRequestLog(callid){
 	createXMLHttpRequest();
 	
 	XMLHttpReq.open("POST","${ctx}/exportcdrinfo",true);
	XMLHttpReq.onreadystatechange = processResponseLog;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl = "stype=5";
	if(callid != '')
		vurl += "&callid="+callid;
	
	XMLHttpReq.send(vurl);
}

function processResponseLog(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageLog();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }
 
 function parseMessageLog()
{
	var ssmnall=XMLHttpReq.responseText;
	if(ssmnall == '')//失败
	{
		
	}
	else//成功
	{
	
	}
}

function playCdr(filepathOnline)
{
	 if(isIE ==1 && isIELow ==1)//低版本IE
	 {
  		div_player.Filename=filepathOnline;
  	 }
  	else
  	{
  		var audio = document.getElementById("audio");
		audio.src=filepathOnline;
		audio.play();
  	}
	 
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
<%java.util.List authlist = (java.util.List)request.getAttribute("authMethodList");%>
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">语音查询</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;"> 
<input type="hidden" name="pagenumber" id="pagenumber" value="${pagenumber}" />
<form method="post" action="querycdrinfo.do" onsubmit="return check();" name="mainfm">
	<table width="100%" align="center">
		<tr style="height:40px;">
			<td class="t6" style="text-indent:10px;">主叫号码：</td>
	    	<td class="t7" style="width:180px;height:40px;">
	    		<input style="width:148px;" class="scinput" id="caller" name="caller" value="${caller}" type="text" size="20" maxlength="11" />
	    	</td>          	
	  		<td class="t6" style="text-indent:10px;">被叫号码：</td>
	    	<td class="t7" style="width:180px;height:40px;">
	    		<input style="width:148px;" class="scinput" id="callee" name="callee" value="${callee}" type="text" size="20" maxlength="11" />
	    	</td> 
	    	
	    	<td class="t6" style="text-indent:10px;" rowspan="2">已接/未接：</td>
    		<td class="t7" rowspan="2">
	    		<select class="select3" id='callReMis' name='callReMis' style='width:120px ; height:20px;'>
				<option value=''>全部</option>
					<option value='1' <c:if test="${callReMis=='1'}">selected='true'</c:if>>已接</option>
					<option value='0' <c:if test="${callReMis=='0'}">selected='true'</c:if>>未接</option>
				</select>
			</td>
    	</tr>
    	<tr>
    	<td class="t6" style="text-indent:10px;">主外显号：</td>
	    	<td class="t7" style="width:180px;height:40px;" >
	    		<input style="width:148px;" class="scinput" id="callerDisplay" name="callerDisplay" value="${callerDisplay}" type="text" size="20" />
	    	</td>
	    	<td class="t6" style="text-indent:10px;">被外显号：</td>
	    	<td class="t7" style="width:180px;height:40px;" >
	    		<input style="width:148px;" class="scinput" id="calleeDisplay" name="calleeDisplay" value="${calleeDisplay}" type="text" size="20" />
	    	</td>
    	</tr>
    	<tr>
	    	<td class="t6" style="text-indent:10px;">序号：</td>
	    	<td class="t7" style="width:120px;height:40px;" >
	    		<input style="width:148px;" class="scinput" id="callId" name="callId" value="${callId}" type="text" size="20" />
	    	</td>
			<td class="t6" style="text-indent:10px;" >起止时间：</td>
			<td class="t7" width="200" colspan="4" >
				&nbsp;<label style="margin-top:15px;">从</label>&nbsp;
				<input class="scinput" type="text" id="startdate"  name="startdate" value="${startdate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:120px"/> 日
				<input class="scinput" type="text" id="starttime" name="starttime" value="${starttime }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'HH'})" class="Wdate" style="width:50px"/> 时 &nbsp;到&nbsp;
				<input class="scinput" type="text" id="enddate" name="enddate" value="${enddate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:120px"/> 日 
				<input class="scinput" type="text" id="endtime" name="endtime" value="${endtime }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'HH'})" class="Wdate" style="width:50px"/> 时
	        </td>
    	</tr>   
    	<tr>
    		<td class="t6" style="text-indent:10px;">通话类型：</td>
	    		<td class="t7" rowspan="2">
	    		<select class="select3" id='callingType' name='callingType' style='width:120px ; height:20px;'>
				<option value=''>全部</option>
					<option value='1' <c:if test="${callingType=='1'}">selected='true'</c:if>>PC双呼</option>
					<option value='2' <c:if test="${callingType=='2'}">selected='true'</c:if>>回拨经纪人</option>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">挂断原因：</td>
	    		<td class="t7" rowspan="2">
	    		<select class="select3" id='endReason' name='endReason' style='width:120px ; height:20px;'>
				<option value=''>全部</option>
					<option value='0' <c:if test="${endReason=='0'}">selected='true'</c:if>>未知</option>
					<option value='1' <c:if test="${endReason=='1'}">selected='true'</c:if>>正常通话结束</option>
					<option value='2' <c:if test="${endReason=='2'}">selected='true'</c:if>>主叫呼叫失败</option>
					<option value='3' <c:if test="${endReason=='3'}">selected='true'</c:if>>主叫接通被叫呼叫失败</option>
					<option value='4' <c:if test="${endReason=='4'}">selected='true'</c:if>>主叫接通被叫未接听</option>
				</select>
			</td>
    		<td class="t7" colspan="2" align="center" valign="middle">
	        	<input class="scbtn" type="submit" value="查询" />
    		</td>
    	</tr>
    <tr>
        <td height="6" colspan="8" bgcolor="ECECF5"></td>
    </tr> 
    <tr><td>&nbsp;</td></tr>
	</table>
</form>

<c:if test="${recordCount >0}">

<c:if test="${calltype !='1'}">
<c:if test="${numchannelOutCounts >0 }" >
<c:if test="${calltype !='2' }"><!-- 有呼入和呼出,加左边距 -->
<table class="tablelist" align="left" style="border-collapse:collapse;margin-bottom:18px;width:${channelcountNumout*84}px;" border="1" bordercolor="#3891a8">
</c:if>
<c:if test="${calltype =='2' }"><!-- 只有呼出,不加左边距 -->
<table class="tablelist" align="left" style="border-collapse:collapse; margin-bottom:18px;width:${channelcountNumout*84}px;" border="1" bordercolor="#3891a8">
</c:if>
	<thead>
	<tr>
		<th style="text-align:center;width:96px;font-size:14px;" colspan=${channelcountNumout} >呼出统计</th>
	</tr>
	</thead>
	<c:forEach items="${channeloutcount}" var="c" varStatus="s">
	<tbody>
		<tr align="center" >
			<c:forEach items="${c}" var="m" varStatus="s">
				<td style="width:84px;">${m}</td>
			</c:forEach>
		</tr>
	</tbody>
	</c:forEach>
</table>
</c:if>
</c:if>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

<div  align="left" style="z-index:-1;">
	<object id="div_player" width="100%" height="64"  type="video/x-ms-asf" >
	<param name="AutoRewind" value=1 />
	<param name="FileName" value="" />
	<param name=InvokeURLs value=-1 />
	<param name=Language value=-1 />
	<param name=Mute value=0 />
	<param name=PlayCount value=1 />
	<param name=PreviewMode value=0 />
	<param name=Rate value=1 />
	<param name=ShowCaptioning value=0 />
	<param name=ShowControls value=1 />
	<param name=ShowAudioControls value=1 />
	<param name=ShowPositionControls value=1 />	
	<param name=ShowTracker value=1 />
	<param name=TransparentAtStart value=0 />	
	<param name=ShowStatusBar value=1 />
	<param name=VideoBorder3D value=0 />
	<param name=Volume value=-570 />
	<param name=WindowlessVideo value=-1 />
	<param name="wmode" value="Opaque" />
	</object>
</div>

<div id="audioDiv" align="left" style="z-index:-1;">
<br/>
<br/>
<audio id="audio" controls="controls" />
</div>

<br/>
<table class="tablelist" >
	<thead>   
		<tr> 
			<th><b>序号</b></th>
	        <th><b>主叫号码</b></th>
	        <th><b>被叫号码</b></th>
	        <th><b>主外显号</b></th>
	        <th><b>被外显号</b></th>
	        <th><b>通话类型</b></th>
	        <th><b>通话结束原因</b></th>
	        <th><b>通话时间</b></th>
	        <th><b>通话时长(分)</b></th>
	        <%if(authlist != null && (authlist.contains("listenOnline")) && (authlist.contains("querycdrinfo"))){ %>
	       		<th><b>在线收听</b></th>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("cdrDownload")) && (authlist.contains("querycdrinfo"))){ %>
	        	<th><b>下载</b></th>
	        <%} %>
	        
	 	</tr>
 	</thead> 
 	<c:forEach items="${cdrlist}" var="u" varStatus="s">
 		<tbody>
	 		<tr>
	 			<td>${u.callid}</td> 
	 			<td>${u.caller}</td> 
	 			<td>${u.callee}</td> 
	 			<td>${u.callerDisplay}</td> 
	 			<td>${u.calleeDisplay}</td> 
	 			<td>${u.calltype}</td>
	 			<c:if test="${u.state == 0 }">
	 				<td>未知</td>
	 			</c:if>
	 			<c:if test="${u.state == 1 }">
	 				<td>正常通话结束</td>
	 			</c:if> 
	 			<c:if test="${u.state == 2 }">
	 				<td>主叫呼叫失败</td>
	 			</c:if> 
	 			<c:if test="${u.state == 3 }">
	 				<td>主叫接通被叫呼叫失败</td>
	 			</c:if> 
	 			<c:if test="${u.state == 4 }">
	 				<td>主叫接通被叫未接听</td>
	 			</c:if> 
	 			<td>${u.answerTimeCalleeStr}</td> 
	 			<td height="20">${u.billDurationCalleeSe}</td>
	 			
		 			 <%if(authlist != null && (authlist.contains("listenOnline")) && (authlist.contains("querycdrinfo"))){ %>
			 			<td>
				 			 <c:if test="${u.fileName != null && u.fileName != '' && u.filepathOnline != null && u.filepathOnline != ''}"> 
				 			 	 <c:if test="${u.isRead == 1}">
				 			 	 	<input class="scbtn2"  type="button" id="${u.callid}" style="color:#c3272b;" onclick="playCdr('${u.filepathOnline}');InsertLog('${u.callid}');" value="在线收听" />			 	
				 			 	 </c:if>
				 			 	  <c:if test="${u.isRead == 0}">		 	
				 			 	 	 <input class="scbtn2" id="${u.callid}"  type="button" value="在线收听" onclick="playCdr('${u.filepathOnline}');InsertLog('${u.callid}');"  />  
				 			 	 </c:if>
				 			 </c:if>
				 			 <c:if test="${(u.fileName != null && u.fileName != '' ) && ( u.filepathOnline == null || u.filepathOnline == '' )}"> <!-- 必须是有录音文件，如果没有录音文件，则不需要显示 -->
				 			 	<!--  <a href="javascript:playRecord('${u.filepathOnline}');" ><u>在线收听</u></a> -->
				 			 	 文件不存在	<!--${u.fileName}   {u.filepathOnline}-->
				 			 </c:if>
			 			</td>  
		 			 <%} %>
		 			
		 			 <%if(authlist != null && (authlist.contains("cdrDownload")) && (authlist.contains("querycdrinfo"))){ %>
			 			 <td>
			 			 <c:if test="${u.fileName != null && u.fileName != '' && u.filepathOnline != null && u.filepathOnline != ''}"> 
			 			 	<input class="scbtn3" type="button" onclick="download('${u.filepath}');" value="下 载"/>
			 			 </c:if>
			 			 <c:if test="${u.fileName == null || u.fileName == '' || u.filepathOnline == null || u.filepathOnline == ''}"> 
				 			 	<!--  <a href="javascript:playRecord('${u.filepathOnline}');" ><u>在线收听</u></a> -->
				 			 	<!--   文件不存在!	-->		 	
				 			 </c:if>
			 			 </td>
		 			<%} %>
	 		</tr>
 		</tbody>
 	</c:forEach>
</table>

<form method="post" action="querycdrinfo.do" name="pageForm">
<input type="hidden" name="caller" value="${caller}" />
<input type="hidden" name="callee" value="${callee}" />
<input type="hidden" name="startdate" value="${startdate}" />
<input type="hidden" name="starttime" value="${starttime}" />
<input type="hidden" name="enddate" value="${enddate}" />
<input type="hidden" name="endtime" value="${endtime}" />
<input type="hidden" name="callerDisplay" value="${callerDisplay}" />
<input type="hidden" name="calleeDisplay" value="${calleeDisplay}" />
<input type="hidden" name="callId" value="${callId}" />
<input type="hidden" name="callReMis" value="${callReMis}" />
<input type="hidden" id="recordCount" value="${recordCount}" />
<input type="hidden" id="callingType" value="${callingType}" />
<input type="hidden" id="endReason" value="${endReason}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="querycdrinfo.do?page="/>
</form>

<br/>
<br/>

 <div style="width:100%;" align="right" > <input class="scbtn1" type="button" value="通话记录导出" onClick="exportInfo()" /></div> 	
</c:if>
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

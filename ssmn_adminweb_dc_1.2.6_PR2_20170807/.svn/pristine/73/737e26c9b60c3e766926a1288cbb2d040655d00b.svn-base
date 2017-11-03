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
<!--
var wBox;
//var jq=jQuery.noConflict();
function clean(){
	document.getElementById("name1").value='';
	document.getElementById("cardgrade1").value='1';
}
function checkDelete(){	
	if(window.confirm('您确定要删除吗？')){
		return true;
	} return false;
}

function check(){
	var startdate=document.getElementById("startdate").value;
	var enddate=document.getElementById("enddate").value;
	if(startdate!=""){
		if(enddate!=""){
			if(isStartEndDate(startdate,enddate)){
				return true;
			}else{
				alert("查询结束时间应晚于开始时间！");
				return false;
			}
		}else{
			alert("请输入查询结束时间！");
		return false;
		}
	}else{
		alert("请输入查询开始时间！");
		return false;
	}
}
function isStartEndDate(startDate,endDate){   
     if(startDate.length>0&&endDate.length>0){  
      var startDateTemp = startDate.split(" ");  
      var endDateTemp = endDate.split(" ");  
      //分开日期   
      var arrStartDate = startDateTemp[0].split("-");   
      var arrEndDate = endDateTemp[0].split("-");   
      //分开时间
      // var arrStartTime = startDateTemp[1].split(":");   
      // var arrEndTime = endDateTemp[1].split(":");   
      //                          年                月                    日            时                    分              秒
      var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2],"00","00","00");   
      var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2],"00","00","00");  
      if(allStartDate.getTime()>allEndDate.getTime()){
       return false;   
      }   
     }   
     return true;   
    } 


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

function switchBut(varBack)
{
	if(varBack.value == '昨日')
	{
		document.getElementById("yesterday").style.background ="#87CEEB";
		document.getElementById("lastWeek").style.background ="#FFF5EE";
		document.getElementById("lastMonth").style.background ="#FFF5EE";
		document.location.href="downloadCDRinfo.do?type=1&calltype=1";
	}
		
	if(varBack.value == '上周')
	{
		document.getElementById("yesterday").style.background ="#FFF5EE";
		document.getElementById("lastWeek").style.background ="#87CEEB";
		document.getElementById("lastMonth").style.background ="#FFF5EE";
		document.location.href="downloadCDRinfo.do?type=2&calltype=1";
	}
		
	if(varBack.value == '上个月')
	{
		document.getElementById("yesterday").style.background ="#FFF5EE";
		document.getElementById("lastWeek").style.background ="#FFF5EE";
		document.getElementById("lastMonth").style.background ="#87CEEB";
		document.location.href="downloadCDRinfo.do?type=3&calltype=1";
	}
}

function switchInOutBut(varBack,calltypetemp)
{
	if(varBack.value == '呼入')
	{
		document.getElementById("callin").style.background ="#87CEEB";
		document.getElementById("callout").style.background ="#FFF5EE";
		document.location.href="downloadCDRinfo.do?type="+calltypetemp+"&calltype=1";
	}
		
	if(varBack.value == '呼出')
	{
		document.getElementById("callin").style.background ="#FFF5EE";
		document.getElementById("callout").style.background ="#87CEEB";
		document.location.href="downloadCDRinfo.do?type="+calltypetemp+"&calltype=2";
	}
}

function download(path){
 var sUrl = "${ctx}/jsp/zydc/download.jsp?path="+path;
 location.href = sUrl;
}

function onclickDownload(sExcelCdr,sExcelDate,sExcelFileName,isExists,stype,callExtent,showtypetemp)
{
if(isExists == '0')
{
	if(stype == '1')
		alert('暂无昨日报表!');
	else if(stype == '2')
		alert('暂无上周报表!');
	else if(stype == '3')
		alert('暂无上月报表!');
	
	return;
}
javascript:document.location.href="${ctx}/<%=Constants.DOWNLOAD_CALL_STATISTIC_FILENAME%>"+"/"+sExcelCdr+"/"+sExcelDate+"/"+sExcelFileName+showtypetemp+callExtent;

}

//-->
</script>
</head>

<body topmargin="0" onLoad="init('${msg}');">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">通话统计</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">

<div align="left">
<c:if test ="${type == 1}" >
	<input style="width:80px ; height:30px; background:#87CEEB;"  id="yesterday"  type="button" value="昨日"  onclick="switchBut(this);" />
</c:if>	
<c:if test ="${type != 1}" >
	<input style="width:80px ; height:30px; background:#FFF5EE;"  id="yesterday"  type="button" value="昨日"  onclick="switchBut(this);" />
</c:if>

<c:if test ="${type == 2}" >	
	<input style="width:80px ; height:30px; margin-left:-1%; background:#87CEEB;"  id="lastWeek"  type="button" value="上周" onclick="switchBut(this);" />
</c:if>
<c:if test ="${type != 2}" >	
	<input style="width:80px ; height:30px; margin-left:-1%; background:#FFF5EE;"  id="lastWeek"  type="button" value="上周" onclick="switchBut(this);" />
</c:if>

<c:if test ="${type == 3}" >
	<input style="width:80px ; height:30px;margin-left:-1%; background:#87CEEB;"  id="lastMonth"  type="button" value="上个月" onclick="switchBut(this);" />
</c:if>
<c:if test ="${type != 3}" >
	<input style="width:80px ; height:30px;margin-left:-1%; background:#FFF5EE;"  id="lastMonth"  type="button" value="上个月" onclick="switchBut(this);" />
</c:if>

</div>
 <table width="100%" align="center" >
       <tr style="HEIGHT:40px"> 
	       	<td class="t7" width="500" >
	      	        <b> ${sExcelFileName }</b>&nbsp;&nbsp;&nbsp; 
	      	        <input class="scbtn" type="button" value="点击下载" onclick="onclickDownload('${sExcelCdr }','${sExcelDate }','${sExcelFileName}','${isExists}','${type}','${callExtent}','${showtypetemp}');" />  
	       	</td>
       </tr>
       <tr>
       		<td height="1"   bgcolor="ECECF5"/>
       </tr>    
</table>

<br/>
<div align="left">
<c:if test ="${calltype == 1}" >
	<input style="width:80px ; height:30px; background:#87CEEB;"  id="callin"  type="button" value="呼入"  onclick="switchInOutBut(this,${type });" />
</c:if>	
<c:if test ="${calltype != 1}" >
	<input style="width:80px ; height:30px; background:#FFF5EE;"  id="callin"  type="button" value="呼入"  onclick="switchInOutBut(this,${type });" />
</c:if>

<c:if test ="${calltype == 2}" >	
	<input style="width:80px ; height:30px; margin-left:-1%; background:#87CEEB;"  id="callout"  type="button" value="呼出" onclick="switchInOutBut(this,${type });" />
</c:if>
<c:if test ="${calltype != 2}" >	
	<input style="width:80px ; height:30px; margin-left:-1%; background:#FFF5EE;"  id="callout"  type="button" value="呼出" onclick="switchInOutBut(this,${type });" />
</c:if>
</div>

<c:if test ="${calltype == '1'}">
<table class="tablelist">
	<thead>
	<tr>
		<th style="text-align:center;" colspan=${intChannelCount}><b><c:if test ="${type == 1}" >昨日</c:if><c:if test ="${type == 2}" >上周</c:if><c:if test ="${type == 3}" >上个月</c:if>渠道呼入统计</b></th>
	</tr>	    
	</thead>
	 <c:forEach items="${channels}" var="c" varStatus="s">
	 	<tbody>
	 	 <tr align="center" >
	 	 	<c:forEach items="${c}" var="m" varStatus="s">
				<td>${m}</td>
			</c:forEach>
		</tr>
		</tbody>
	</c:forEach>	 
	
</table>
<br/>
<table class="tablelist">
	<thead>
	<tr>
		<th style="text-align:center;" colspan=${intChannelCount}><b>经纪人渠道呼入统计</b></th>
	</tr>
	</thead>	    
	<c:forEach items="${channelInfo}" var="c" varStatus="s">
		<tbody>
		<tr>
		 	<c:forEach items="${c}" var="m" varStatus="s">
				<td>${m}</td>
			</c:forEach>	 
		</tr>
		</tbody>
	</c:forEach>
</table>
</c:if>

<c:if test ="${calltype == '2'}">
<table class="tablelist">
	<thead>
	<tr>
		<th style="text-align:center;" colspan=${outChannelCount}><b><c:if test ="${type == 1}" >昨日</c:if><c:if test ="${type == 2}" >上周</c:if><c:if test ="${type == 3}" >上个月</c:if>呼出统计</b></th>
	</tr>	    
	</thead>
	 <c:forEach items="${channelOuts}" var="c" varStatus="s">
	 	<tbody>
	 	 <tr>
	 	 	<c:forEach items="${c}" var="m" varStatus="s">
				<td>${m}</td>
			</c:forEach>
		</tr>
		</tbody>
	</c:forEach>	 
</table>
<br/>
<table class="tablelist">
	<thead>
	<tr>
		<th style="text-align:center;" colspan=${outChannelCount}><b>经纪人呼出统计</b></th>
	</tr>	    
	</thead>
	<c:forEach items="${channelOutInfo}" var="c" varStatus="s">
		<tbody>
		<tr align="center" >
			 	<c:forEach items="${c}" var="m" varStatus="s">
					<td>${m}</td>
			</c:forEach>	 
		</tr>
		</tbody>
	</c:forEach>
</table>
<br/><br/><br/><br /><br/><br/><br/><br /><br/><br/><br/><br /><br/><br/><br/><br /><br/><br/>
</c:if>
<br/>
<br/>
<br/>
<br />
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>
</body>
</html>

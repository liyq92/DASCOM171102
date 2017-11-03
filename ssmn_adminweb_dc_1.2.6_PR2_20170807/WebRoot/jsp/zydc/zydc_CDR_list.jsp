<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ page import="com.dascom.ssmn.util.Constants" %>
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
var bar_color = 'green';                            //进度条背景颜色 
var no_color="";                                     //解决浏览器兼容设置的无颜色变量 
var clear = ""; 

function cancel(){
	wBox.close();
}

function init(msg,zipfilename){
	
	if(msg != ''){
		wBox=jQuery("#wbox10").wBox({
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
 	var msisdn = document.getElementById("msisdn").value;   
 	var sstreamnum = document.getElementById("streamnum").value;
 	var sname = document.getElementById("name").value;
 	var clientnumber = document.getElementById("clientnumber").value;
 	var bdbox1 = document.getElementById("bdbox1");//事业部
 	var zonebox1 = document.getElementById("zonebox1");//战区
 	var areabox1 = document.getElementById("areabox1");//片区
 	var bagbox1 = document.getElementById("bagbox1");//行动组
	var channelbox = document.getElementById("channelbox");//渠道
	var ssmnnum = document.getElementById("ssmnnum").value;
	var calltype = document.getElementById("calltype");//呼入呼出
	var callremis = document.getElementById("callReMis");//已接/未接
	var recordcount = document.getElementById("recordCount").value;
 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseCDR;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl = "type=1&startdate="+startdate+"&starttime="+starttime+"&enddate="+enddate+"&endtime="+endtime+"&msisdn="+msisdn+"&streamnum="+sstreamnum+"&name="+sname+"&clientnumber="+clientnumber;
	vurl +="&ssmnnum="+ssmnnum;
	if(bdbox1.selectedIndex != 0)
		vurl +="&bdbox1="+bdbox1.value;
	if(zonebox1.selectedIndex != 0)
		vurl +="&zonebox1="+zonebox1.value;
	if(areabox1.selectedIndex != 0)
		vurl +="&areabox1="+areabox1.value;
	if(bagbox1.selectedIndex != 0)
		vurl +="&bagbox1="+bagbox1.value;
	if(channelbox.selectedIndex != 0)
		vurl +="&channelbox="+channelbox.value;
	if(calltype.selectedIndex !=0)
		vurl +="&calltype="+calltype.value;
	if(callremis.selectedIndex !=0)
		vurl +="&callReMis="+callremis.value;
	vurl +="&recordcount="+recordcount;

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

//点击选择查询条件各等级下拉菜单  
function changeHeadSelect(type){
    if(type == '1'){
        var opObj = document.getElementById('bdbox1'); 
	       var select_root=document.getElementById('zonebox1');    
   	       select_root.options.length=0;
   	       var defa = new Option("全部","");
     	   select_root.add(defa);
     	   
		   var select_root1=document.getElementById('areabox1');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("全部","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('bagbox1');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("全部","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
		      sendHeadRequest(type);
	        }
    }else if(type == '2'){
          var opObj = document.getElementById('zonebox1'); 
		   var select_root1=document.getElementById('areabox1');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("全部","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('bagbox1');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("全部","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
     	      sendHeadRequest(type);
     	   }
	    
    }else{
        var opObj = document.getElementById('areabox1'); 
     	var select_root2=document.getElementById('bagbox1');    
   	    select_root2.options.length=0;
   	    var defa2 = new Option("全部","");
     	select_root2.add(defa2);
	    if(opObj.selectedIndex!=0){
		   sendHeadRequest(type);
	    }
    }
}

function sendHeadRequest(type){
 	createXMLHttpRequest();
 	var opObj = document.getElementById('bdbox1'); 
 	var opObj1 = document.getElementById('zonebox1'); 
    var opObj2 = document.getElementById('areabox1'); 
 	XMLHttpReq.open("POST","${ctx}/selectlevel",true);
	XMLHttpReq.onreadystatechange = processHeadResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type="+type+"&bd="+opObj.value+"&warzone="+opObj1.value+"&area="+opObj2.value);
}

function processHeadResponse(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseHeadMessage();
		}else{
			alert(XMLHttpReq.status);
		}
	}
 }

 function parseHeadMessage()
{
  	var all=XMLHttpReq.responseText.split("#");  
  	var type = all[0];   
  	var select_root=document.getElementById('bdbox1');   
  	var defa;
  	if(type == '1'){
  	     select_root=document.getElementById('zonebox1');   
  	     select_root.options.length=0;
       	 defa = new Option("全部",""); 
  	}else if(type == '2'){
  	     select_root=document.getElementById('areabox1');  
  	     select_root.options.length=0;
       	 defa = new Option("全部","");   
  	}else{
  		select_root=document.getElementById('bagbox1');  
  	     select_root.options.length=0;
       	 defa = new Option("全部",""); 
  	}
  	
   	select_root.options.length=0;
    select_root.add(defa);
    for(var i=1; i<all.length-1; i++)
    {
        var xValue=all[i];
        var option=new Option(xValue,xValue);
        try{
            select_root.add(option);
        }catch(e){
        }
    }   
}

function check()
{
	var startdate = document.getElementById("startdate").value;
	var starttime = document.getElementById("starttime").value;
 	var enddate = document.getElementById("enddate").value;
 	var endtime = document.getElementById("endtime").value;
 	var msisdn = document.getElementById("msisdn").value;   
 	var sstreamnum = document.getElementById("streamnum").value;
 	var sname = document.getElementById("name").value;
 	var clientnumber = document.getElementById("clientnumber").value;
 	var bdbox1 = document.getElementById("bdbox1");//事业部
 	var zonebox1 = document.getElementById("zonebox1");//战区
 	var areabox1 = document.getElementById("areabox1");//片区
 	var bagbox1 = document.getElementById("bagbox1");//行动组
	var channelbox = document.getElementById("channelbox");//渠道
	var ssmnnum = document.getElementById("ssmnnum").value;
	var calltype = document.getElementById("calltype");//呼入呼出
	var callremis = document.getElementById("callReMis");//已接/未接

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
	}
}

//记录日志,听录音
function sendRequestLog(streamnumber,ssmnnumber){
 	createXMLHttpRequest();
 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseLog;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl = "type=5";
	if(streamnumber != '')
		vurl += "&streamnumber="+streamnumber+"&ssmnnumberstr="+ssmnnumber;
	
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

function InsertLog(streamnumber ,ssmnnuber)
{
	sendRequestLog(streamnumber,ssmnnuber);
	document.getElementById(streamnumber).style.color="#c3272b";
}
var vstreamnumber ;
function showRemarkBox(streamnum ,vremark)
{
	vstreamnumber =streamnum;
	wBox=jQuery("#wbox10").wBox({
	title: "添加备注",

	html: "<div style='width:380px;height:280px;'><textarea style='margin-top:10px;margin-left:10px;width:350px;height:186px;' id='remark' name='remark'>"+vremark+"</textarea>" +
		  "<br/>&nbsp;&nbsp;&nbsp;<font color='red'>注：输入限制512个字符(256个汉字)</font>"+
		  "<div style='margin-bottom:28px;margin-top:17px;'><table width='380' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>" + 
		  "<div style='text-align:center;'><input  style='margin-top:-18px;width:30%;' class='scbtn' type='button' value='保存' onclick='onSave();' /></div> </div>"  });
	wBox.showBox();
}

function onSave()
{
	var vremark = document.getElementById('remark').value;
	if(vremark == '')
	{
		alert('请输入备注内空!');
		return;
	}
	
	var sum = 0; 
	for (var i=0; i<vremark.length; i++)
	{ 
	    var c = vremark.charCodeAt(i); 
	    if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f))
		{ 
	    	sum++; 
	    }
	    else 
	    {     
	    	sum+=2; 
	    } 
	}
	if(sum >512)
	{
		alert('备注信息长度不能超过512个字符(256个汉字)!');
		return;
	}
	
	var startdate = document.getElementById("startdate").value;
	var starttime = document.getElementById("starttime").value;
 	var enddate = document.getElementById("enddate").value;
 	var endtime = document.getElementById("endtime").value;
 	var bdbox1 = document.getElementById("bdbox1");//事业部
 	var zonebox1 = document.getElementById("zonebox1");//战区
 	var areabox1 = document.getElementById("areabox1");//片区
 	var bagbox1 = document.getElementById("bagbox1");//行动组
	var channelbox = document.getElementById("channelbox");//渠道
	var msisdn = document.getElementById("msisdn").value;   
 	var sstreamnum = document.getElementById("streamnum").value;
 	var sname = document.getElementById("name").value;
 	var clientnumber = document.getElementById("clientnumber").value;
 	var ssmnnum = document.getElementById("ssmnnum").value;
	var calltype = document.getElementById("calltype");//呼入呼出
	var callremis = document.getElementById("callReMis");//已接/未接
	var vpage =document.getElementById("pagenumber").value;//当前页
 	
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
	}
		
	var vres = vremark.replace(/\n/g,"\\n");
	var vurl ="queryssmninfo.do?type=6&remark="+vremark+"&streamnumRe="+vstreamnumber+
	"&startdate="+startdate+"&starttime="+starttime+"&enddate="+enddate+"&endtime="+endtime
	+"&msisdn="+msisdn+"&streamnum="+sstreamnum+"&name="+sname+"&clientnumber="+clientnumber+
	"&ssmnnum="+ssmnnum;
	
	if(bdbox1.selectedIndex != 0)
		vurl +="&bdbox1="+bdbox1.value;
	if(zonebox1.selectedIndex != 0)
		vurl +="&zonebox1="+zonebox1.value;
	if(areabox1.selectedIndex != 0)
		vurl +="&areabox1="+areabox1.value;
	if(bagbox1.selectedIndex != 0)
		vurl +="&bagbox1="+bagbox1.value;
	if(channelbox.selectedIndex != 0)
		vurl +="&channelbox="+channelbox.value;
	if(calltype.selectedIndex !=0)
		vurl +="&calltype="+calltype.value;
	if(callremis.selectedIndex !=0)
		vurl +="&callReMis="+callremis.value;
	vurl +="&page="+vpage;
		
	document.location.href= vurl;
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
<form method="post" action="queryssmninfo.do?type=1" onsubmit="return check();" name="mainfm">
	<table width="100%" align="center">
		<tr style="height:40px;">
			<td class="t6" style="text-indent:10px; ">${businName }：</td>
			<td class="t7">
				<select class="select3" id='bdbox1' name='bdbox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(1)' <c:if test="${slevelid==6 || slevelid==5 || slevelid==4 || slevelid==3 }"> disabled="disabled" </c:if> >
					<option value=''>全部</option>
					<c:forEach items="${bdlist}" var="bd" varStatus="s">
						<option value='${bd }' <c:if test="${bdparam==bd}">selected='true'</c:if>>${bd }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${warName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='zonebox1' name='zonebox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(2)' <c:if test="${ slevelid==6 || slevelid==5 || slevelid==4 }"> disabled="disabled" </c:if> >
					<option value=''>全部</option>
					<c:forEach items="${wzlist}" var="wz" varStatus="s">
						<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${areaName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='areabox1' name='areabox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(3)' <c:if test="${slevelid==6 || slevelid==5 }"> disabled="disabled" </c:if>>
					<option value=''>全部</option>
					<c:forEach items="${arealist}" var="ar" varStatus="s">
						<option value='${ar }' <c:if test="${areaparam==ar}">selected='true'</c:if>>${ar }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${branName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='bagbox1' name='bagbox1' style='width:120px ; height:20px;' <c:if test="${slevelid==6 }"> disabled="disabled" </c:if>>
					<option value=''>全部</option>
					<c:forEach items="${baglist}" var="bag" varStatus="s">
						<option value='${bag }' <c:if test="${bagparam==bag}">selected='true'</c:if>>${bag }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr style="height:40px;">
		<td class="t6" style="text-indent:10px;">姓名：</td>
    	<td class="t7" style="width:120px;height:40px;">
    		<input style="width:148px;" class="scinput" id="name" name="name" value="${name}" type="text" size="20" />
    	</td>
		<td class="t6" style="text-indent:10px;">主号码：</td>
    	<td class="t7" style="width:120px;height:40px;">
    		<input style="width:148px;" class="scinput" id="msisdn" name="msisdn" value="${msisdn}" type="text" size="20" maxlength="11" />
    	</td>          	
  		<td class="t6" style="text-indent:10px;">副号码：</td>
    	<td class="t7" style="width:120px;height:40px;">
    		<input style="width:148px;" class="scinput" id="ssmnnum" name="ssmnnum" value="${ssmnnum}" type="text" size="20" maxlength="11" />
    	</td> 
    	<td class="t6" style="text-indent:10px;">客户电话：</td>
    	<td class="t7" style="width:120px;height:40px;" >
    		<input style="width:148px;" class="scinput" id="clientnumber" name="clientnumber" value="${clientnumber}" type="text" size="20" />
    	</td>
    	</tr>
    	<tr>
    	<td class="t6" style="text-indent:10px;">序号：</td>
    	<td class="t7" style="width:120px;height:40px;" >
    		<input style="width:148px;" class="scinput" id="streamnum" name="streamnum" value="${streamnum}" type="text" size="20" />
    	</td>
		<td class="t6" style="text-indent:10px;">渠道：</td>
    	<td class="t7">
	    	<select class="select3" id='channelbox' name='channelbox' style='width:120px ; height:20px;'>
				<option value=''>全部</option>
				<c:forEach items="${channels}" var="bag" varStatus="s">
					<option value='${bag.id }' <c:if test="${channelid==bag.id}">selected='true'</c:if>>${bag.name }</option>
				</c:forEach>
			</select>
		</td>
		<td class="t6" style="text-indent:10px;">呼入/呼出：</td>
    	<td class="t7">
	    	<select class="select3" id='calltype' name='calltype' style='width:120px ; height:20px;'>
				<option value=''>全部</option>
					<option value='1' <c:if test="${calltype=='1'}">selected='true'</c:if>>呼入</option>
					<option value='2' <c:if test="${calltype=='2'}">selected='true'</c:if>>呼出</option>
			</select>
		</td>
    	<td class="t6" style="text-indent:10px;">已接/未接：</td>
    	<td class="t7">
	    	<select class="select3" id='callReMis' name='callReMis' style='width:120px ; height:20px;'>
				<option value=''>全部</option>
					<option value='1' <c:if test="${callReMis=='1'}">selected='true'</c:if>>已接</option>
					<option value='0' <c:if test="${callReMis=='0'}">selected='true'</c:if>>未接</option>
			</select>
		</td>
    </tr>
    <tr style="height:40px;">
    	<td class="t6" style="text-indent:10px;" >起止时间：</td>
		<td class="t7" width="250" colspan="4" >
			&nbsp;<label style="margin-top:15px;">从</label>&nbsp;<input class="scinput" type="text" id="startdate"  name="startdate" value="${startdate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:120px"/> 日
			<input class="scinput" type="text" id="starttime" name="starttime" value="${starttime }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'HH'})" class="Wdate" style="width:50px"/> 时 &nbsp;到&nbsp;
			<input class="scinput" type="text" id="enddate" name="enddate" value="${enddate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startdate\')}',maxDate:'#F{$dp.$D(\'startdate\',{d:30})}'})" class="Wdate" style="width:120px"/> 日 
			<input class="scinput" type="text" id="endtime" name="endtime" value="${endtime }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'HH'})" class="Wdate" style="width:50px"/> 时
        </td>
        <td class="t7" colspan="3">
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
<c:if test="${calltype !='2'}">
<c:if test="${numchannelcounts >0}" >
<table class="tablelist1" align="left" style="border-collapse:collapse;margin-bottom:18px;" border="1" bordercolor="#3891a8">
	<thead>
	<tr>
		<th style="text-align:center;width:96px;font-size:14px;" colspan=${channelcountNumin} >渠道呼入统计</th>
	</tr>
	</thead>
	<c:forEach items="${channelcount}" var="c" varStatus="s">
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

<c:if test="${calltype !='1'}">
<c:if test="${numchannelOutCounts >0 }" >
<c:if test="${calltype !='2' }"><!-- 有呼入和呼出,加左边距 -->
<table class="tablelist" align="left" style="border-collapse:collapse;margin-left:57px; margin-bottom:18px;width:${channelcountNumout*84}px;" border="1" bordercolor="#3891a8">
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
<br/>
<br/>

<table class="tablelist" >
	<thead>   
		<tr> 
			<th><b>序号</b></th>
	        <th><b>经纪人姓名</b></th>
	        <th><b>员工编号</b></th>
	        <th><b>${branName }</b></th>
	        <th><b>主号码</b></th>
	        <th><b>副号码</b></th>
	        <!--   <c:if test="${isSecondMsisdn == '1'}">
	        	<th><b>第一联系人</b></th>
	        </c:if>-->
	        <th><b>客户电话</b></th>
	        <th><b>渠道</b></th>
	        <th><b>通话类型</b></th>
	        <th><b>通话时间</b></th>
	        <th><b>通话时长</b></th>
	        <%if(authlist != null && (authlist.contains("listenOnline")) && (authlist.contains("queryssmninfo"))){ %>
	       		<th><b>在线收听</b></th>
	        <%} %>
	        <%if(authlist != null && (authlist.contains("cdrDownload")) && (authlist.contains("queryssmninfo"))){ %>
	        	<th><b>下载</b></th>
	        	<c:if test="${isAddRemark ==1}">
		 		<th><b>备注</b></th>
		 	</c:if>
	        <%} %>
	        
	 	</tr>
 	</thead> 
 	<c:forEach items="${clist}" var="u" varStatus="s">
 		<tbody>
	 		<tr>
	 			<td>${u.streamNumber}</td> 
	 			<td>${u.username}</td> 
	 			<td>${u.empno}</td> 
	 			<td>${u.branchactiongroup}</td> 
	 			<td>${u.msisdn}</td> 
	 			<td>${u.ssmnnumber}</td> 
	 			<!--<c:if test="${isSecondMsisdn == '1'}">
	 				<td>${u.firstMsisdn}</td>
	 			</c:if>-->
	 			<td>${u.clientnumber}</td> 
	 			<td>${u.channelname}</td>
	 			<td>${u.calltype}</td>
	 			<td>${u.callstarttime}</td>
	 			<td height="20">${u.strCallDuration}</td>
	 			
		 			 <%if(authlist != null && (authlist.contains("listenOnline")) && (authlist.contains("queryssmninfo"))){ %>
			 			<td>
				 			 <c:if test="${u.fileName != null && u.fileName != '' && u.filepathOnline != null && u.filepathOnline != ''}"> 
				 			 	 <c:if test="${u.isRead == 1}">
				 			 	 	<input class="scbtn2"  type="button" id="${u.streamNumber}" style="color:#c3272b;" onclick="div_player.Filename='${u.filepathOnline}';InsertLog('${u.streamNumber}','${u.ssmnnumber}');" value="在线收听" />			 	
				 			 	 </c:if>
				 			 	  <c:if test="${u.isRead == 0}">		 	
				 			 	 	 <input class="scbtn2" id="${u.streamNumber}"  type="button" value="在线收听" onclick="div_player.Filename='${u.filepathOnline}';InsertLog('${u.streamNumber}','${u.ssmnnumber}');"  />  
				 			 	 </c:if>
				 			 </c:if>
				 			 <c:if test="${(u.fileName != null && u.fileName != '' ) && ( u.filepathOnline == null || u.filepathOnline == '' )}"> <!-- 必须是有录音文件，如果没有录音文件，则不需要显示 -->
				 			 	<!--  <a href="javascript:playRecord('${u.filepathOnline}');" ><u>在线收听</u></a> -->
				 			 	 文件不存在	<!--${u.fileName}   {u.filepathOnline}-->
				 			 </c:if>
			 			</td>  
		 			 <%} %>
		 			
		 			 <%if(authlist != null && (authlist.contains("cdrDownload")) && (authlist.contains("queryssmninfo"))){ %>
			 			 <td>
			 			 <c:if test="${u.fileName != null && u.fileName != '' && u.filepathOnline != null && u.filepathOnline != ''}"> 
			 			 	<input class="scbtn3" type="button" onclick="download('${u.filepath}');" value="下 载"/>
			 			 </c:if>
			 			 <c:if test="${u.fileName == null || u.fileName == '' || u.filepathOnline == null || u.filepathOnline == ''}"> 
				 			 	<!--  <a href="javascript:playRecord('${u.filepathOnline}');" ><u>在线收听</u></a> -->
				 			 	<!--   文件不存在!	-->		 	
				 			 </c:if>
			 			 </td>
			 			 
			 			 <td>
			 			  	<c:if test="${u.filepathOnline != '' && isAddRemark ==1}">
			 			  		<c:if test="${u.remarkCount ==1}">
			 						<input class="scbtn3" type="button" style="color:#c3272b" onclick="showRemarkBox('${u.streamNumber}','${u.remark}')" value="备 注" />
			 					</c:if>
			 					<c:if test="${u.remarkCount ==0}">
			 						<input class="scbtn3" type="button" onclick="showRemarkBox('${u.streamNumber}','${u.remark}')" value="备 注" />
			 					</c:if>
		 					</c:if>
		 				</td>
		 			<%} %>
	 		</tr>
 		</tbody>
 	</c:forEach>
</table>

<form method="post" action="queryssmninfo.do?type=1" name="pageForm">
<input type="hidden" name="msisdn" value="${msisdn}" />
<input type="hidden" name="streamnum" value="${streamnum}" />
<input type="hidden" name="name" value="${name}" />
<input type="hidden" name="startdate" value="${startdate}" />
<input type="hidden" name="starttime" value="${starttime}" />
<input type="hidden" name="enddate" value="${enddate}" />
<input type="hidden" name="endtime" value="${endtime}" />
<input type="hidden" name="clientnumber" value="${clientnumber}" />
<input type="hidden" name="ssmnnum" value="${ssmnnum}" />
<input type="hidden" name="channelbox" value="${channelid}" />
<input type="hidden" name="bdbox1" value="${bdparam}" />
<input type="hidden" name="zonebox1" value="${zoneparam}" />
<input type="hidden" name="areabox1" value="${areaparam}" />
<input type="hidden" name="bagbox1" value="${bagparam}" />
<input type="hidden" name="calltype" value="${calltype}" />
<input type="hidden" name="callReMis" value="${callReMis}" />
<input type="hidden" id="recordCount" value="${recordCount}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="queryssmninfo.do?type=1&page="/>
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

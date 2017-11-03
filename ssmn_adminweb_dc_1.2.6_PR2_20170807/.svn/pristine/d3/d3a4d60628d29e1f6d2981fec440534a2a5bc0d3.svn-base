<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script language="JavaScript" type="text/JavaScript">
<!--
var wBox;
//var jq=jQuery.noConflict();

<!--下面三个值为查询条件，由于通过js传递有问题，所以定义成全局的了 -->
var selectMsisdn;
var selectname;
var selectssmnnumber;
var varchannelname;
var oldmsisdn;
var switchuid;
var switchssmnnumber;
var selectsempno;
 
function cancel(){
	wBox.close();
}

function init(msg,stype,oldmsisdn,switchtomsisdn,switchtossmnnumber,switchtochannelname,switchusername,stmsisdn,suid,switchtochannelId,selectName,selectSsmnNumber,selectsempno){

	//如果type== 3,显示确定绑定消息
	if(stype == '3' && msg == '')
	{
		selectMsisdn=stmsisdn;
		selectname = selectName;
		selectssmnnumber = selectSsmnNumber;
		selectsempno = selectsempno

		wBox=jQuery("#wbox10").wBox({
		title: "",
		html: "<div style='width:300px;height:150px;'><div style='text-align=left;margin-left:30px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>经纪人:"+switchusername+"</div><table style='margin-top:5px;margin-left:30px;width:80%;'><tr><td>主号码：</td><td>副号码：</td><td>渠道：</td></tr><tr><td>"+switchtomsisdn+"</td><td>"+switchtossmnnumber+"</td><td>"+switchtochannelname+"</td></tr></table>"+
		"<div style='margin-bottom:5px;margin-top:10px;'><table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div> <div style='text-align:center;font-family:Arial;font-size:13px;color:#333333;'>是否确认进行换绑操作?<div> <input  style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='取消' onclick='cancel();' />&nbsp;&nbsp;&nbsp;&nbsp;<input  style='margin-top:5px;margin-left:10px;width:30%;' type='button' value='确定' onclick='switchBind("+oldmsisdn+","+suid+","+switchtossmnnumber+","+switchtomsisdn+","+switchtochannelId+");' /> </div>"
		});
		wBox.showBox();
		
	}else	
	{
		if(msg != ''){
			wBox=jQuery("#wbox10").wBox({
			title: "返回结果",
			html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
			});
			wBox.showBox();
		}
	}
}

function returntossmnnumber(selectssmnnum,selectbdbox1,selectzonebox1,selectareabox1,selectbagbox1,pagetemp,selectstatusbox,selecttype)
{
	var vurl = "modifyssmnnumber.do?method=list";

	if(selectssmnnum != '')
		vurl+="&ssmnnum="+selectssmnnum;
	if(selectbdbox1 != '')
		vurl+="&bdbox1="+selectbdbox1;
	if(selectzonebox1 != '')
		vurl+="&zonebox1="+selectzonebox1;
	if(selectareabox1 != '')
		vurl+="&areabox1="+selectareabox1;
	if(selectbagbox1 != '')
		vurl+="&bagbox1="+selectbagbox1;
	if(pagetemp != '')
		vurl +="&page="+pagetemp;
	if(selectstatusbox != '')
		vurl +="&statusbox="+selectstatusbox;
	if(selecttype !='')
		vurl +="&enmtype="+selecttype;
	document.location.href=vurl;

}

//-->
</script>
</head>
<body topmargin="0" onLoad="init('${msg}','${stype}','${oldmsisdn}','${switchtomsisdn}','${switchtossmnnumber}','${switchtochannelname}','${switchusername}','${msisdn}','${suid}','${switchtochannelId}','${name}','${ssmnnumber}','${sempno}');">
<%java.util.List authlist = (java.util.List)request.getAttribute("authMethodList");%>
 <div class="place">
 	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">副号码管理</a></li>
		<li><a href="#">业主副号码详情</a></li>
	</ul>
</div>




<div class="formbody" style="width:1100px;">
<input type="hidden" id="sempno" name="sempno" value="${sempno}" />
<table class="tablelist">	
	<thead> 
	 	<tr>
			<th colspan="6" style="text-align:center;"><b>当前状态</b></th>
		</tr>	    
		<tr> 
	        <th><b>副号码</b></th>
	        <th><b>业主电话</b></th>
	        <th><b>申请人姓名</b></th>
	        <th><b>申请人手机号</b></th>
	        <th><b>申请人所属行动组</b></th>
	        <th><b>绑定时间</b></th>
	   	</tr>
   	</thead>
 	<c:forEach items="${sdrlist}" var="u" varStatus="s">
 	<tbody>
		<tr>
			<td>${u.ssmnnumber}</td>
			<td>${u.ownerMsisdn}</td> 
			<td>${u.name}</td> 
			<td>${u.agentMsisdn}</td> 
			<td>${u.branchactiongroup}</td> 
			<td><fmt:formatDate value="${u.createline}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
		</tr>
	</tbody>
 	</c:forEach>
</table>
<br/>

<table class="tablelist">
	<thead>
	<tr>
		<th colspan="7" style="text-align:center;"><b>历史信息</b></th>
	</tr>	    
	<tr> 
        <th><b>副号码</b></th>
        <th><b>业主电话</b></th>
        <th><b>申请人姓名</b></th>
        <th><b>申请人手机号</b></th>
        <th><b>申请人所属行动组</b></th>
        <th><b>绑定时间</b></th>
         <th>解绑时间</th>
   	</tr>
   	</thead>
 	<c:forEach items="${sdrdellist}" var="u" varStatus="s">
 		<tbody>
 		<tr>
 			<td>${u.ssmnnumber}</td>
 			<td>${u.ownerMsisdn}</td>
 			<td>${u.name}</td> 
 			<td>${u.agentMsisdn}</td> 
 			<td>${u.branchactiongroup}</td> 
 			<td><fmt:formatDate value="${u.createline}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
 		 	<td><fmt:formatDate value="${u.deadline}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
 		</tr>
 		</tbody>
 	</c:forEach>
</table>

<br/>

<div align="center"><input class="scbtn" type="button" value="返回"  onClick="returntossmnnumber('${selectssmnnum}','${selectbdbox1}','${selectzonebox1}','${selectareabox1}','${selectbagbox1}','${pagetemp }','${selectstatusbox }','${selecttype }');"  /></div>
<!--<div align="center"><input class="btnstyle" onmouseout="this.style.backgroundImage='url(images/btnbg.png)';" onmouseover="this.style.backgroundImage='url(images/btnbg2.png)';" type="button" value="返回" onClick="gobackbut();"   />-->

<br/>
<br/>
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>

</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>
<title>Untitled Document</title>

<script language="JavaScript" type="text/JavaScript">
<!--
var wBox;
var jq=jQuery.noConflict();
 

function cancel(){
	wBox.close();
}

function init(msg){
	if(msg != ''){
		wBox=jq("#wbox10").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
}

function exportInfo()
{
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
 	var streamnum = document.getElementById("streamnum").value;
 	var sname = document.getElementById("name").value;
 	var ssmnnum = document.getElementById("ssmnnum").value;
 	var clientnumber = document.getElementById("clientnumber").value;
 	 	var bdbox1 = document.getElementById("bdbox1");//事业部
 	var zonebox1 = document.getElementById("zonebox1");//战区
 	var areabox1 = document.getElementById("areabox1");//片区
 	var bagbox1 = document.getElementById("bagbox1");//行动组
	var channelbox = document.getElementById("channelbox");//渠道
	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseCDR;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl= "type=2&startdate="+startdate+"&starttime="+starttime+"&enddate="+enddate+"&endtime="+endtime+"&msisdn="+msisdn+"&streamnum="+streamnum+"&name="+sname+"&clientnumber="+clientnumber+"&ssmnnum="+ssmnnum;
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
	var ssmnall=XMLHttpReq.responseText;

	if(ssmnall == '')//导出失败
	{
		alert('生成报表失败!');
	}
	else//成功，把名称反回来
	{
		var sre = "${ctx}/exportrecordFiles/"+ssmnall;
		document.location=sre;
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
 	var streamnum = document.getElementById("streamnum").value;
 	var sname = document.getElementById("name").value;
 	var ssmnnum = document.getElementById("ssmnnum").value;
 	var clientnumber = document.getElementById("clientnumber").value;
 	var bdbox1 = document.getElementById("bdbox1");//事业部
 	var zonebox1 = document.getElementById("zonebox1");//战区
 	var areabox1 = document.getElementById("areabox1");//片区
 	var bagbox1 = document.getElementById("bagbox1");//行动组
	var channelbox = document.getElementById("channelbox");//渠道

 	if(startdate == '' && starttime=='' && enddate=='' && endtime=='' &&
	msisdn=='' && sname=='' && streamnum=='' && clientnumber=='' && bdbox1.selectedIndex==0 && 
	zonebox1.selectedIndex==0 && areabox1.selectedIndex==0 && bagbox1.selectedIndex==0 &&
	channelbox.selectedIndex==0 && ssmnnum=='' )
	{
		alert('请输入查询条件');
		return;
	}
}

//-->
</script>
</head>
<style type="text/css">
	div.test{
	width:160px;
	height:25px;
	padding:5px;
	overflow:hidden;
	text-overflow:ellipsis;
	white-space:nowrap;
	}
</style>
<body topmargin="0" onLoad="init('${msg}');">
<div style="width:1100px;" align="center">
 <div id="title" style="height:20px" align="left"><img src="images/500000002.gif" width="10" height="11" />&nbsp;当前位置：短信查询</div>	
 	<hr/>
 	<br/>
<form method="post" action="queryssmninfo.do?type=2"  onsubmit="return check();" name="mainfm">
<IFRAME id="CalFrame"  style="DISPLAY: none; Z-INDEX: 100; WIDTH: 148px; POSITION: absolute;HEIGHT: 194px; left: 42px; top: 50px "
 marginWidth="0" marginHeight="0" src="scripts/calendar/calendar.htm" frameBorder="0"  noResize scrolling="no"></IFRAME>
<table width="1000" align="center">
 <tr>
		<td class="t6" style="text-indent:10px; ">事业部：</td>
		<td class="t7"><select id='bdbox1' name='bdbox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(1)' <c:if test="${slevelid==6 || slevelid==5 || slevelid==4 || slevelid==3 }"> disabled="disabled" </c:if> >
			<option value=''>全部</option>
			<c:forEach items="${bdlist}" var="bd" varStatus="s">
				<option value='${bd }' <c:if test="${bdparam==bd}">selected='true'</c:if>>${bd }</option>
			</c:forEach>
		</select></td>
    	<td class="t6" style="text-indent:20px;">战区：</td>
    	<td class="t7"><select id='zonebox1' name='zonebox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(2)' <c:if test="${ slevelid==6 || slevelid==5 || slevelid==4 }"> disabled="disabled" </c:if> >
			<option value=''>全部</option>
			<c:forEach items="${wzlist}" var="wz" varStatus="s">
				<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
			</c:forEach>
		</select></td>
    	<td class="t6" style="text-indent:20px;">片区：</td>
    	<td class="t7"><select id='areabox1' name='areabox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(3)' <c:if test="${slevelid==6 || slevelid==5 }"> disabled="disabled" </c:if>>
			<option value=''>全部</option>
			<c:forEach items="${arealist}" var="ar" varStatus="s">
				<option value='${ar }' <c:if test="${areaparam==ar}">selected='true'</c:if>>${ar }</option>
			</c:forEach>
		</select></td>
    	<td class="t6" style="text-indent:10px;">行动组：</td>
    	<td class="t7"><select id='bagbox1' name='bagbox1' style='width:120px ; height:20px;' <c:if test="${slevelid==6 }"> disabled="disabled" </c:if>>
			<option value=''>全部</option>
			<c:forEach items="${baglist}" var="bag" varStatus="s">
				<option value='${bag }' <c:if test="${bagparam==bag}">selected='true'</c:if>>${bag }</option>
			</c:forEach>
		</select></td>
		
	</tr>
	<tr>
		<td class="t6" style="text-indent:20px;">渠道：</td>
    	<td class="t7"><select id='channelbox' name='channelbox' style='width:120px ; height:20px;'>
			<option value=''>全部</option>
			<c:forEach items="${channels}" var="bag" varStatus="s">
				<option value='${bag.id }' <c:if test="${channelid==bag.id}">selected='true'</c:if>>${bag.name }</option>
			</c:forEach>
		</select></td>
		<td class="t6" style="text-indent:20px;">姓名：</td>
    	<td class="t7" width="120" ><input style='width:120px ;' id="name" name="name" value="${name}" type="text" size="20" maxlength="11" /></td>
		<td class="t6" style="text-indent:10px;">主号码：</td>
    	<td class="t7" width="120"><input style='width:120px ;' id="msisdn" name="msisdn" value="${msisdn}" type="text" size="20" maxlength="11" /></td>          	
  		<td class="t6" style="text-indent:10px;">副号码：</td>
    	<td class="t7" width="120"><input style='width:120px ;' id="ssmnnum" name="ssmnnum" value="${ssmnnum}" type="text" size="20" maxlength="11" /></td> 
    	</tr>
    	<tr>
    	<td class="t6" style="text-indent:0px;">客户电话：</td>
    	<td class="t7" width="120" ><input style='width:120px ;' id="clientnumber" name="clientnumber" value="${clientnumber}" type="text" size="20" /></td>
    	<td class="t6" style="text-indent:20px;">序号：</td>
    	<td class="t7" width="120" ><input style='width:120px ;' id="streamnum" name="streamnum" value="${streamnum}" type="text" size="20" maxlength="11" /></td>
    	<td class="t6" style="text-indent:00px;" >起止时间：</td>
		<td class="t7" width="250" colspan="6" >
		&nbsp;从&nbsp;<input type="text" id="startdate"  name="startdate" value="${startdate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:120px"/> 日
		<input type="text" id="starttime" name="starttime" value="${starttime }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'HH'})" class="Wdate" style="width:50px"/> 时 &nbsp;<br/>&nbsp;到&nbsp;<input type="text" id="enddate" name="enddate" value="${enddate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:120px"/> 日 <input type="text" id="endtime" name="endtime" value="${endtime }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'HH'})" class="Wdate" style="width:50px"/> 时

        </td>
        
           	
    </tr>
    
    <tr>
     <td class="t7" style="text-align:center" colspan="8">
  			<input class="btnstyle" onmouseout="this.style.backgroundImage='url(images/btnbg.png)';" onmouseover="this.style.backgroundImage='url(images/btnbg2.png)';" type="submit" value="查询" />&nbsp; &nbsp; &nbsp;  	
  		</td>
  	</tr>
    <tr>
        <td height="6" colspan="8" bgcolor="ECECF5"></td>
    </tr> 
    <tr><td>&nbsp;</td></tr>
</table>


</form>

<c:if test="${recordCount >0}">

<table align="left" style="border-collapse:collapse;font-size: 12px; margin-left:57px; margin-bottom:28px;" border="1" bordercolor="#3891a8">
	<tr>
		<td class="t8" colspan=${msgchannelcountnum} >渠道短信统计</td>
	</tr>
	
	<tr align="center" >
		<td height="20" bgcolor="#F7F7FB" width="70">渠道</td>
		<c:forEach items="${msgchannelcount}" var="c" varStatus="s">
			<td  height="20" bgcolor="#F7F7FB" width="70" >${c.channelname } </td>
		</c:forEach>
	</tr>
	
	<tr align="center">
		<td height="20" bgcolor="#F7F7FB">数量</td>
		<c:forEach items="${msgchannelcount}" var="c" varStatus="s">
			<td height="20" bgcolor="#F7F7FB" >${c.channelCount } </td>
		</c:forEach>
	</tr>
	
</table>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<table width="1000" align="center" style="border-collapse:collapse;font-size: 12px;" border="1" bordercolor="#3891a8">
	<tr>
		<td class="t8" colspan="11"><b>短信查询</b></td>
	</tr>	    
	<tr> 
		<td class="t8" width="70"><b>序号</b></td>
		<!--  <td class="t8" width="70"><b>接收日期</b></td>-->
		<td class="t8" width="70"><b>接收时间</b></td>
        <td class="t8" width="70"><b>经纪人姓名</b></td>
        <td class="t8" width="70"><b>员工编号</b></td>
        <td class="t8" width="70"><b>所属行动组</b></td>
        <td class="t8" width="80"><b>主号码</b></td>
        <td class="t8" width="80"><b>副号码</b></td>
        <td class="t8" width="80"><b>客户电话</b></td>
        <td class="t8" width="50"><b>渠道</b></td>
        <td class="t8"><b>短信内容</b></td>
 	</tr>
 	<c:forEach items="${clist}" var="u" varStatus="s">
 		<tr	align="center">
 			<td height="20" bgcolor="#F7F7FB">${u.streamNumber}</td>
 			<!--<td height="20" bgcolor="#F7F7FB">${u.yesterday}</td>-->
 			<td height="20" bgcolor="#F7F7FB">${u.rcvmsgtime}</td> 
 			<td height="20" bgcolor="#F7F7FB">${u.username}</td> 
 			<td height="20" bgcolor="#F7F7FB">${u.empno}</td>
 			<td height="20" bgcolor="#F7F7FB">${u.groupname}</td> 
 			<td height="20" bgcolor="#F7F7FB">${u.msisdn}</td> 
 			<td height="20" bgcolor="#F7F7FB">${u.ssmnnumber}</td> 
 			<td height="20" bgcolor="#F7F7FB">${u.clientnumber}</td> 
 			<td height="20" bgcolor="#F7F7FB">${u.channelname}</td> 
 			<td height="20" bgcolor="#F7F7FB">${u.msgcontent}</td> 
 		</tr>
 	</c:forEach>
</table>

<form method="post" action="queryssmn.do" name="pageForm">
<input type="hidden" name="msisdn" value="${msisdn}" />
<input type="hidden" name="streamnum" value="${streamnum}" />
<input type="hidden" name="name" value="${name}" />
<input type="hidden" name="startdate" value="${startdate}" />
<input type="hidden" name="starttime" value="${starttime}" />
<input type="hidden" name="enddate" value="${enddate}" />
<input type="hidden" name="endtime" value="${endtime}" />
<input type="hidden" name="clientnumber" value="${clientnumber}" />
<input type="hidden" name="channelbox" value="${channelid}" />
<input type="hidden" name="bdbox1" value="${bdparam}" />
<input type="hidden" name="zonebox1" value="${zoneparam}" />
<input type="hidden" name="areabox1" value="${areaparam}" />
<input type="hidden" name="bagbox1" value="${bagparam}" />
<input type="hidden" name="ssmnnum" value="${ssmnnum}" />
<div style="width:1100px;" align="right" >
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="queryssmninfo.do?type=2&page="/>
</div>
</form>
<br/>
<br/>

 <div style="width:1000px;" align="right" > <input style="width:120px ; height:30px"  type="button" value="短信记录导出" onClick="exportInfo()" /></div>  	
</c:if>

<br/>
<br/>
</div>
</body>
</html>

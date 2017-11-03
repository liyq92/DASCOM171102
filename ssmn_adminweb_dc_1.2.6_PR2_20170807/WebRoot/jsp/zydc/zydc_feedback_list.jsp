<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>
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
<!--
var wBox;
var vuid;
 
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
 	var sname = document.getElementById("name").value;
 	var ssmnnum = document.getElementById("ssmnnum").value;
 	var clientnumber = document.getElementById("clientnumber").value;
	
 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseCDR;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type=2&startdate="+startdate+"&starttime="+starttime+"&enddate="+enddate+"&endtime="+endtime+"&msisdn="+msisdn+"&name"+sname+"&clientnumber="+clientnumber+"&ssmnnum="+ssmnnum);
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

function addfeedback()
{
	var vcontent = document.getElementById('feedcontent').value;
	var vstarttime = document.getElementById('startdate').value;
	var vendtime = document.getElementById('enddate').value;
	document.location.href="searchfeedback.do?type=addview&feedcontent="+vcontent+"&startdate="+vstarttime+"&enddate="+vendtime;
}

function delefeedback(uid)
{
	vuid = uid;
	wBox=jQuery("	#wbox10").wBox({
		title: "",
		html: "<div style='width:350px;height:120px;'>"+
		"<table style='margin-top:5px;margin-left:40px;width:90%;'>"+
		"<tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+
		"<td style='font-size:14px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;确认是否删除?<br><br></td> </tr><br>  </table> "+
		"<div style='margin-bottom:10px;'>"+
		"<table width='350' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel' style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' /><input class='sure' style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='deletecheck();' /> </div>"
		});
		wBox.showBox();
}

function deletecheck()
{

	document.location.href='searchfeedback.do?type=delete&uid='+vuid;

}

//-->
</script>
</head>

<body topmargin="0" onLoad="init('${msg}');">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">信息反馈</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<form method="post" action="searchfeedback.do" name="mainfm">
<table width="100%" align="center">
	<tr style="height:40px;">
		<td class="t6" style="width:100px;text-indent:10px;">反馈内容：</td>
    	<td class="t7" style="width:190px;height:40px;">
    		<input class="scinput" style='width:120px ;' id="feedcontent" name="feedcontent" value="${feedcontent}" type="text" size="20" />
    	</td>
    	<td class="t6" style="width:100px;text-indent:10px;" >反馈时间：</td>
		<td class="t7" width="400" colspan="5">从
          	  <input class="scinput" type="text" id="startdate" name="startdate" value="${startdate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:150px"/>
          	  到 <input class="scinput" type="text" id="enddate" name="enddate" value="${enddate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:150px"/>
        </td>        	
    </tr>
    <tr style="height:40px;">
     <td class="t7" style="text-align:center" colspan="8">
     	<input class="scbtn" type="button" onclick="addfeedback();" value="反馈" />&nbsp; &nbsp; &nbsp;  	
  		<input class="scbtn" type="submit" value="查询" />&nbsp; &nbsp; &nbsp;  	
  	</td>
  	</tr>
    <tr>
        <td height="6" colspan="8" bgcolor="ECECF5"></td>
    </tr> 
    <tr><td>&nbsp;</td></tr>
</table>
</form>

<table class="tablelist" style="border-collapse:collapse;" border="1">
    <thead>
		<tr> 
			<th><b>${branName }</b></th>
			<th><b>姓名</b></th>
	        <th><b>手机号</b></th>
	        <th><b>反馈内容</b></th>
	        <th><b>反馈时间</b></th>
	        <th><b>操作</b></th>
	 	</tr>
 	</thead>
 	<c:forEach items="${flist}" var="u" varStatus="s">
 		<tbody>
 		<tr>
 			<td>${u.branchactiongroup}</td>
 			<td>${u.name}</td> 
 			<td>${u.msisdn}</td> 
 			<td>${u.feedbackcontent}</td> 
 			<td>${u.strfeedbackTime}</td> 
 			<td><a href="#" style="color:#056dae;" onclick="delefeedback('${u.id }');" >删除</a></td> 
 			
 		</tr>
 		</tbody>
 	</c:forEach>
</table>

<form method="post" action="searchfeedback.do" name="pageForm">
<input type="hidden" name="feedcontent" value="${feedcontent}" />
<input type="hidden" name="startdate" value="${startdate}" />
<input type="hidden" name="starttime" value="${enddate}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="searchfeedback.do?page="/>
</form>
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

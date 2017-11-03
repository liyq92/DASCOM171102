<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>

<script type="text/javascript">
$(document).ready(function(e) {
    $(".select1").uedSelect({
		width : 345			  
	});
	$(".select2").uedSelect({
		width : 167  
	});
	$(".select3").uedSelect({
		width : 248
	});
});
</script>
<script language="JavaScript" type="text/JavaScript">
<!--
var wBox;

var ssmnall;//取到的副号码
var vbusinName;
var vwarName;
var vareaName;
var vbranName;
var visSecondMsisdn;//是否有第二联系人
var visHaveSsmnNum;//是否分配副号码

function cancel(){
	wBox.close();
}
 
//各等级下拉菜单  
var XMLHttpReq = false;
function changeSelect(type,branName){
  // alert(type);
    var fm=document.forms[0];
    
    if(type == '1'){
        var opObj = document.forms[0].businessdepartment;
	    //alert(opObj.value);
	       var select_root=document.getElementById('warzone');    
   	       select_root.options.length=0;
   	       var defa = new Option("----请选择----","");
     	   select_root.add(defa);
     	   
		   var select_root1=document.getElementById('area');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("----请选择----","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('branchactiongroup');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("----请选择----","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
		      sendRequest(type);
	        }
    }else if(type == '2'){
          var opObj = document.forms[0].warzone;
	      //alert(opObj.value);
		   var select_root1=document.getElementById('area');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("----请选择----","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('branchactiongroup');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("----请选择----","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
     	      sendRequest(type);
     	   }
	    
    }else if(type == '3'){
        var opObj = document.forms[0].area;
	    //alert(opObj.value);
     	var select_root2=document.getElementById('branchactiongroup');    
   	    select_root2.options.length=0;
   	    var defa2 = new Option("----请选择----","");
     	select_root2.add(defa2);
	    if(opObj.selectedIndex!=0){
		   sendRequest(type);
	    }
    }else {
   		 vbranName = branName;
    	 
    }
    
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

function sendRequest(type){
 	createXMLHttpRequest();
 	var opObj = document.forms[0].businessdepartment;
 	var opObj1 = document.forms[0].warzone;
 	var opObj2 = document.forms[0].area;

 	XMLHttpReq.open("POST","${ctx}/selectlevel",true);
	XMLHttpReq.onreadystatechange = processResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type="+type+"&bd="+opObj.options[opObj.selectedIndex].value+"&warzone="+opObj1.options[opObj1.selectedIndex].value+"&area="+opObj2.options[opObj2.selectedIndex].value);
}

function processResponse(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessage();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }

 function parseMessage()
{
  	var all=XMLHttpReq.responseText.split("#");  
  	var type = all[0];   
  	var select_root=document.getElementById('branchactiongroup');   
  	var defa;
  	if(type == '1'){
  	     select_root=document.getElementById('warzone');   
  	     select_root.options.length=0;
       	 defa = new Option("----请选择----",""); 
  	}else if(type == '2'){
  	     select_root=document.getElementById('area');  
  	     select_root.options.length=0;
       	 defa = new Option("----请选择----","");   
  	}else{
  	     select_root.options.length=0;
       	 defa = new Option("----请选择----",""); 
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

function changeChannel()
{
	var branchactiongroup = document.getElementById('branchactiongroup');
	if(branchactiongroup.value == '')
	{
		alert('请选择行动组!');
		var vchannel = document.getElementById('channel');
		vchannel.selectedIndex =0;
		return false;
	}
	//选择副号码
	sendRequestEN();

}

function sendRequestEN(){
 	createXMLHttpRequest();
 	var opObj = document.getElementById("branchactiongroup").value;
 	var opObjchannel = document.getElementById("channel").value;
 	if(opObj == '')
 	{
 		alert('请选择'+vbranName);
 		return;
 	}
 	if(opObjchannel == '')
 	{
 		alert('请选择渠道!');
 		return ;
 	}
 	
 	XMLHttpReq.open("POST","${ctx}/selectenablenumber",true);
	XMLHttpReq.onreadystatechange = processResponseEN;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("branchactiongroup="+opObj+"&type=1&channelid="+opObjchannel);
}

function processResponseEN(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageEN();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }
 
  function parseMessageEN()
{
	ssmnall=XMLHttpReq.responseText;
	if(ssmnall == '' )
		alert('号码池中没有可用的副号码!');
	
	//if反回11个0默认副号码，则隐藏副号码框（A+渠道不分配副号码的)
	if(ssmnall.indexOf('00000000000')>=0)
	{
		visHaveSsmnNum ='1'; //不需要分配副号码
		document.getElementById("selectSsmnnumber").style.display="none";
	}
	else
	{
		visHaveSsmnNum ='0'; //需要分配副号码
		document.getElementById("selectSsmnnumber").style.display="table-row";
		document.getElementById("ssmn").value=ssmnall;
	}
}

function check(businName,warName,areaName,branName,isSecondMsisdn)
{
	vbusinName =businName;
	vwarName = warName;
 	vareaName = areaName;
	vbranName = branName;
	visSecondMsisdn =isSecondMsisdn;
	sendRequestNU();
}

//判断副号码是否有效
function sendRequestNU(){
 	createXMLHttpRequest();
 	var ssmnval = '';
 	if(visHaveSsmnNum == '1')//如果开关打开,并且是A+渠道，不分配副号码（副号码框不存在，给默认的00000000000
 		ssmnval ='00000000000';
 	else
 		ssmnval =document.getElementById('ssmn').value;
 	
 	var opObj = document.getElementById("branchactiongroup").value;
 	var smsisdn = document.getElementById("msisdn").value;
 	var sempno = document.getElementById("aempno").value;
 	var schannelid = document.getElementById('channel').value;
 	//第二联系人
	var vsecondmsisdn ='';
	if(visSecondMsisdn == '1')
	 	vsecondmsisdn =document.getElementById("secondMsisdn").value;
 	
 	if(sempno == '')
 	{
 		alert('请填写员工编号!');
 		return;
 	}
 	if(opObj == '')
 	{
 		alert('请选择'+vbranName);
 		return;
 	}
 	if(schannelid == '')
 	{
 		alert('请选择渠道!');
 		return ;
 	}
 	//判断第二联系人，是否分配副号码
 	XMLHttpReq.open("POST","${ctx}/selectenablenumber",true);
	XMLHttpReq.onreadystatechange = processResponseNU;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("branchactiongroup="+opObj+"&ssmnnumber="+ssmnval+"&type=2&smsisdn="+smsisdn+
	"&aempno="+sempno+"&channelid="+schannelid+"&secondmsisdn="+vsecondmsisdn);
}

function processResponseNU(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageNU();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }
 
  function parseMessageNU()
{
	ssmnall=XMLHttpReq.responseText;
	
	var aname = document.getElementById('aname').value;
	if(aname == '')
	{
		alert('请输入经纪人姓名!')
		return false;
	}
	
	var sempno = document.getElementById("aempno").value;
	if(sempno == '')
	{
		alert('请填写员工编号!');
 		return;
	}
	
	var msisdn = document.getElementById('msisdn').value;
	if(msisdn == '')
	{
		alert('请输入主号码!')
		return false;
	}
	 var reg = new RegExp("^[0-9]*$");
	 if(!reg.test(msisdn))
	 {
	 	alert("主号码必须是数字!");
	 	return ;
	 }
	
	var businessdepartment = document.getElementById('businessdepartment').value;
	if(businessdepartment == '')
	{
		alert('请输入'+vbusinName);
		return false;
	}
	
	var warzone = document.getElementById('warzone').value;
	if(warzone == '')
	{
		alert('请输入'+vwarName);
		return false;
	}
	
	var area = document.getElementById('area').value;
	if(area == '')
	{
		alert('请输入'+vareaName);
		return false;
	}
	
	var branchactiongroup = document.getElementById('branchactiongroup').value;
	if(branchactiongroup == '')
	{
		alert('请输入'+vbranName);
		return false;
	}

	var ssmn = '';
	if(visHaveSsmnNum == '1')//如果开关打开并且是A+渠道，不分配副号码（副号码框不存在，给默认的00000000000
 		ssmn ='00000000000';
 	else
	 	ssmn =document.getElementById('ssmn').value;
	if(ssmn == '')
	{
		if(ssmnall == '')
			alert('号码池中没有可用的副号码!');
		else
			alert('请填写副号码!')
		return false;
	}
	
	//第二联系人
	var vsecondmsisdn ='';
	if(visSecondMsisdn =='1')
	 	vsecondmsisdn=document.getElementById("secondMsisdn").value;
	
	var channel = document.getElementById('channel').value;
	if(channel == '')
	{
		alert('请输入所属渠道!')
		return false;
	}
	if(ssmnall == '')
	{
		alert('副号码不可用!');
		return false;
	}else
	{
				
		if( ssmnall.indexOf("主号码已存在") >=0 || ssmnall.indexOf("该员工编号已经存在") >=0 
			|| ssmnall.indexOf("填写的第二联系人无效") >=0 )
		{
			alert(ssmnall);
			return false;
		}
		else
			document.location.href="manageagent.do?method=add&aname="+aname+"&msisdn="+
			msisdn+"&businessdepartment="+businessdepartment+"&warzone="+warzone+"&area="+
			area+"&branchactiongroup="+branchactiongroup+"&ssmn="+ssmn+
			"&channel="+channel+"&aempno="+sempno+"&secondMsisdn="+vsecondmsisdn;

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
}
.dfinput
{
	width:248px;
	height:28px;
}
</style>
</head>

<body topmargin="0" >
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">创建经纪人</a></li>
	</ul>
</div>
<c:if test = "${not empty(msg)}">
	 <font color="red">${msg}</font>
</c:if>

<div class="formbody" style="width:1100px;">
<form method="post" action="manageagent.do?method=add" onsubmit="return check('${businName }','${warName }',
							'${areaName }','${branName }','${isSecondMsisdn}');" name="mainfm">

	<table width="100%" align="center" style="border-style:none;margin-left:50px;margin-top:30px;">
		<tr style="height:40px;">
			<td  width="90px;"><label>经纪人姓名：</label></td>
			<td>
				<input class="dfinput" id="aname" name="aname" value="${aname}" size="20" type="text" /><font color="red">*</font>
			</td>
		</tr>
		<tr style="height:40px;">
			<td><label>员工编号：</label></td>
			<td>
				<input class="dfinput"  id="aempno" name="aempno" value="${aempno}" type="text" size="20" /><font color="red">*</font>
			</td>
		</tr>
		<tr style="height:40px;">
			<td><label>主号码：</label></td>
			<td >
				<input class="dfinput" id="msisdn" name="msisdn" value="${msisdn}" type="text" size="20" maxlength="11" /><font color="red">*</font>
			</td>
		</tr>
		<tr style="height:40px;">
			<td><label>${businName }：</label></td>
			<td>
			<select class="select3" id="businessdepartment" name="businessdepartment"  onChange="changeSelect('1','${businName }')">
				 <c:if test="${businessdepartment == null }">
					 <option value="">----请选择----</option> 
				 </c:if>
				 <c:forEach items="${bdlist}" var="c">
		         	 <option value="${c}" >${c}</option>
		         </c:forEach>
     	 	</select>
			</td>
		</tr>
		<tr style="height:40px;">
			<td><label>${warName }：</label></td>
			<td>
				<select class="select3" style="width:173px;height:23px;" id="warzone" name="warzone"  onChange="changeSelect('2','${warName }')">
					  <c:if test="${warzone == null }">
						 <option value="">----请选择----</option> 
					  </c:if>	
					  <c:forEach items="${wzlist}" var="c">
			         	 <option value="${c}" >${c}</option>
			          </c:forEach>
	     	 	</select>
			</td>
		</tr>
		<tr style="height:40px;">
			<td><label>${areaName }：</label></td>
			<td>
				<select class="select3" style="width:173px;height:23px;" id="area" name="area"  onChange="changeSelect('3','${areaName }')">
					<c:if test="${area == null }">
						<option value="">----请选择----</option> 
					</c:if>	
					<c:forEach items="${arealist}" var="c">
						<option value="${c }" >${c}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr style="height:40px;">
			<td><label>${branName }：</label></td>
			<td>
				<select class="select3" style="width:173px;height:23px;" id="branchactiongroup" name="branchactiongroup" onChange="changeSelect('4','${branName }')">
					<c:if test="${branchactiongroup == null }">
						<option value="">----请选择----</option> 
					</c:if>	
					<c:forEach items="${baglist}" var="c">
						<option value="${c}" >${c}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr style="height:40px;">
			<td><label>副号码渠道：</label></td>
			<td>
				<select class="select3" style="width:173px;height:23px;" id="channel" name="channel" onChange="changeChannel()" >
					<option value="">----请选择----</option> 
					<c:forEach items="${channellist}" var="c">
						<option value="${c.id}" >${c.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr style="height:40px;" id='selectSsmnnumber'>
			<td><label>分配副号码：</label></td>
			<td><input class="dfinput" id="ssmn" name="ssmn" type="text" /><font color="red">* 随机分配,可编辑</font> </td>
		</tr>
		<c:if test="${isSecondMsisdn == 1}"> 
			<tr style="height:40px;">
				<td><label>第二联系人：</label></td>
				<td>
					<input class="dfinput" size="20" maxlength="11"  id="secondMsisdn" name="secondMsisdn" />
				</td>
			</tr>
		</c:if>
		<tr><td  colspan="2">&nbsp;</td></tr>
		<tr>
			<td colspan="2" >	 
				<input style="margin-left:8%;" class="scbtn" type="button" value="保存" onclick="return check('${businName }','${warName }','${areaName }','${branName }','${isSecondMsisdn}');" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	 		<input class="scbtn"  type="button" value="返回"  onClick="javascript:document.location.href='manageagent.do?method=queryagent'"/>
			</td>
		</tr>
	</table>
</form>

</div>
</body>
</html>

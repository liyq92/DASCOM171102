<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
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
var wBox;

var varagentName;//暂存经纪人姓名
var varagentMsisdn;//暂存经纪人主号码
var varagentbranchactiongroup;//暂存经纪人行动组名
var varagentempno;//暂存经纪人员工编号
var varexportbatchcancelfile;//解绑结果文件名称
var vbusinName;
var vwarName;
var vareaName;
var vbranName;
var varsecondmsisdn;//暂存第二联系人
var visSecondMsisdn;//是否开启第二联系人开关
var visHaveSsmnNum ;//针对重庆，是否开启     只有A+渠道不给分配副号码

function cancel(){
	wBox.close();
}

function init(msg,filenamebatchcan,sucesscount,faildcount,opertype){
	if(filenamebatchcan != '')//显示批量处理结果
	{
		varexportbatchcancelfile = filenamebatchcan;
		if(opertype == '解绑' || opertype== '绑定')
		{
			wBox=jQuery("#wbox10").wBox({
			title: "",
			html: "<div style='width:300px;height:100px;'><div style='text-align=center;margin-left:30px;margin-top:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+opertype+"结果:"+opertype+"成功"+sucesscount+"个;"+opertype+"失败"+faildcount+"个</div>"+
			"<div style='margin-bottom:5px;margin-top:10px;'><table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div><input class='scbtn'  style='margin-left:80px;margin-top:5px;width:40%;' type='button' value='"+opertype+"结果导出' onclick='exportCancelbatchFile();' /> </div>"
			});
		}else
		{
			wBox=jQuery("#wbox10").wBox({
			title: "",
			html: "<div style='width:300px;height:100px;'><div style='text-align=center;margin-left:30px;margin-top:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+"批量处理结果:"+"成功"+sucesscount+"个;  "+"失败"+faildcount+"个</div>"+
			"<div style='margin-bottom:5px;margin-top:10px;'><table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div><input class='scbtn' style='margin-left:80px;margin-top:5px;width:40%;' type='button' value='"+"结果导出' onclick='exportCancelbatchFile();' /> </div>"
			});
		}
		wBox.showBox();
	}
	else
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

function exportCancelbatchFile()
{
	wBox.close();
	if(varexportbatchcancelfile !='')
		document.location="${ctx}/batchDealUsers/"+varexportbatchcancelfile;
}

//初始化查询各级级别列表 
var XMLHttpReq = false;
function sendRequest2(bd,warzone,area,bag){
 	createXMLHttpRequest();
 	XMLHttpReq.open("POST","${ctx}/selectlevel",true);
	XMLHttpReq.onreadystatechange = processResponse2;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("method=list&bd="+bd+"&warzone="+warzone+"&area="+area+"&bag="+bag);
}

function processResponse2(){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    // alert('success2');
			parseMessage2();
		}else{
			alert(XMLHttpReq.status);
		}
	}
 }

function parseMessage2()
{
    var all=XMLHttpReq.responseText.split("#"); 
    var param = all[0].split("|"); 
    var bd =param[0];
    var wz =param[1];
    var area =param[2];
    var bag =param[3];
    var bdlist = all[1].split("|");
    var wzlist = all[2].split("|");
    var arealist = all[3].split("|");
    var baglist = all[4].split("|");
    //处理事业部列表
    var select_root=document.getElementById('businessdepartmentbox');   
    var	 defa = new Option("全部",""); 
  	select_root.options.length=0;
    select_root.add(defa);
    for(var i=0; i<bdlist.length; i++)
    {
        var xValue=bdlist[i];
        var option=new Option(xValue,xValue);
        if(xValue==bd){
          option.selected = true;
        }
        try{
            select_root.add(option);
        }catch(e){
        }
    }  
    
    //处理区域列表
    var select_root1=document.getElementById('warzonebox');   
    var	 defa1 = new Option("全部",""); 
  	select_root1.options.length=0;
    select_root1.add(defa1);
    for(var i=0; i<wzlist.length; i++)
    {
        var xValue=wzlist[i];
        var option=new Option(xValue,xValue);
        if(xValue==wz){
          option.selected = true;
        }
        try{
            select_root1.add(option);
        }catch(e){
        }
    }  
    
    //处理片区列表
    var select_root2=document.getElementById('areabox');   
    var	 defa2 = new Option("全部",""); 
  	select_root2.options.length=0;
    select_root2.add(defa2);
    for(var i=0; i<arealist.length; i++)
    {
        var xValue=arealist[i];
        var option=new Option(xValue,xValue);
        if(xValue==area){
          option.selected = true;
        }
        try{
            select_root2.add(option);
        }catch(e){
        }
    }  
    //处理行动组列表
    var select_root3=document.getElementById('branchactiongroupbox');   
    var	 defa3 = new Option("全部",""); 
  	select_root3.options.length=0;
    select_root3.add(defa3);
    for(var i=0; i<baglist.length; i++)
    {
        var xValue=baglist[i];
        var option=new Option(xValue,xValue);
        if(xValue==bag){
          option.selected = true;
        } 
        try{
            select_root3.add(option);
        }catch(e){
        }
    }   
}

//点击选择各等级下拉菜单  
function changeSelect(type){
    if(type == '1'){
        var opObj = document.getElementById('businessdepartmentbox'); 
	    //alert(opObj.value);
	       var select_root=document.getElementById('warzonebox');    
   	       select_root.options.length=0;
   	       var defa = new Option("全部","");
     	   select_root.add(defa);
     	   
		   var select_root1=document.getElementById('areabox');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("全部","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('branchactiongroupbox');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("全部","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
		      sendRequest(type);
	        }
    }else if(type == '2'){
          var opObj = document.getElementById('warzonebox'); 
	       //alert(opObj.selectedIndex);
		   var select_root1=document.getElementById('areabox');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("全部","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('branchactiongroupbox');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("全部","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
     	      sendRequest(type);
     	   }
	    
    }else{
        var opObj = document.getElementById('areabox'); 
	    //alert(opObj.value);
     	var select_root2=document.getElementById('branchactiongroupbox');    
   	    select_root2.options.length=0;
   	    var defa2 = new Option("全部","");
     	select_root2.add(defa2);
	    if(opObj.selectedIndex!=0){
		   sendRequest(type);
	    }
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
 	var opObj = document.getElementById('businessdepartmentbox'); 
 	var opObj1 = document.getElementById('warzonebox'); 
    var opObj2 = document.getElementById('areabox'); 
    //alert(opObj.value);
    //alert(opObj1.value);
    //alert(opObj2.value);
 	XMLHttpReq.open("POST","${ctx}/selectlevel",true);
	XMLHttpReq.onreadystatechange = processResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type="+type+"&bd="+opObj.value+"&warzone="+opObj1.value+"&area="+opObj2.value);
}

function processResponse(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessage();
		}
		else{
			alert(XMLHttpReq.status);
		}
	}
 }

function parseMessage()
{
  	var all=XMLHttpReq.responseText.split("#");  
  	var type = all[0];   
  	var select_root=document.getElementById('branchactiongroupbox');   
  	var defa;
  	if(type == '1'){
  	     select_root=document.getElementById('warzonebox');   
  	     select_root.options.length=0;
       	 defa = new Option("全部",""); 
  	}else if(type == '2'){
  	     select_root=document.getElementById('areabox');  
  	     select_root.options.length=0;
       	 defa = new Option("全部","");   
  	}else{
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

var agentuid;
var agentmsisdn;
var selectName;
function editAgent(name,msisdn,bd,warzone,area,bag,uid ,empno,
					businName,warName,areaName,branName,secondmsis,isSecondMsisdn)
{
	//查询对应的级别列表
	sendRequest2(bd,warzone,area,bag);
	//agentuid = uid;
	agentmsisdn = msisdn;
      
	varagentName=name;
	varagentMsisdn = msisdn;
	varagentbranchactiongroup =bag;
	varagentempno = empno;
	vbusinName = businName;
	vwarName = warName;
	vareaName = areaName;
	vbranName = branName;
	varsecondmsisdn =secondmsis;
	visSecondMsisdn =isSecondMsisdn;

	wBox=jQuery("	#wbox10").wBox({
	title: "",
	html: "<c:if test='${isSecondMsisdn == 1}'><div style='width:330px;height:300px;'></c:if>"+
	"<c:if test='${isSecondMsisdn != 1}'><div style='width:330px;height:270px;'></c:if>"+
	"<table style='margin-left:40px;width:80%;margin-top:-14px;'>"+
	"<tr style='margin-bottom:10px;font-family:Arial;font-style:normal;text-decoration:none;color:#333333;height:25px;'>"+
	"<td style='font-size:14px;font-weight:bold;'>经纪人：</td>"+
	"<td><input id='agentname' style='width:150px ; height:20px;border:1px;border-style:solid;font-size:14px;' name='agentname' type='text' value='"+name+"' /> </td>"+
	"</tr>"+
	"<tr style='font-family:Arial;font-style:normal;text-decoration:none;color:#333333;height:25px;'>"+
		"<td style='font-size:14px;font-weight:bold;'>员工编号：</td>"+
		"<td><input id='aempno' style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;'  name='aempno' type='text' value='"+empno+"' /> </td></tr>"+
		"<tr style='margin-bottom:10px;font-family:Arial;font-style:normal;text-decoration:none;color:#333333;'>"+
		"<td style='font-size:14px;font-weight:bold;'>主号码：</td>"+
		"<td><input id='agentmsisdn' style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' value='"+msisdn+"' /></td>"+
	"</tr>"+
	"<tr style='height:27px;'>"+
		"<td style='font-size:14px;font-weight:bold;'>"+businName+"：</td>"+
		"<td>"+
			"<select id='businessdepartmentbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(1)'>"+
				"<option value=''>全部</option><option value=''>13811100000</option>"+
			"</select>"+
		"</td>"+
	"</tr><br>"+
	"<tr style='height:27px;'><td style='font-size:14px;font-weight:bold;'>"+warName+"：</td>"+
	"<td><select id='warzonebox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(2)'>"+
	"<option value=''>全部</option>"+
	"<option value=''>13811100000</option>"+
	"</select></td></tr>"+
	"<tr style='height:27px;'><td style='font-size:14px;font-weight:bold;'>"+areaName+"：</td>"+
	"<td><select id='areabox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(3)'>"+
	"<option value=''>全部</option>"+
	"<option value=''>13811100000</option>"+
	"</select></td></tr>"+
	"<tr style='height:27px;'><td style='font-size:14px;font-weight:bold;'>"+branName+"：</td>"+
	"<td><select id='branchactiongroupbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;'>"+
	"<option value=''>全部</option>"+
	"<option value=''>13811100000</option>"+
	"</select></td></tr>"+
	"<c:if test='${isSecondMsisdn == 1}'>"+
	"<tr style='margin-bottom:10px;font-family:Arial;font-style:normal;text-decoration:none;color:#333333;'>"+
	"<td style='font-size:14px;font-weight:bold;'>第二联系人</td>"+
	"<td><input id='esecondmsisdn' style='font-size:14px;width:150px ;"+
		" height:20px;border:1px;border-style:solid;' type='text' value='"+secondmsis+"'/></td>"+
	"</tr> </c:if> "+
	"</table> "+
	"<div style='margin-bottom:10px;margin-top:10px;'>"+
	"<table width='330' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
	" <input  style='margin-top:5px;margin-left:50px;width:30%;' class='cancel' type='button' value='取消' onclick='cancel();' />"+
	"<input  style='margin-top:5px;margin-left:20px;width:30%;' class='sure' type='button' value='确定' onclick='check();' /> </div>"
	});
	wBox.showBox();
}
 
function check()
{
  	var agentname = document.getElementById('agentname').value;
	if(agentname == '')
	{
		alert('请输入经纪人名称!')
		return false;
	}
	var aempno = document.getElementById('aempno').value;
	//if(aempno == '')
	//{
	//	alert('员工编号不能为空!');
	//	return false;
	//}
	
	var agentmis = document.getElementById('agentmsisdn').value;
	if(agentmis == '')
	{
		alert('请输入主号码!');
		return false;
	}
	var reg = new RegExp("^[0-9]*$");
	if(!reg.test(agentmis))
	{
		alert("主号码必须是数字!");
		return false;
	}
	
	var businessdepartment = document.getElementById('businessdepartmentbox').value;
	if(businessdepartment == '')
	{
		alert('请输入'+vbusinName);
		return false;
	}
	
	var warzone = document.getElementById('warzonebox').value;
	if(warzone == '')
	{
		alert('请输入'+vwarName);
		return false;
	}
	
	var area = document.getElementById('areabox').value;
	if(area == '')
	{
		alert('请输入'+vareaName);
		return false;
	}
	
	var branchactiongroup = document.getElementById('branchactiongroupbox').value;
	if(branchactiongroup == '')
	{
		alert('请输入'+vbranName);
		return false;
	}
	var vsecmsis ='';
	if(visSecondMsisdn =='1')
		vsecmsis=document.getElementById('esecondmsisdn').value;//第二联系人
	
	//判断所编辑的内容是否与之前一致,因为行动组唯一，所以只需要判断行动组名即可
	if(agentname ==varagentName && varagentempno==aempno　&& agentmis==varagentMsisdn 
	&& branchactiongroup == varagentbranchactiongroup && varsecondmsisdn ==vsecmsis)
	{
		alert('没有修改操作!');
		return false;
	}
	//判断第二联系人是否有效
	if(vsecmsis !='')
	{
		sendRequestSeconMsisdn();
	}else
		submitEdit();

}
//获取可用的副号码
function sendRequestSeconMsisdn()
{
 	createXMLHttpRequest();
 	var vsecmsis ='';
 	if(visSecondMsisdn =='1')
 		vsecmsis=document.getElementById('esecondmsisdn').value;//第二联系人
 	
 	XMLHttpReq.open("POST","${ctx}/selectenablenumber",true);
	XMLHttpReq.onreadystatechange = processResponseSeconMsisdn;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type=6&seconmsisdn="+vsecmsis);
}

function processResponseSeconMsisdn(){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageSeconMsisdn();
		}
		else{
			alert(XMLHttpReq.status);
		}
	}
 }
 
function parseMessageSeconMsisdn()
{
	var sn=XMLHttpReq.responseText;
	
	 if(sn.indexOf("填写的第二联系人无效") >=0)
	 {
	 	alert(sn);
	 	return;
	 }
	 
	 submitEdit();
}

function submitEdit()
{
	var selectsname = document.getElementById("name").value;
 	var selectvmsisdn = document.getElementById("msisdn").value;
 	var selectvssmnnum = document.getElementById("ssmnnum").value;
 	var selectvchannelbox =document.getElementById("channelbox");
 	var selectvbdbox1 = document.getElementById("bdbox1");
 	var selectvzonebox1 = document.getElementById("zonebox1");
 	var selectvareabox1 = document.getElementById("areabox1");
 	var selectvbagbox1 = document.getElementById("bagbox1");
 	var vsecmsis ='';
 	if(visSecondMsisdn =='1')
 		vsecmsis=document.getElementById('esecondmsisdn').value;//第二联系人
 	var businessdepartment = document.getElementById('businessdepartmentbox').value;
 	var warzone = document.getElementById('warzonebox').value;
 	var area = document.getElementById('areabox').value;
 	var branchactiongroup = document.getElementById('branchactiongroupbox').value;
 	var agentname = document.getElementById('agentname').value;
 	var aempno = document.getElementById('aempno').value;
 	var agentmis = document.getElementById('agentmsisdn').value;
 	
	var varurl ="manageagent.do?method=edit&bd="+businessdepartment+"&warzone="+warzone+
		"&area="+area+"&bag="+branchactiongroup+"&agentname="+agentname+"&aempon="+aempno+
		"&oldEmpno="+varagentempno+"&msisdn1="+agentmsisdn+"&newmsisdn="+agentmis;
	varurl +="&name="+selectsname+"&msisdn="+selectvmsisdn+"&ssmnnum="+selectvssmnnum;
	
	if(selectvchannelbox.selectedIndex != 0)
		varurl +="&channelbox="+selectvchannelbox.value;
	if(selectvbdbox1.selectedIndex != 0)
		varurl +="&bdbox1="+selectvbdbox1.value;
	if(selectvzonebox1.selectedIndex != 0)
		varurl +="&zonebox1="+selectvzonebox1.value;
	if(selectvareabox1.selectedIndex != 0)
		varurl +="&areabox1="+selectvareabox1.value;
	if(selectvbagbox1.selectedIndex != 0)
		varurl +="&bagbox1="+selectvbagbox1.value;
		
	varurl +="&secondmsisdn="+vsecmsis;
	
	document.location.href=varurl;

}

 
 
function deleteAgent(msisdn){
agentmsisdn = msisdn;
 wBox=jQuery("	#wbox10").wBox({
		title: "",
		html: "<div style='width:350px;height:110px;'>"+
		"<table style='margin-top:5px;width:100%;'>"+
		"<tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+
		"<td style='font-size:15px;text-align:center;'>确认是否删除该经纪人？删除后解绑所有副号码！<br><br></td> </tr><br>  </table> "+
		"<div style='margin-bottom:2px;'>"+
		"<table width='350' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input  style='margin-top:7px;margin-left:60px;width:30%;' class='cancel' type='button' value='取消' onclick='cancel();' /><input  style='margin-top:5px;margin-left:20px;width:30%;' class='sure' type='button' value='确定' onclick='deletecheck();' /> </div>"
		});
		wBox.showBox();
	
}

function deletecheck()
{
	var selectsname = document.getElementById("name").value;
 	var selectvmsisdn = document.getElementById("msisdn").value;
 	var selectvssmnnum = document.getElementById("ssmnnum").value;
 	var selectvchannelbox =document.getElementById("channelbox");
 	var selectvbdbox1 = document.getElementById("bdbox1");
 	var selectvzonebox1 = document.getElementById("zonebox1");
 	var selectvareabox1 = document.getElementById("areabox1");
 	var selectvbagbox1 = document.getElementById("bagbox1");
 	
	var varurl ="manageagent.do?method=delete&demsisdn="+agentmsisdn ;
	varurl +="&name="+selectsname+"&msisdn="+selectvmsisdn+"&ssmnnum="+selectvssmnnum;
	
	if(selectvchannelbox.selectedIndex != 0)
		varurl +="&channelbox="+selectvchannelbox.value;
	if(selectvbdbox1.selectedIndex != 0)
		varurl +="&bdbox1="+selectvbdbox1.value;
	if(selectvzonebox1.selectedIndex != 0)
		varurl +="&zonebox1="+selectvzonebox1.value;
	if(selectvareabox1.selectedIndex != 0)
		varurl +="&areabox1="+selectvareabox1.value;
	if(selectvbagbox1.selectedIndex != 0)
		varurl +="&bagbox1="+selectvbagbox1.value;

	document.location.href=varurl;

}

function bindAgent(sname,smsisdn,strChannel,uid,stname)
{	
	selectName = stname;
	var channels = new Array();
	if(strChannel.length >0)
	{
		channels = strChannel.split(",");
	}
	
	var opchannelstr = '';
	for(var j=0; j<channels.length; j++)
	{
		var str = channels[j];
		var strsplit = str.split("|");
		opchannelstr += "<option value='";
		opchannelstr +=strsplit[0];
		opchannelstr +="'>";
		opchannelstr +=strsplit[1];
		opchannelstr +="</option>";
	}
	
	if(sname != '' && smsisdn != '')
	{	
		<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;绑定 -->
		wBox=jQuery("	#wbox10").wBox({
		title: "",
		html: "<div style='width:330px;height:165px;'>"+
			  "<table style='margin-top:5px;margin-left:30px;width:92%;' cellpadding='9px'>"+
			  "<tr style='margin-bottom:10px;font-family:Arial;font-style:normal;text-decoration:none;color:#333333;'>"+
			 	 "<td height='25' style='font-size:14px;font-weight:bold;'>经纪人：</td><td style='font-size:14px;'>"+sname+
			  "</td></tr>"+
			  "<tr style='font-family:Arial;font-size:14px;font-style:normal;text-decoration:none;color:#333333;'>"+
			  "<td height='25' style='font-size:14px;font-weight:bold;'>主号码：</td><td style='font-size:14px;'>"+smsisdn+"</td></tr>"+
			  "<tr><td height='27' style='font-size:14px;font-weight:bold;'>渠道：</td>"+
			  	"<td><select style='height:23px;width:120px;font-size:15px; progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' id='ssmnchannelvalue'  onchange='changeChannel("+uid+")' >"+
			  		"<option value=''>请选择渠道</option>"+
				opchannelstr+"</select></td></tr>"+
			  "<tr style='margin-top:20px;margin-bottom:50px;' id='selectSsmnnumber' >"+
			  	"<td height='27' style='font-size:14px;font-weight:bold;'>新增副号码：</td>"+
			  	"<td><input type='text' id='ssmnnumbervalue' style='width:118px ; height:20px;border:1px;border-style:solid;' value='' />"+
			  		"<font color='red'>&nbsp;随机分配,可编辑</font></td></tr></table> <div style='margin-bottom:10px;margin-top:10px;'><table width='330' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td>"+
			  		"</tr></table></div>"+
		      " <input style='margin-top:3px;margin-left:56px;width:30%;' class='cancel' type='button' value='取消' onclick='cancel();' />"+
		      " <input class='sure' style='margin-top:3px;margin-left:20px;width:30%;' type='button' value='确定' onclick='bindButoon("+smsisdn+","+uid+");' /> "+
		      "</div>"
		});
		
		wBox.showBox();
	}
}

function changeChannel(uid)
{
	sendRequestEN(uid); 
}

//获取可用的副号码
function sendRequestEN(uid)
{
 	createXMLHttpRequest();

 	var sc = document.getElementById('ssmnchannelvalue');
	var scindex = sc.selectedIndex;
	var scvalue = sc.options[scindex].value;
	if(scvalue == '')
	{
		alert("请选择渠道!");
		return;
	}
 	
 	XMLHttpReq.open("POST","${ctx}/selectenablenumber",true);
	XMLHttpReq.onreadystatechange = processResponseEN;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type=1&uid="+uid+"&channelid="+scvalue);
}

function processResponseEN(){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageEN();
		}
		else{
			alert(XMLHttpReq.status);
		}
	}
 }
 
function parseMessageEN()
{
	var sn=XMLHttpReq.responseText;
	
	document.getElementById("ssmnnumbervalue").value=sn;
		 
	var aa = document.getElementById('ssmnnumbervalue').value;
	 
	if(aa == '')
	{
		wBox.close();
		alert("号码池中没有可用的副号码!");
	}
	
	//if反回11个0默认副号码，则隐藏副号码框（A+渠道不分配副号码的)
	if(sn.indexOf('00000000000')>=0)
	{
		visHaveSsmnNum ='1'; //不需要分配副号码
		document.getElementById("selectSsmnnumber").style.display="none";
	}
	else
	{
		visHaveSsmnNum ='0'; //需要分配副号码
		document.getElementById("selectSsmnnumber").style.display="table-row";
		document.getElementById("ssmnnumbervalue").value=sn;
	}
	
	
}

//绑定
function bindButoon(smsisdn,suid)
{
	//wBox.close();
	var smvalue ='';
	if(visHaveSsmnNum == '1')//如果开关打开,并且是A+渠道，不分配副号码（副号码框不存在，给默认的00000000000
 		smvalue ='00000000000';
 	else
		smvalue = document.getElementById('ssmnnumbervalue').value;
	if(smvalue == '')
	{
		alert("请输入副号码!");
		return;
	}
	
	var sc = document.getElementById('ssmnchannelvalue');
	var scindex = sc.selectedIndex;
	var scvalue = sc.options[scindex].value;
	if(scvalue == '')
	{
		alert("请选择渠道!");
		return;
	}
	
	var selectsname = document.getElementById("name").value;
 	var selectvmsisdn = document.getElementById("msisdn").value;
 	var selectvssmnnum = document.getElementById("ssmnnum").value;
 	var selectvchannelbox =document.getElementById("channelbox");
 	var selectvbdbox1 = document.getElementById("bdbox1");
 	var selectvzonebox1 = document.getElementById("zonebox1");
 	var selectvareabox1 = document.getElementById("areabox1");
 	var selectvbagbox1 = document.getElementById("bagbox1");
 	
	var varurl ="manageagent.do?method=bind&strsmnnumber="+smvalue+"&&channelvalue="+scvalue+"&&bindmsisdn="+smsisdn+"&&uid="+suid+"&name="+selectName;
	varurl +="&msisdn="+selectvmsisdn+"&ssmnnum="+selectvssmnnum;
	
	if(selectvchannelbox.selectedIndex != 0)
		varurl +="&channelbox="+selectvchannelbox.value;
	if(selectvbdbox1.selectedIndex != 0)
		varurl +="&bdbox1="+selectvbdbox1.value;
	if(selectvzonebox1.selectedIndex != 0)
		varurl +="&zonebox1="+selectvzonebox1.value;
	if(selectvareabox1.selectedIndex != 0)
		varurl +="&areabox1="+selectvareabox1.value;
	if(selectvbagbox1.selectedIndex != 0)
		varurl +="&bagbox1="+selectvbagbox1.value;
	
	document.location.href=varurl;

}

function exportInfo()
{
	sendRequestSSMN();
}

function sendRequestSSMN(){
 	createXMLHttpRequest();
  
 	var sname = document.getElementById("name").value;
 	var vmsisdn = document.getElementById("msisdn").value;
 	var vssmnnum = document.getElementById("ssmnnum").value;
 	var vchannelbox =document.getElementById("channelbox");
 	var vbdbox1 = document.getElementById("bdbox1");
 	var vzonebox1 = document.getElementById("zonebox1");
 	var vareabox1 = document.getElementById("areabox1");
 	var vbagbox1 = document.getElementById("bagbox1");
 	var vempno = document.getElementById("empno").value;
 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseSSMN;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl = "type=3&name="+sname+"&msisdn="+vmsisdn+"&ssmnnum="+vssmnnum;
	if(vchannelbox.selectedIndex != 0)
		vurl +="&channelbox="+vchannelbox.value;
	if(vbdbox1.selectedIndex != 0)
		vurl +="&bdbox1="+vbdbox1.value;
	if(vzonebox1.selectedIndex != 0)
		vurl +="&zonebox1="+vzonebox1.value;
	if(vareabox1.selectedIndex != 0)
		vurl +="&areabox1="+vareabox1.value;
	if(vbagbox1.selectedIndex != 0)
		vurl +="&bagbox1="+vbagbox1.value;
	if(vempno != '')
		vurl +="&vempno="+vempno;
	XMLHttpReq.send(vurl);
}

function processResponseSSMN(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageSSMN();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }
 
  function parseMessageSSMN()
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

function showuploadFileCancel()
{
	document.getElementById("uploadfile_cancel_form").style.display= "inline";
	document.getElementById("uploadfile_bind_form").style.display= "none";
	document.getElementById("uploadfile_deal_form").style.display="none";
}

function showuploadFileBind()
{
	document.getElementById("uploadfile_cancel_form").style.display= "none";
	document.getElementById("uploadfile_bind_form").style.display= "inline";
	document.getElementById("uploadfile_deal_form").style.display="none";
}

function showuploadFileDeal()
{
	document.getElementById("uploadfile_deal_form").style.display="inline";
	document.getElementById("uploadfile_bind_form").style.display= "none";
	document.getElementById("uploadfile_cancel_form").style.display= "none";
}

function checkcancelbatch()
{
	var vfile = document.getElementById("uploadfileCancelUser").value;
	if(vfile == '')
	{
		alert('请上传文件!');
		return false;
	}
}

function checkbindbatch()
{
	var vfile = document.getElementById("uploadfileBindUser").value;
	if(vfile == '')
	{
		alert('请上传文件!');
		return false;
	}
}

function checkdealbatch()
{
	var vfile = document.getElementById("uploadfileDealUser").value;
	if(vfile == '')
	{
		alert('请上传文件!');
		return false;
	}
}

function checkFile(fileName)
{
	if(fileName != ''){
	
	var str= new String(fileName);
	
	//判断文件名是否excel文件
	//alert(str);
	var index = str.lastIndexOf(".");
	if(index < 0) 
	{
		alert("上传的文件格式不正确，请选择Excel文件(*.xls)！");
		return false;
	}else 
	{
		
	 if(str.lastIndexOf(".xlsx")>0)
	 {
		alert('必须导入2003Excel文件！');	
		return false;
		}
	else if(str.lastIndexOf(".xls")<0)
	{
		alert( '必须导入Excel文件！');
		return false;
	}	
	}
	}else
	{
		alert('请上传文件!');
		return false;
	}
}

function bindbatch()
{
	var sre = "${ctx}/exportExcelTemp/bindbatchtemp.xls";
		document.location=sre;
}

function cancelbatch()
{
	var sre = "${ctx}/exportExcelTemp/cancelbatchtemp.xls";
		document.location=sre;

}
function dealbatch()
{
	var sre = "${ctx}/exportExcelTemp/dealbatchtemp.xls";
		document.location=sre;
}

function infoDetail(vmsisdn,vempno)
{
	if(vmsisdn== '' || vempno == '')
	{
		return false;
	}
	var selectsname = document.getElementById("name").value;
	var selectempno = document.getElementById("empno").value;
	var selectvmsisdn = document.getElementById("msisdn").value;

 	var selectvssmnnum = document.getElementById("ssmnnum").value;
 	var selectvchannelbox =document.getElementById("channelbox");
	var selectvbdbox1 = document.getElementById("bdbox1");
 	var selectvzonebox1 = document.getElementById("zonebox1");
 	var selectvareabox1 = document.getElementById("areabox1");
 	var selectvbagbox1 = document.getElementById("bagbox1");
 	var pagetemp = document.getElementById("pagetemp").value;
 	
	var varurl ="queryssmn.do?type=0&msisdn="+vmsisdn+"&vempno="+vempno;
	if(selectvmsisdn != '')
		varurl +="&selectmsisdn="+selectvmsisdn;
	if(selectempno != '')
		varurl +="&selectempno="+selectempno;
	if(selectsname != '')
		varurl +="&selectname="+selectsname;
	if(selectvssmnnum != '')
		varurl +="&selectssmnnum="+selectvssmnnum;

	if(selectvchannelbox.selectedIndex != 0)
		varurl +="&selectchannelbox="+selectvchannelbox.value;
	if(selectvbdbox1.selectedIndex != 0)
		varurl +="&selectbdbox1="+selectvbdbox1.value;
	if(selectvzonebox1.selectedIndex != 0)
		varurl +="&selectzonebox1="+selectvzonebox1.value;
	if(selectvareabox1.selectedIndex != 0)
		varurl +="&selectareabox1="+selectvareabox1.value;
	if(selectvbagbox1.selectedIndex != 0)
		varurl +="&selectbagbox1="+selectvbagbox1.value; 
	if(pagetemp != '')
		varurl +="&pagetemp="+pagetemp;
	
	document.location.href=varurl;

}
</script>
</head>

<body onLoad="init('${msg}','${filenamebatchcan}','${sucesscount }','${faildcount }','${opertype}');">
<%java.util.List authlist = (java.util.List)request.getAttribute("authMethodList");%>
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">经纪人管理</a></li>
	</ul>
</div>
    
<div class="formbody" style="width:1100px;"> 
<input type="hidden" id="pagetemp" value="${pagetemp }" />
<form method="post" action="manageagent.do?method=queryagent" name="mainfm" >
 <table width="100%" align="center" style="">
	 <tr style="height:40px;">
	 	<td class="t6" style="text-indent:10px;">
	     	<label>${businName }：</label> 
	     </td>
	     <td class="t7">
		    <select class="select3" id='bdbox1' name='bdbox1' onchange='changeHeadSelect(1)' <c:if test="${slevelid==6 || slevelid==5 || slevelid==4 || slevelid==3 }"> disabled="disabled" </c:if>>
			    <option value=''>全部</option>
				<c:forEach items="${bdlist}" var="bd" varStatus="s">
					<option value='${bd }' <c:if test="${bdparam==bd}">selected='true'</c:if>>${bd }</option>
				</c:forEach>
		    </select>
	    </td>
	    <td class="t6" style="text-indent:10px;">
	     	<label>${warName }：</label> 
	  	</td>
	  	<td class="t7">
		    <div class="vocation" >
			    <select class="select3" id='zonebox1' name='zonebox1' onchange='changeHeadSelect(2)' <c:if test="${ slevelid==6 || slevelid==5 || slevelid==4 }"> disabled="disabled" </c:if>>
				    <option value=''>全部</option>
					<c:forEach items="${wzlist}" var="wz" varStatus="s">
						<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
					</c:forEach>
			    </select>
		    </div>
	   	</td>
	    <td class="t6" style="text-indent:10px;">
	 		<label>${areaName }：</label> 
	    </td>
	    <td class="t7">
		    <div class="vocation" >
			    <select class="select3" id='areabox1' name='areabox1' onchange='changeHeadSelect(3)' <c:if test="${slevelid==6 || slevelid==5 }"> disabled="disabled" </c:if>>
			    	<option value=''>全部</option>
					<c:forEach items="${arealist}" var="ar" varStatus="s">
						<option value='${ar }' <c:if test="${areaparam==ar}">selected='true'</c:if>>${ar }</option>
					</c:forEach>
			    </select>
		    </div>
	    </td>
	    <td class="t6" style="text-indent:10px;">
	   		<label>${branName }：</label> 
	   	</td>
	  	<td class="t7">
		    <div class="vocation" >
			    <select class="select3" id='bagbox1' name='bagbox1' <c:if test="${slevelid==6 }"> disabled="disabled" </c:if>>
			    	<option value=''>全部</option>
					<c:forEach items="${baglist}" var="bag" varStatus="s">
						<option value='${bag }' <c:if test="${bagparam==bag}">selected='true'</c:if>>${bag }</option>
					</c:forEach>
			    </select>
		    </div>
	    </td>
	</tr>
	<tr style="height:40px;">
		<td class="t6" style="text-indent:10px;">
		 	<label>姓名：</label> 
		</td>
		<td class="t7">
		    <input style="width:148px;" id="name" name="name" type="text" class="scinput" value="${name}" maxlength="11" style="height:28px;" />
		</td>  
		<td class="t6" style="text-indent:10px;">
		  	<label>员工编号：</label>
		</td>
		<td class="t7">
			<input style="width:148px;" id="empno" name="empno" type="text" class="scinput" value="${empno}" maxlength="11" style="height:28px;" />
		</td>
	    <td class="t6" style="text-indent:10px;">
	  		<label>主号码：</label> 
	  	</td>
		<td class="t7">
		  	<input style="width:148px;" id="msisdn" name="msisdn" type="text" class="scinput" value="${smsisdn}" maxlength="11" style="height:28px;" />
		</td>
		<td class="t6" style="text-indent:10px;">
			<label>副号码：</label>
		</td>
		<td class="t7">
		 	<input style="width:148px;" id="ssmnnum" name="ssmnnum" type="text" class="scinput" value="${ssmnnum}" maxlength="11" style="height:28px;" />
		</td>
	 </tr>
	 <tr style="height:40px;"> 
		<td class="t6" style="text-indent:10px;">
			 <label>渠道：</label>
		</td>
		<td class="t7">
		    <div class="vocation" >
			    <select class="select3" id='channelbox' name='channelbox'>
			    	<option value=''>全部</option>
					<c:forEach items="${channels}" var="bag" varStatus="s">
						<option value='${bag.id }' <c:if test="${channelid==bag.id}">selected='true'</c:if>>${bag.name }</option>
					</c:forEach>
			  	</select>
		 	</div>
		</td>
	    <td class="t7" colspan="6"> 
	     	<input type="submit" class="scbtn" value="查询"/> &nbsp; &nbsp; &nbsp; &nbsp; 
	     	<%if(authlist != null && (authlist.contains("manageUsers"))){ %>
	  			<input class="scbtn" type="button" value="新建经纪人" onClick="javascript:document.location.href='manageagent.do?method=addview'"/>&nbsp; &nbsp; &nbsp; &nbsp; 
	  	  		<input class="scbtn" type="button"  value="批量解绑" onClick="showuploadFileCancel();"/>&nbsp; &nbsp;&nbsp; &nbsp;  			
	  			<input class="scbtn" type="button" value="批量绑定" onClick="showuploadFileBind()"/>&nbsp; &nbsp;  &nbsp; &nbsp;
	  			<input class="scbtn" type="button" value="批量处理" onClick="showuploadFileDeal()"/>&nbsp; &nbsp; &nbsp; &nbsp; 					
  			<%} %>
	     </td>
	</tr>
	<tr>
        <td height="6" colspan="8" bgcolor="ECECF5"></td>
	</tr> 
	<tr><td>&nbsp;</td></tr>
 </table>
</form>
 
<form  style="display:none"  method="post" action="manageagent.do?method=cancelBatch"   name="uploadfile_cancel_form" id="uploadfile_cancel_form" onsubmit="return checkcancelbatch();" enctype="MULTIPART/FORM-DATA" >	
	<table width="100%" align="center" style="border-collapse:collapse;font-size: 12px;" border="1" bordercolor="#3891a8">
		<tr>
			<td class="t8" style="font-size:15px;text-align:center;">批量解绑</td>
		</tr>
		<tr>
			<td class="t8" style="height:54px;">
				<input style="width:520px; height:24px;" type="file" id="uploadfileCancelUser" name="uploadfileCancelUser" onchange="checkFile(this.value)" />&nbsp;&nbsp;&nbsp;
				<input class="scbtn" style="width:80px" type="submit" value="上传"  />&nbsp;&nbsp;&nbsp;
				<input class="scbtn" style="width:100px;" type="button" value="模板下载" onclick="cancelbatch();" />
			</td>
		</tr>
	</table >
</form>

<form  style="display:none"  method="post" action="manageagent.do?method=bindBatch"   name="uploadfile_bind_form" id="uploadfile_bind_form" onsubmit="return checkbindbatch();" enctype="MULTIPART/FORM-DATA" >	
	<table width="100%" align="center" style="border-collapse:collapse;font-size: 12px;" border="1" bordercolor="#3891a8">
		<tr>
			<td class="t8" style="font-size:15px;text-align:center;">批量绑定<br /></td>
		</tr>
		<tr>
			<td class="t8" style="height:54px;">
				<input style="width:520px; height:24px;" type="file" id="uploadfileBindUser" name="uploadfileBindUser" onchange="checkFile(this.value)" />&nbsp;&nbsp;&nbsp;
				<input class="scbtn" style="width:80px" type="submit" value="上传"  />&nbsp;&nbsp;&nbsp;
				<input class="scbtn" style="width:100px;" type="button" value="模板下载" onclick="bindbatch();" /><br />
			</td>
		</tr>  
	</table >
</form>

<form  style="display:none"  method="post" action="manageagent.do?method=bindBatchDeal"   name="uploadfile_deal_form" id="uploadfile_deal_form" onsubmit="return checkdealbatch();" enctype="MULTIPART/FORM-DATA" >	
	<table width="100%" align="center" style="border-collapse:collapse;font-size: 12px;" border="1" bordercolor="#3891a8">
		<tr>
			<td class="t8" style="font-size:15px;text-align:center;">批量处理<br /></td>
		</tr>
		<tr>
			<td class="t8" style="height:54px;">
				<input style="width:520px; height:24px;" type="file" id="uploadfileDealUser" name="uploadfileDealUser" onchange="checkFile(this.value)" />&nbsp;&nbsp;&nbsp;
				<input class="scbtn" style="width:80px" type="submit" value="上传"  />&nbsp;&nbsp;&nbsp;
				<input class="scbtn" style="width:100px;" type="button" value="模板下载" onclick="dealbatch();" /><br />
			</td>
		</tr>  
	</table >
</form>   
  	   
<table class="tablelist">
	<thead>
		<tr>
		    <th>姓名</th>
		    <th>员工编号</th>
		    <th>主号码</th>
		    <th>${businName }</th>
		    <th>${warName }</th>
		    <th>${areaName }</th>
		    <th>${branName }</th>
		    <c:if test="${isSecondMsisdn == '1'}">
		    	<th>第二联系人</th>
		    </c:if>
		    <%if(authlist != null && (authlist.contains("manageUsers"))){ %>
        		<th>操作</th>
            <%} %>
	    </tr>
    </thead>
    <tbody>
    	<c:forEach items="${agentlist}" var="u" varStatus="s">
		    <tr>
			    <td><a href="javascript:" onClick="infoDetail('${u.msisdn}','${u.empno}');"><u>${u.name}</u></a></td>
			    <td>${u.empno}</td>
			    <td>${u.msisdn}</td>
			    <td>${u.businessdepartment}</td>
			    <td>${u.warzone}</td>
			    <td>${u.area}</td>
			    <td>${u.branchactiongroup}</td>
			    <c:if test="${isSecondMsisdn == '1'}">
			    	<td>${u.secondMsisdn}</td>
			    </c:if>
			    
			    <%if(authlist != null && (authlist.contains("manageUsers"))){ %>
		 			<td> 
		 				<a href="javascript:" style="color:#056dae;" onClick="bindAgent('${u.name}','${u.msisdn}',
		 				'${strChannels}','${u.uid}','${name}');">绑定</a>
		 				&nbsp;&nbsp;|&nbsp;&nbsp;
		 				<c:if test="${isSecondMsisdn == '1'}">
		 				<a href="javascript:" style="color:#056dae;" onClick="editAgent('${u.name}','${u.msisdn}',
		 										'${u.businessdepartment}','${u.warzone}','${u.area}','${u.branchactiongroup}',
		 										 '${u.uid}','${u.empno }','${businName }','${warName }','${areaName }',
		 										 '${branName }','${u.secondMsisdn}','${isSecondMsisdn}');">编辑</a>
		 				</c:if>
		 				<c:if test="${isSecondMsisdn != '1'}">
		 				<a href="javascript:" style="color:#056dae;" onClick="editAgent('${u.name}','${u.msisdn}',
		 										'${u.businessdepartment}','${u.warzone}','${u.area}','${u.branchactiongroup}',
		 										 '${u.uid}','${u.empno }','${businName }','${warName }','${areaName }',
		 										 '${branName }','','${isSecondMsisdn}');">编辑</a>
		 				</c:if>
		 				&nbsp;&nbsp;|&nbsp;&nbsp;
		 			    <a href="javascript:" style="color:#056dae;" onClick="deleteAgent('${u.msisdn}');">删除</a>
		 			</td> 
 				<%} %>
		    </tr> 
	    </c:forEach>
    </tbody>
</table>

<form method="post" action="manageagent.do" name="pageForm" style="width:100%;">
<input type="hidden" name="name" value="${name}" />
<input type="hidden" name="empno" value="${empno}" />
<input type="hidden" name="msisdn" value="${smsisdn}" />
<input type="hidden" name="ssmnnum" value="${ssmnnum}" />
<input type="hidden" name="channelbox" value="${channelid}" />
<input type="hidden" name="bdbox1" value="${bdparam}" />
<input type="hidden" name="zonebox1" value="${zoneparam}" />
<input type="hidden" name="areabox1" value="${areaparam}" />
<input type="hidden" name="bagbox1" value="${bagparam}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}"  formName="pageForm" url="manageagent.do?method=queryagent&page="/>
</form>

<br/>
<br/>
<c:if test="${recordCount >0}">
 <div style="width:100%;" align="right" > <input class="scbtn1" type="button" value="经纪人信息导出" onClick="exportInfo()" /></div>  
</c:if>

<!-- 提示信息 -->
<div class="tip">
	<div class="tiptop"><span>提示信息</span><a></a></div>
	   
	 <div class="tipinfo">
	   <div id="tipinfoBind"></div>
	   
	   <div class="tipbtn">
		   <input name="" type="button"  class="sure" value="确定" />&nbsp;
		   <input name="" type="button"  class="cancel" value="取消" />
	   </div>
</div>

</div>  
 
<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>
   
</body>
</html>

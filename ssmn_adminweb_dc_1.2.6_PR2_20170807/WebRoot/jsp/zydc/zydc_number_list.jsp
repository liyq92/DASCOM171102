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
var varssmnnumber;//暂存副号码
var varbranchactiongroup;//暂存行动组
var vbusinName;
var vwarName;
var vareaName;
var vbranName;
var vtype;
var varshowType;

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
        if(xValue.trim()==bd.trim()){
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
        if(xValue.trim()==wz.trim()){
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
        if(xValue.trim()==area.trim()){
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
        if(xValue.trim()==bag.trim()){
          option.selected = true;
        } 
        try{
            select_root3.add(option);
        }catch(e){
        }
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

//获取可用的副号码
function sendRequestEN(uid){
 	createXMLHttpRequest();
 	
 	XMLHttpReq.open("POST","${ctx}/selectenablenumber",true);
	XMLHttpReq.onreadystatechange = processResponseEN;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("uid="+uid);
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

}

function editNumber(msisdn,bd,warzone,area,bag,businName,warName,areaName,branName,nutype,showType){
       //查询对应的级别列表
       sendRequest2(bd,warzone,area,bag);
            
	  varssmnnumber = msisdn;
	  varbranchactiongroup = bag;
	  vbusinName = businName;
	  vwarName = warName;
	  vareaName = areaName;
	  vbranName = branName;
	  vtype=nutype;
	  varshowType =showType;
	  
	  var vNumtype;
	  if(showType == 0)
	  	vNumtype ="<option value='0'>渠道副号码</option>";
	  if(showType == 1)
	  	vNumtype ="<option value='2'>A+副号码</option>";
	  if(showType == 2)
	  	vNumtype ="<option value='0'>渠道副号码</option><option value='1'>业主副号码</option>"+
	  		"<option value='2'>A+副号码</option>";

        wBox=jQuery("	#wbox10").wBox({
		title: "",
		html: "<div style='width:330px;height:230px;'>"+
		"<table style='margin-left:40px;width:80%;margin-top: -12px;'>"+
			"<tr style='font-family:Arial;font-size:14px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+
			"<td style='height:25px;'>副号码：</td><td><input id='ssmnnumber' style='width:150px ; height:20px;border:1px;border-style:solid;' type='text' value='"+msisdn+"' /></td></tr>"+
			"<tr style='font-weight:bold;margin-top:20px;margin-bottom:130px;'>"+
				"<td style='font-size:14px;height:27px;'>"+businName+"：</td>"+
				"<td><select id='businessdepartmentbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(1)'>"+
			"<option value=''>全部</option><option value=''>13811100000</option></select></td></tr><br>"+
			"<tr style='font-weight:bold;'><td style='font-size:14px;height:27px;'>"+warName+"：</td>"+
			"<td><select id='warzonebox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(2)'>"+
			"<option value=''>全部</option>"+
			"<option value=''>13811100000</option>"+
			"</select></td></tr>"+
			"<tr style='font-weight:bold;'><td style='font-size:14px;height:27px;'>"+areaName+"：</td>"+
			"<td><select id='areabox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(3)'>"+
			"<option value=''>全部</option>"+
			"<option value=''>13811100000</option>"+
			"</select></td></tr>"+
			"<tr style='font-weight:bold;'><td style='font-size:14px;height:27px;'>"+branName+"：</td>"+
			"<td><select id='branchactiongroupbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' >"+
			"<option value=''>全部</option>"+
			"<option value=''>13811100000</option>"+
			"</select></td></tr><tr style='font-weight:bold;'><td style='font-size:14px;height:27px;'>类型：</td>"+
			"<td><select id='numtype' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;'>"+
			vNumtype+
			"</select></td></tr>"+
		"</table> "+
		"<div style='margin-bottom:10px;margin-top:10px;'>"+
		"<table width='330' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel' style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
		"<input class='sure' style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='check();' /> </div>"
		});
		wBox.showBox();
		selectNumType(nutype);

}

function selectNumType(nutype)
{
	var vnumtype = document.getElementById('numtype');
	if(varshowType ==0 || varshowType ==1)
		vnumtype.selectedIndex=0;
	else	
		vnumtype.selectedIndex=nutype;

}
 
 function check()
{
	var ssmnnumber = document.getElementById('ssmnnumber').value;
	if(ssmnnumber == '')
	{
		alert('请输入副号码!');
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
	var vvtype = document.getElementById('numtype').value;

	//判断所编辑的内容是否与之前一致,因为行动组唯一，所以只需要判断行动组名即可
	if(ssmnnumber==varssmnnumber && branchactiongroup == varbranchactiongroup && vtype==vvtype)
	{
		alert('修改');
		return false;
	}
		
	//alert(agentmsisdn);
	var varul="modifyssmnnumber.do?method=edit&bd="+businessdepartment+"&warzone="+warzone+"&area="+area+"&bag="+branchactiongroup+"&msisdn="+ssmnnumber;
	
	var selectbdbox1 = document.getElementById("bdbox1");//事业部
 	var selectzonebox1 = document.getElementById("zonebox1");//战区
 	var selectareabox1 = document.getElementById("areabox1");//片区
 	var selectbagbox1 = document.getElementById("bagbox1");//行动组
	var selectssmnnum = document.getElementById("ssmnnum").value;
	var selectstatusbox = document.getElementById("statusbox");

	varul += "&ssmnnum="+selectssmnnum;
	
	if(selectbdbox1.selectedIndex != 0)
		varul +="&bdbox1="+selectbdbox1.value;
	if(selectzonebox1.selectedIndex != 0)
		varul +="&zonebox1="+selectzonebox1.value;
	if(selectareabox1.selectedIndex != 0)
		varul +="&areabox1="+selectareabox1.value;
	if(selectbagbox1.selectedIndex != 0)
		varul +="&bagbox1="+selectbagbox1.value;
	if(selectstatusbox.selectedIndex != 0)
		varul +="&statusbox="+selectstatusbox.value;
	//类型
	varul+="&numtype="+vvtype;
	
	document.location.href=varul;
}
 
 
function deleteAgent(msisdn){
agentmsisdn = msisdn;
 wBox=jQuery("	#wbox10").wBox({
		title: "",
		html: "<div style='width:350px;height:100px;'>"+
		"<table style='margin-top:5px;margin-left:40px;width:90%;'>"+
		"<tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+
		"<td>确认是否删除该经纪人？删除后解绑所有副号码！<br><br></td> </tr><br>  </table> "+
		"<div style='margin-bottom:2px;'>"+
		"<table width='350' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input  style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' /><input  style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='deletecheck();' /> </div>"
		});
		wBox.showBox();
	
}

function deletecheck(){
document.location.href="manageagent.do?method=delete&msisdn="+agentmsisdn ;

}

function bindAgent(sname,smsisdn,strChannel,uid,stname)
{
	sendRequestEN(uid);
		
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
		html: "<div style='width:330px;height:170px;'><table style='margin-top:0px;margin-left:30px;width:80%;' cellpadding='9px'><tr style='margin-top:0px;margin-bottom:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'><td height='20' >经纪人：</td><td>"+sname+
		"</td></tr><tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'><td height='20' >主号码：</td><td>"+smsisdn+"</td></tr><tr style='margin-top:20px;margin-bottom:50px;'><td height='30'>新增副号码：</td><td><input type='text' id='ssmnnumbervalue' style='width:90px ; height:15px;' value='' /><font color='red'>&nbsp;随机分配,可编辑</font></td></tr><tr><td height='30' >渠道：</td><td><select id='ssmnchannelvalue' style='width:150px ; height:20px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1;'><option value=''>请选择渠道</option>"+
		opchannelstr+"</select></td></tr></table> <div style='margin-bottom:10px;margin-top:10px;'><table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input  style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' /><input  style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='bindButoon("+smsisdn+","+uid+");' /> </div>"
		});
		
		wBox.showBox();
	}
	
}

//绑定
function bindButoon(smsisdn,suid)
{
	//wBox.close();
	var smvalue = document.getElementById('ssmnnumbervalue').value;
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
	
	document.location.href="manageagent.do?method=bind&strsmnnumber="+smvalue+"&&channelvalue="+scvalue+"&&bindmsisdn="+smsisdn+"&&uid="+suid+"&name="+selectName;
}

function exportInfo()
{
	sendRequestCDR();
}

function sendRequestCDR(){
 	createXMLHttpRequest();

 	var bdbox1 = document.getElementById("bdbox1");//事业部
 	var zonebox1 = document.getElementById("zonebox1");//战区
 	var areabox1 = document.getElementById("areabox1");//片区
 	var bagbox1 = document.getElementById("bagbox1");//行动组
	var ssmnnum = document.getElementById("ssmnnum").value;
	var statusbox = document.getElementById("statusbox");
	var typebox = document.getElementById('enmtype');
 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseCDR;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl = "type=4&ssmnnum="+ssmnnum;
	if(bdbox1.selectedIndex != 0)
		vurl +="&bdbox1="+bdbox1.value;
	if(zonebox1.selectedIndex != 0)
		vurl +="&zonebox1="+zonebox1.value;
	if(areabox1.selectedIndex != 0)
		vurl +="&areabox1="+areabox1.value;
	if(bagbox1.selectedIndex != 0)
		vurl +="&bagbox1="+bagbox1.value;
	if(statusbox.selectedIndex != 0)
		vurl +="&statusbox="+statusbox.value;
	if(typebox.selectedIndex != 0)
		vurl +="&enmtype="+typebox.value;
		
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

function infoDetail(ssmnnumber,type)
{
	if(ssmnnumber == '')
		return false;
	var bdbox1 = document.getElementById("bdbox1");//事业部
 	var zonebox1 = document.getElementById("zonebox1");//战区
 	var areabox1 = document.getElementById("areabox1");//片区
 	var bagbox1 = document.getElementById("bagbox1");//行动组
	var ssmnnum = document.getElementById("ssmnnum").value;
	var statusbox = document.getElementById("statusbox");
	var typebox = document.getElementById('enmtype');
	var pagetemp = document.getElementById("pagetemp").value;
	if(type == 1)
	{
		//业主副号码信息
		var vurl = "modifyssmnnumber.do?method=ownerInfo&ssmnnumber="+ssmnnumber;
		if(bdbox1.selectedIndex != 0)
			vurl +="&bdbox1="+bdbox1.value;
		if(zonebox1.selectedIndex != 0)
			vurl +="&zonebox1="+zonebox1.value;
		if(areabox1.selectedIndex != 0)
			vurl +="&areabox1="+areabox1.value;
		if(bagbox1.selectedIndex != 0)
			vurl +="&bagbox1="+bagbox1.value;
		if(statusbox.selectedIndex != 0)
			vurl +="&statusbox="+statusbox.value;
		if(typebox.selectedIndex !=0)
			vurl +="&enmtype="+typebox.value;
		document.location.href=vurl;
	}
	else
	{
		//经纪人副号码信息
		var vurl = "queryssmn.do?type=7&ssmnnumber="+ssmnnumber;
		if(bdbox1.selectedIndex != 0)
			vurl +="&selectbdbox1="+bdbox1.value;
		if(zonebox1.selectedIndex != 0)
			vurl +="&selectzonebox1="+zonebox1.value;
		if(areabox1.selectedIndex != 0)
			vurl +="&selectareabox1="+areabox1.value;
		if(bagbox1.selectedIndex != 0)
			vurl +="&selectbagbox1="+bagbox1.value;
		if(statusbox.selectedIndex != 0)
			vurl +="&selectstatusbox="+statusbox.value;
		if(ssmnnum != '')
			vurl +="&selectssmnnum="+ssmnnum;
		if(typebox.selectedIndex !=0)
			vurl +="&selectenmtype="+typebox.value;
		if(pagetemp != '')
			vurl +="&pagetemp="+pagetemp;
		document.location.href=vurl;
	}
	
}
</script>
</head>

<body topmargin="0" onLoad="init('${msg}');">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">副号码管理</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;"> 
<input type="hidden" id="pagetemp" value="${pagetemp }" />
<form method="post" action="modifyssmnnumber.do?method=queryNumber" name="mainfm">
	<table width="100%" align="center">
	    <tr style="height:40px;">
			<td class="t6" style="text-indent:10px;">${businName }：</td>
			<td class="t7" style="width:190px;">
				<select class="select3" id='bdbox1' name='bdbox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(1)'>
					<option value=''>全部</option>
					<c:forEach items="${bdlist}" var="bd" varStatus="s">
						<option value='${bd }' <c:if test="${bdparam==bd}">selected='true'</c:if>>${bd }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${warName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='zonebox1' name='zonebox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(2)'>
					<option value=''>全部</option>
					<c:forEach items="${wzlist}" var="wz" varStatus="s">
						<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${areaName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='areabox1' name='areabox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(3)'>
					<option value=''>全部</option>
					<c:forEach items="${arealist}" var="ar" varStatus="s">
						<option value='${ar }' <c:if test="${areaparam==ar}">selected='true'</c:if>>${ar }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${branName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='bagbox1' name='bagbox1' style='width:120px ; height:20px;'>
					<option value=''>全部</option>
					<c:forEach items="${baglist}" var="bag" varStatus="s">
						<option value='${bag }' <c:if test="${bagparam==bag}">selected='true'</c:if>>${bag }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr style="height:40px;">
	    	<td class="t6" style="text-indent:10px;">副号码：</td>
	    	<td class="t7" style="width:120px;height:40px;" >
	    		<input style="width:148px;" class="scinput" id="ssmnnum" name="ssmnnum" value="${ssmnnum}" type="text" size="20" maxlength="11" />
	    	</td>
	    	<td class="t6" style="text-indent:10px;">状态：</td>
	    	<td class="t7">
		    	<select class="select3" id='statusbox' name='statusbox' style='width:120px ; height:20px;'>
					<option value=''>全部</option>
					<option value='1' <c:if test="${status==1}">selected='true'</c:if>>使用</option>
					<option value='0' <c:if test="${status==0}">selected='true'</c:if>>未使用</option>
				</select>
			</td>
			<td class="t6" style="text-indent:10px;">类型：</td>
	    	<td class="t7">
		    	<select class="select3" id='enmtype' name='enmtype' style='width:120px ; height:20px;'>
					<option value=''>全部</option>
					<c:forEach items="${typeList}" var="u" >
						<c:if test="${u==0 &&(showType ==0 || showType ==2 )}">
					    	<option value='${u}' <c:if test="${enmtype==0}">selected='true'</c:if>>渠道副号码</option>
					    </c:if>
					    <c:if test="${u==1 && showType ==2 }">
							<option value='${u}' <c:if test="${enmtype==1}">selected='true'</c:if>>业主副号码</option>
						</c:if>
						<c:if test="${u==2 &&(showType ==1 || showType ==2 )}">
							<option value='${u}' <c:if test="${enmtype==2}">selected='true'</c:if>>A+副号码</option>
						</c:if>
		 			</c:forEach>
				</select>
			</td>
	    	 <td class="t7" colspan="2" >
	  			<input class="scbtn" type="submit" value="查询" />  	
	  		</td>
	    </tr>
	    <tr>
	        <td height="6" colspan="8" bgcolor="ECECF5"></td>
	    </tr> 
	    <tr><td>&nbsp;</td></tr>
	</table>
</form>

<table class="tablelist">
	<thead>
		<tr>
			<th>副号码</th>
			<th>${businName }</th>
			<th>${warName }</th>
			<th>${areaName }</th>
			<th>${branName }</th>
			<th>状态</th>
			<th>类型</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
    	<c:forEach items="${erlist}" var="u" varStatus="s">
		    <tr>
			    <td><a href="javascript:" onClick="infoDetail('${u.enablenumber}','${u.type }');"><u>${u.enablenumber}</u></a></td>
			    <td>${u.businessdepartment}</td>
			    <td>${u.warzone}</td>
			    <td>${u.area}</td>
			    <td>${u.branchactiongroup}</td>
	 			<td> 
	 				<c:if test="${u.status==0}">未使用</c:if>
					<c:if test="${u.status==1}">已使用</c:if>
	 			</td> 
 				<td>
	 				<c:if test="${u.type==0}">渠道副号码</c:if>
	 				<c:if test="${u.type==1}">业主副号码</c:if>
	 				<c:if test="${u.type==2}">A+副号码</c:if>
	 			</td> 
	 			<td> 
	 				<c:if test="${u.status==0 && (u.type==0 || u.type==2)}">
	 					<a href="javascript:" style="color:#056dae;" onClick="editNumber('${u.enablenumber}','${u.businessdepartment}','${u.warzone}','${u.area}','${u.branchactiongroup}','${businName }','${warName }','${areaName }','${branName }','${u.type }','${showType}');">编辑</a>
	 				</c:if>
	 			</td> 
		    </tr> 
	    </c:forEach>
    </tbody>
</table>

<form method="post" action="modifyssmnnumber.do?method=queryNumber" name="pageForm">
<input type="hidden" name="bdbox1" value="${bdparam }" />
<input type="hidden" name="zonebox1" value="${zoneparam }" />
<input type="hidden" name="areabox1" value="${areaparam }" />
<input type="hidden" name="bagbox1" value="${bagparam }" />
<input type="hidden" name="statusbox" value="${status}" />
<input type="hidden" name="ssmnnum" value="${ssmnnum }" />
<input type="hidden" name="enmtype" value="${enmtype}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="modifyssmnnumber.do?method=queryNumber&page="/>
</form>

<br/>
<br/>
<c:if test="${recordCount >0}">
 <div style="width:100%;" align="right" > <input class="scbtn1"  type="button" value="副号码信息导出" onClick="exportInfo()" /></div> 
</c:if>
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>

</body>
</html>

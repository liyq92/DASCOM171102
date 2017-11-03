<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
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
		width : 150
	});
});
</script>

<%  
  response.setHeader("Cache-Control",   "Public");  
  response.setHeader("Pragma",   "no-cache");  
  response.setDateHeader("Expires",   0);   
%>

<script language="JavaScript" type="text/JavaScript">
var wBox;
var vid;//暂存id
var vbusiness;//暂存事业部
var vwarzone;//暂存战区
var varea;//暂存片区
var vbranch;//暂存行动组别

var vbusinName;
var vwarName;
var vareaName;
var vbranName;

function delLevel(mid,mbusinessdepartment,mwarzone,marea,mbranchactiongroup){
		
	vid =mid;
	vbusiness = mbusinessdepartment;
	vwarzone = mwarzone;
	varea = marea;
	vbranch = mbranchactiongroup;
	
	wBox=$("#wbox2").wBox({
	title: "",
	html: "<div style='width:350px;height:120px;'>"+
		"<table style='margin-top:5px;margin-left:100px;width:90%;'>"+
		"<tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+
		"<td style='font-size:14px;'>确定要删除该级别吗？<br><br></td> </tr><br>  </table> "+
		"<div style='margin-bottom:2px;'>"+
		"<table width='350' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel' style='margin-top:10px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
		"<input class='sure' style='margin-top:10px;margin-left:20px;width:30%;' type='button' value='确定' onclick='delconf();' /> </div>"
		});
	wBox.showBox();	
}
function delconf(){
	if(vid == '')
	{
		alert('该组别的序号为空,不合法!');
		return;
	}
	document.location.href="levelmanage.do?type=del&mid="+vid+"&mbusin="+vbusiness+"&mwarzone="+vwarzone+"&marea="+varea+"&mbranch="+vbranch;
	wBox.close();
}
function cancel(){
	wBox.close();
}
function init(msg,openo1) {

	if(msg != ''){
		wBox=$("#wbox3").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
}
function exportlevel(){

	sendRequestExport();
}

function sendRequestExport(){
 	createXMLHttpRequest();
 	var selectvbdbox1 = document.getElementById("bdbox1");
 	var selectvzonebox1 = document.getElementById("zonebox1");
 	var selectvareabox1 = document.getElementById("areabox1");
 	var selectvbagbox1 = document.getElementById("bagbox1");
 	 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseExport;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

	var varurl="type=8";
	if(selectvbdbox1.selectedIndex != 0)
		varurl +="&bdbox1="+selectvbdbox1.value;
	if(selectvzonebox1.selectedIndex != 0)
		varurl +="&zonebox1="+selectvzonebox1.value;
	if(selectvareabox1.selectedIndex != 0)
		varurl +="&areabox1="+selectvareabox1.value;
	if(selectvbagbox1.selectedIndex != 0)
		varurl +="&bagbox1="+selectvbagbox1.value;

	XMLHttpReq.send(varurl);
}

function processResponseExport(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageExport();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }
 
  function parseMessageExport()
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

function editbut(uid,bd,warzone,area,bag,businName ,warName ,areaName,branName )
{
	vid =uid;
	vbusiness = bd;
	vwarzone = warzone;
	varea = area;
	vbranch = bag;
	vbusinName = businName;
	vwarName = warName;
	vareaName = areaName;
	vbranName = branName;
	
	//查询对应的级别列表
       sendRequest2(bd,warzone,area,bag);

		if((bag == '' || bag=='全部') && (area =='' || area=='全部')&& (warzone == '' || warzone=='全部') && (bd !='' && bd !='全部'))//事业部级别
		{
			var vhtml = "<div style='width:330px;height:180px;'>"+
						"<table style='margin-left:40px;width:80%;margin-top: -15px;'>";
			vhtml +="<tr>";
			vhtml +="<td style='font-size:14px;height:27px;'>"+businName+"：</td>";
			vhtml +="<td><input  style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' id='bdbox1Txt' name='bdbox1Txt' value='"+bd+"'  />";
			vhtml +="</td></tr><br>";
			vhtml +="<tr><td style='font-size:14px;height:27px;'>"+warName+"：</td>"+
					"<td><select id='warzonebox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' disabled='disabled' onchange='changeSelect(2)'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>"+
					"<tr><td style='font-size:14px;height:27px;'>"+areaName+"：</td>"+
					"<td><select id='areabox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' disabled='disabled' onchange='changeSelect(3)'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>"+
					"<tr><td style='font-size:14px;height:27px;'>"+branName+"：</td>"+
					"<td><select id='branchactiongroupbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' disabled='disabled'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>";
			vhtml +="</table> "+
		"<div style='margin-bottom:10px;margin-top:10px;'>"+
		"<table width='330' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel' style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
		"<input class='sure' style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='editLevel(1);' /> </div>";
		}
		if((bag == '' || bag=='全部') && (area =='' || area=='全部')&& (warzone != '' && warzone!='全部') && (bd !='' && bd !='全部'))//战区级别
		{
			var vhtml = "<div style='width:330px;height:180px;'>"+
						"<table style='margin-left:40px;width:80%;margin-top: -30px;'>";
			vhtml +="<tr>";
			vhtml +="<td style='font-size:14px;height:27px;'>"+businName+"：</td>";
			vhtml +="<td> <select id='businessdepartmentbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(1)'>"+
			"<option value=''>全部</option></select>";
			vhtml +="</td></tr><br>";
			vhtml +="<tr><td style='font-size:14px;height:25px;'>"+warName+"：</td>";
			vhtml +="<td><input  style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' id='zonebox1Txt' name='zonebox1Txt' value='"+warzone+"'  />";
			vhtml +="</td></tr><br>";
			vhtml +="<tr><td style='font-size:14px;height:27px;'>"+areaName+"：</td>"+
					"<td><select id='areabox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' disabled='disabled' onchange='changeSelect(3)'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>"+
					"<tr><td style='font-size:14px;height:27px;'>"+branName+"：</td>"+
					"<td><select id='branchactiongroupbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' disabled='disabled'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>";
			vhtml +="</table> "+
		"<div style='margin-bottom:10px;margin-top:10px;'>"+
		"<table width='330' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel' style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
		"<input class='sure' style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='editLevel(2);' /> </div>";
		}
		if((bag == '' || bag=='全部') && (area !='' && area !='全部')&& (warzone != '' && warzone!='全部') && (bd !='' && bd !='全部'))//片区级别
		{
			var vhtml = "<div style='width:330px;height:180px;'>"+
						"<table style='margin-left:40px;width:80%;margin-top: -30px;'>";
			vhtml +="<tr>";
			vhtml +="<td style='font-size:14px;height:27px;'>"+businName+"：</td>";
			vhtml +="<td> <select id='businessdepartmentbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(1)'>"+
					"<option value=''>全部</option></select>";
			vhtml +="</td></tr><br>";
			vhtml +="<tr ><td style='font-size:14px;height:27px;'>"+warName+"：</td>"+
					"<td><select id='warzonebox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(2)'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>";
			vhtml +="<tr ><td style='font-size:14px;height:25px;'>"+areaName+"：</td>";
			vhtml +="<td><input  style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' id='areabox1Txt' name='areabox1Txt' value='"+area+"'  />";
			vhtml +="</td></tr><br>";
			vhtml +="<tr ><td style='font-size:14px;height:27px;'>"+branName+"：</td>"+
					"<td><select id='branchactiongroupbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' disabled='disabled'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>";
					
			vhtml +="</table> "+
		"<div style='margin-bottom:10px;margin-top:10px;'>"+
		"<table width='330' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel' style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
		"<input class='sure' style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='editLevel(3);' /> </div>";
		}
		if((bag != '' && bag !='全部') && (area !='' && area !='全部')&& (warzone != '' && warzone!='全部') && (bd !='' && bd !='全部'))//行动组级别
		{
			var vhtml = "<div style='width:330px;height:180px;'>"+
						"<table style='margin-left:40px;width:80%; margin-top: -30px;'>";
			vhtml +="<tr>";
			vhtml +="<td style='font-size:14px;height:27px;'>"+businName+"：</td>";
			vhtml +="<td> <select id='businessdepartmentbox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(1)'>"+
					"<option value=''>全部</option></select>";
			vhtml +="</td></tr><br>";
			vhtml +="<tr><td style='font-size:14px;height:27px;'>"+warName+"：</td>"+
					"<td><select id='warzonebox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;' onchange='changeSelect(2)'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>";
			vhtml +="<tr ><td style='font-size:14px;height:27px;'>"+areaName+"：</td>";
			vhtml +="<td><select id='areabox' style='font-size:14px;width:152px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;'  onchange='changeSelect(3)'>"+
					"<option value=''>全部</option>"+
					"</select></td></tr>";
			vhtml +="<tr ><td style='font-size:14px;height:25px;'>"+branName+"：</td>";
			vhtml +="<td> <input  style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' id='bagbox1Txt'  name='bagbox1Txt' value='"+bag+"'  />";
			vhtml +="</td></tr><br>";
			
			vhtml +="</table> "+
		"<div style='margin-bottom:10px;margin-top:10px;'>"+
		"<table width='330' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel'  style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
		"<input class='sure' style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='editLevel(4);' /> </div>";
		}
		
        wBox=$("#wbox10").wBox({
		title: "",
		html: vhtml
		});
		wBox.showBox();
	 
}

function editLevel(vtype)
{
	if(vtype == 1)//事业部级
	{
		//获取新的值
		var vbusinNew = document.getElementById('bdbox1Txt').value;
		if(vbusinNew == '')
		{
			alert('请输入'+vbusinName);
			return;
		}
		if(vbusinNew == vbusiness)
		{
			alert('没有改变!');
			return;
		}
		document.location.href="levelmanage.do?type=edit&chtype=1&mbusinNew="+vbusinNew+"&vbusinOld="+vbusiness;
	}
	if(vtype == 2)//战区级
	{
		var vwarzoneNew = document.getElementById('zonebox1Txt').value;
		var vbusinNew = document.getElementById('businessdepartmentbox').value;
		if(vwarzoneNew == '')
		{
			alert('请输入'+vwarName);
			return;
		}
		if(vbusinNew == '' || vbusinNew =='全部')
		{
			alert('请选择'+vbusinName);
			return;
		}
		if(vwarzoneNew == vwarzone && vbusinNew==vbusiness)
		{
			alert('没有改变!');
			return;
		}
		document.location.href="levelmanage.do?type=edit&chtype=2&mbusinNew="+vbusinNew+"&mbusinOld="+vbusiness+"&mwarzoneNew="+vwarzoneNew+"&mwarzoneOld="+vwarzone;
	}
	if(vtype == 3)
	{
		var vbusinNew = document.getElementById('businessdepartmentbox').value;
		var vwarezoneNew = document.getElementById('warzonebox').value;
		var vareNew = document.getElementById('areabox1Txt').value;
		if(vareNew == '')
		{
			alert('请输入'+vareaName);
			return;
		}
		if(vwarezoneNew =='' || vwarezoneNew=='全部')
		{
			alert('请选择'+vwarName);
			return;
		}
		if(vbusinNew =='' || vbusinNew=='全部')
		{
			alert('请选择'+vbusinName);
			return;
		}
		if(vwarezoneNew == vwarzone && vbusinNew==vbusiness && vareNew==varea)
		{
			alert('没有改变!');
			return;
		}
		document.location.href="levelmanage.do?type=edit&chtype=3&mbusinNew="+vbusinNew+"&mbusinOld="+
		vbusiness+"&mwarzoneNew="+vwarezoneNew+"&mwarzoneOld="+vwarzone+
		"&mareaNew="+vareNew+"&mareaOld="+varea;
	
	}
	if(vtype == 4)
	{
		var vbusinNew = document.getElementById('businessdepartmentbox').value;
		var vwarezoneNew = document.getElementById('warzonebox').value;
		var vareNew = document.getElementById('areabox').value;
		var vbranchNew = document.getElementById('bagbox1Txt').value;
		if(vbranchNew == '')
		{
			alert('请输入'+vbranName);
			return;
		}
		if(vareNew == '')
		{
			alert('请选择'+vareaName);
			return;
		}
		if(vwarezoneNew =='' || vwarezoneNew=='全部')
		{
			alert('请选择'+vwarName);
			return;
		}
		if(vbusinNew =='' || vbusinNew=='全部')
		{
			alert('请选择'+vbusinName);
			return;
		}
		if(vwarezoneNew == vwarzone && vbusinNew==vbusiness && vareNew==varea && vbranchNew==vbranch)
		{
			alert('没有改变!');
			return;
		}
		document.location.href="levelmanage.do?type=edit&chtype=4&mbusinNew="+vbusinNew+"&mbusinOld="+
		vbusiness+"&mwarzoneNew="+vwarezoneNew+"&mwarzoneOld="+vwarzone+
		"&mareaNew="+vareNew+"&mareaOld="+varea+
		"&mbranchNew="+vbranchNew+"&mbranchOld="+vbranch;
	
	}
	
}

//点击选择各等级下拉菜单  
function changeSelect(type){
    if(type == '1'){
        var opObj = document.getElementById('businessdepartmentbox');
        
	       var select_root=document.getElementById('warzonebox'); 
	       if(  select_root != null)
	       { 
   	       		select_root.options.length=0;
   	       		var defa = new Option("全部","");
     	   		select_root.add(defa);
     	   }
     	   
		   var select_root1=document.getElementById('areabox'); 
		   if(select_root1 !=null)
		   {
   	       		select_root1.options.length=0;
   	       		var defa1 = new Option("全部","");
     	   		select_root1.add(defa1);
     	   }

     	   var select_root2=document.getElementById('branchactiongroupbox');  
     	   if(select_root2 != null)
     	   {
   	       		select_root2.options.length=0;
   	      		var defa2 = new Option("全部","");
     	   		select_root2.add(defa2);
     	   }
     	   
     	   if(opObj !=null && opObj.selectedIndex!=0){
		      sendRequest(type);
	        }
    }else if(type == '2'){
          var opObj = document.getElementById('warzonebox'); 
	       //alert(opObj.selectedIndex);
		   var select_root1=document.getElementById('areabox'); 
		   if(select_root1 !=null)
		   {
   	      		select_root1.options.length=0;
   	       		var defa1 = new Option("全部","");
     	   		select_root1.add(defa1);
     	   	}
     	
     	   var select_root2=document.getElementById('branchactiongroupbox');   
     	   if( select_root2 !=null)
     	   {
   	       		select_root2.options.length=0;
   	       		var defa2 = new Option("全部","");
     	   		select_root2.add(defa2);
     	   }
     	   
     	   if(opObj !=null && opObj.selectedIndex!=0){
     	      sendRequest(type);
     	   }
	    
    }else{
        var opObj = document.getElementById('areabox'); 
	    //alert(opObj.value);
     	var select_root2=document.getElementById('branchactiongroupbox');   
     	if( select_root2 !=null)
     	{
   	    		select_root2.options.length=0;
   	    		var defa2 = new Option("全部","");
     			select_root2.add(defa2);
     	}
	    if(opObj !=null && opObj.selectedIndex!=0){
		   sendRequest(type);
	    }
    }
    
}

function sendRequest(type){
 	createXMLHttpRequest();
 	var opObj = document.getElementById('businessdepartmentbox'); 
 	var opObj1 = document.getElementById('warzonebox'); 
    var opObj2 = document.getElementById('areabox'); 

 	XMLHttpReq.open("POST","${ctx}/selectlevel",true);
	XMLHttpReq.onreadystatechange = processResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	var vurl = "type="+type+"&bd=";
	if(opObj == null)
		vurl +='';
	else
		vurl +=opObj.value;
	vurl +="&warzone=";
	if(opObj1 == null)
		vurl +='';
	else
		vurl +=opObj1.value;
	vurl += "&area=";
	if(opObj2 == null)
		vurl +='';
	else
		vurl +=opObj2.value;
	
	XMLHttpReq.send(vurl);
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
  	     if(select_root !=null)
  	     {
  	     	select_root.options.length=0;
       	 	defa = new Option("全部",""); 
       	 }
  	}else if(type == '2'){
  	     select_root=document.getElementById('areabox');  
  	     if(select_root !=null)
  	     {
  	     	select_root.options.length=0;
       	 	defa = new Option("全部","");  	
       	 } 
  	}else{
  		 if(select_root !=null)
  	     {
  	     	select_root.options.length=0;
       	 	defa = new Option("全部",""); 
       	 }
  	}
  if(select_root !=null)
  {
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
</script>
</head>
<body onLoad="init('${msg}');" topmargin="0">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">架构管理</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<input type="hidden" id="pagetemp" value="${pagetemp }" />	
<form method="post" action="levelmanage.do" name="mainfm" >  
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
    	<td class="t6" style="text-indent:20px;">${warName }：</td>
    	<td class="t7">
	    	<select class="select3" id='zonebox1' name='zonebox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(2)' <c:if test="${ slevelid==6 || slevelid==5 || slevelid==4 }"> disabled="disabled" </c:if> >
				<option value=''>全部</option>
				<c:forEach items="${wzlist}" var="wz" varStatus="s">
					<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
				</c:forEach>
			</select>
		</td>
    	<td class="t6" style="text-indent:20px;">${areaName }：</td>
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
    <td class="t7" colspan="8">
		<div style="text-align:center;">
			<input class="scbtn" type="submit" value="查询"/>&nbsp;&nbsp;&nbsp;
	       	&nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	       	<input class="scbtn" type="button" value="新增架构" onClick="javascript:document.location='levelmanage.do?type=preAdd'"/>&nbsp;&nbsp;&nbsp;
		</div> 		  	
  	</td>  
    </tr>
    <tr>
        <td height="6" colspan="8" bgcolor="ECECF5">
	</td>
    </tr> 
     <tr><td>&nbsp;</td></tr>     
 </table>  
</form>

<table class="tablelist">
	<thead>        
  	<tr> 
        <th><b>${businName }</b></th>
        <th><b>${warName }</b></th>
        <th><b>${areaName }</b></th>
        <th><b>${branName }</b></th>
        <th><b>操作</b></th>
 	</tr>
 	</thead>
 	<c:forEach items="${llist}" var="m">
 		<tbody>
        <tr> 
          <td>${m.businessdepartment}</td>
          <td>${m.warzone}</td>
          <td>${m.area}</td>
          <td>${m.branchactiongroup}</td>
          <td>
          	<a href="#" style="color:#056dae;" onclick="editbut('${m.id}','${m.businessdepartment}','${m.warzone}','${m.area}','${m.branchactiongroup}','${businName }','${warName }','${areaName }','${branName }');" >编辑</a>&nbsp;&nbsp;&nbsp;
          	<a href="#" style="color:#056dae;" onClick="delLevel('${m.id}','${m.businessdepartment}','${m.warzone}','${m.area}','${m.branchactiongroup}');">删除</a>
          </td>
        </tr>
        </tbody>
 	</c:forEach>
</table>
<form method="post" action="levelmanage.do" name="pageForm">
<input type="hidden" name="bdbox1" value="${bdparam}" />
<input type="hidden" name="zonebox1" value="${zoneparam}" />
<input type="hidden" name="areabox1" value="${areaparam}" />
<input type="hidden" name="bagbox1" value="${bagparam}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="levelmanage.do?page="/>	  			
</form>
<br/>
<br/>
<div style="width:100%;" align="right" >	<input class="scbtn1" style="width:120px ; height:30px;" type="button" value="架构信息导出" onclick="exportlevel();" /></div> 
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>

</body>
</html>


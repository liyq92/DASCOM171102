<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>

<script language="JavaScript" type="text/JavaScript">
<!--
var XMLHttpReq = false;
var serviceKey;
var checkBoxIndex = 0;

function reloadAuth(){
  sendRequest();
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

function sendRequest(){
 	createXMLHttpRequest();
	XMLHttpReq.open("POST","${ctx}/loadAuth",true);
	XMLHttpReq.onreadystatechange = processResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	//var opObj = document.forms[0].rank;
	//XMLHttpReq.send("flag=mod&"+"rank=0"+opObj.options[opObj.selectedIndex].value+"&groupid="+${group.id});
	XMLHttpReq.send("flag=mod&"+"rank=0&groupid="+${group.id});
}

function processResponse(){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
			ringsetADD(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }


function ringsetADD(responseText)
{
	showTable(document.all.resultTable);

	checkBoxIndex = 0;//给每个父选项，编号
	var fm=document.forms[0];
	var all=responseText.split("|");

	var auths=all[0].split("$");
	for(var i=0;i<auths.length;i++)
	{
		//标题写死了
		var vTitle;
		if(i == 0)
			vTitle ='※ 信息管理：';
		if(i == 1)
			vTitle ='※ 副号码通讯信息：';
		if(i == 2)
			vTitle ='※ 统计报表下载：';
		if(i == 3)
			vTitle ='※ 来电统计：';
		if(i == 4)
			vTitle ='※ 我的信息：';
		if(i == 5)
			vTitle ='※ 操作员管理：';
		if(i == 6)
			vTitle ='※ 数据显示：';
		var tableTitle =document.all.resultTable;
		var rowsTitle=tableTitle.rows.length;
		tableTitle.insertRow(rowsTitle);
		tableTitle.rows[rowsTitle].insertCell(0);
		tableTitle.rows[rowsTitle].cells[0].innerHTML=vTitle;
		
		var auSplit = auths[i].split("#");
		for(var m=0;m<auSplit.length;m++)
		{
			var ringset = new Array();
			var auMen = auSplit[m].split("%");
			for(var j=0;j<auMen.length;j++)
			{
				var au=auMen[j].split(";");
				var newSet=new Array(2);
				newSet[0]=au[0];
				newSet[1]=au[1];
				ringset[ringset.length]=newSet;
			}
			createRow(document.all.resultTable,ringset,checkBoxIndex);
			checkBoxIndex++;
		}
		var table =document.all.resultTable;
		var rowsBlack=table.rows.length;
		table.insertRow(rowsBlack);
		table.rows[rowsBlack].insertCell(0);
		table.rows[rowsBlack].cells[0].innerHTML='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
	}
	
	var all2=all[1].split("#");
	for(var i=0;i<all2.length-1;i++)
	{
		changSelSta(all2[i]);
	}
}

function clearTable(table)
{
	var rows1=table.rows.length;
	for(var i=rows1-1;i>0;i--)
        table.deleteRow(i);
}
function showTable(table)
{
	clearTable(table);
	var rows=table.rows.length;
	table.insertRow(rows);
	table.rows[rows].insertCell(0);
	table.rows[rows].cells[0].innerHTML='<input class="scbtn" name="selAll" type="button"  value="全选"  onclick="setAllchk(\'true\')" />&nbsp;&nbsp;&nbsp;&nbsp;<input class="scbtn" name="unselAll" type="button"  value="全不选" onclick="setAllchk()" />';
	table.rows[rows].cells[0].className="t9";
	table.rows[rows].cells[0].style.textAlign ="left";

}

function createRow(table,arr,checkBoxIndex)
{
	var i=0;
	while(i<arr.length)
	{
		var set=arr[i];
		//var maxCell=table.rows[0].cells.length;
		var rows=table.rows.length;
		table.insertRow(rows);
		table.rows[rows].insertCell(0);

		if(i == 0)
			table.rows[rows].cells[0].innerHTML='<input name="chk" type="checkbox" id="'+checkBoxIndex+'_'+i+'" onClick =checkChildren("'+checkBoxIndex+'_'+i+'"); value="'+set[0]+'">'+set[1];
		else
			table.rows[rows].cells[0].innerHTML='&nbsp;&nbsp;&nbsp;<input name="chk" type="checkbox"  id="'+checkBoxIndex+'_'+i+'" onClick =checkParent("'+checkBoxIndex+'_'+i+'");  value="'+set[0]+'">'+set[1];
		table.rows[rows].cells[0].className="t9";
		table.rows[rows].cells[0].style.textAlign ="left";
	
		i=i+1;
	}
	
}
//checkId是父级的，检查子级
function checkChildren(checkId)
{
	var parentCheckBox = document.getElementById(checkId);
	if(typeof(parentCheckBox) !="undefined")
	{
		var checkIdArray = checkId.split('_');
		for(var i=1;i<10;i++)
		{
			var childrenCheckbox = document.getElementById(checkIdArray[0]+'_'+i);
			if(typeof(childrenCheckbox) != "undefined")
			{
				if(parentCheckBox.checked == true)
					childrenCheckbox.checked =true;
				else
					childrenCheckbox.checked =false;
			}else
				break;
		}
	}
}
//checkId是子级，检查父级
function checkParent(checkId)
{
	var childCheckBox = document.getElementById(checkId);

	 if(typeof(childCheckBox) != "undefined")
 	{
 		var idArray =checkId.split('_');
		var parentCheckBox = document.getElementById(idArray[0]+'_0');
		if(typeof(parentCheckBox) != "undefined" )
		{
			if(childCheckBox.checked == true)
				 parentCheckBox.checked= true;
			//else
				//parentCheckBox.checked= false;
		}
		
	}
}

function   addOption(sel,value,text)   {   
  var   objSelect   =   document.getElementById(sel);
    
  //var   objSelect   =   sel; 
  var   objOption   =   document.createElement("OPTION");   
  objSelect.options.add(objOption);   
  objOption.innerText   =  text;   
  objOption.value   =   value;   
  }  
function init()
{
/*	 var level = parseInt(${level});
	 for(var i=level;i<1;i++)
	 {	
	 	addOption("rank",i,i);
	 } 
	 var sel=${group.rank};
	 var   objSelect   =   document.getElementById("rank");
	 for(var i=0;i<objSelect.length;i++)
	 {
	 	if(objSelect.options[i].value==sel)
	 	{
	 		objSelect.options[i].selected=true;
	 	}	
	 }*/
	 reloadAuth();
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function changSelSta(itemValue){
   var i;
   var opObj = document.forms[0].chk;
 //  var desObj = document.forms[0].selurlDes;
 if(typeof(opObj) != "undefined")
 {
 	 for (i=0;i<opObj.length;i++) {
      if (opObj[i].value == itemValue) {
        opObj[i].checked = true;
        break;
      }
   }
 }
  
}

function check(){
	 var fm=document.forms[0];
	if (fm.groupName.value=='') {
	     alert("权限组名不能为空！");  
	     fm.groupName.focus();
	     return false;
    }
	if (fm.description.value=='') {
	     alert("权限组描述不能为空！");  
	     fm.groupName.focus();
	     return false;
    }
	  var obj = document.forms[0].groupName;
		var exp = new RegExp("^[A-Za-z0-9_]*$");


   if (!isValuableChar(fm.description.value)) {
     alert("组描述必须是普通英文字符和中文！");  
     fm.description.focus();
     return false;
   }
   var opObj = fm.chk;
   if(typeof(opObj) != "undefined")
   {
   		
	   	if (opObj.length) {
		   for (i=0;i<opObj.length;i++) {
	
			  if (opObj[i].checked) {			
				   return true;		   
			  }
		   }
	   } else {
		   if (opObj.checked) {
	           return true;		
		   }
   }
   }
   alert("请为此分组至少选择一个权限！");
	 return false
}
function selAllchk(){

if(document.forms[0].selAll.value=="全选")
{
	document.forms[0].selAll.value="全不选";
	setallchk("tr");
}
else if(document.forms[0].selAll.value=="全不选")
{
	document.forms[0].selAll.value="全选";
	setallchk("");
}

}
function setAllchk(flag){
var num = document.forms[0].chk.length;
	for(var i=0;i<num;i++)
	{
		document.forms[0].chk[i].checked=flag;
	}
}
//-->
</script>
<style>
.dfinput
{
	width:188px;
}
</style>
</head>

<body topmargin="0" onLoad="init();reloadAuth();">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">修改权限组信息</a></li>
	</ul>
</div>

<form action="OPgroupModify.do" method="post" onSubmit="return check();">
<c:if test = "${not empty(msg)}">
	 <font color="red">${msg}</font>
</c:if>
	<table width="100%" align="center" style="border-style:none;margin-left:30px;margin-top:30px;">
        <tr style="height:40px;"> 
          <td class="t6" style="text-indent:10px;width:90px;"><label>权限组名：</label></td>
          <td class="t7" style="height:40px;border-right:none;">
			  <input class="dfinput" type="text" name="groupName" value="${group.groupName}" maxlength="32" /><font color="red">*</font>
			  <input type="hidden" name="groupId" value ="${group.id}" />
		  </td>
		  <td class="t7" style="border-left:none;">&nbsp;</td>
        </tr>
        <tr style="height:40px;"> 
          <td class="t6" style="text-indent:10px;width:90px;"><label>组描述：</label></td>
          <td class="t7" style="height:40px;border-right:none;">
          	<input class="dfinput" type="text" name="description" maxlength="64"  value ="${group.description}"><font color="red">*</font>
          </td>
          <td class="t7" style="border-left:none;">&nbsp;</td>
        </tr>
		<tr>
        	<td height="6" colspan="2"  bgcolor="ECECF5"></td>
      	</tr>
      </table>
	<br/>
<div align="center">
	 <table id="resultTable" width="18%" align="center">
	 	
		<tr>
	        
	    </tr>
	 </table>
</div>
	 <table id="resultTable1" width="100%" align="center">
		<tr>
	        <td height="6" colspan="2" bgcolor="ECECF5"></td>
	    </tr>
	 </table>	  
	<br/>
 <div align="center">
 	<input class="scbtn" type="submit" value="保存"/>&nbsp;&nbsp;&nbsp;
    <input class="scbtn" type="button" value="返回"  onClick="javascript:document.location='OPgroupSearch.do'"/>
 </div>  
</form>
</body>

</html>

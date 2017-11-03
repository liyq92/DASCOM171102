<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
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
var varexportbatchcancelfile;//导出结果
var vid;
var vumsisdn;
var vuname;
var vcnum;
var vcname;
var vremark;


function cancel(){
	wBox.close();
}
function init(msg,filenamebatchcan,sucesscount,faildcount) {
	if(filenamebatchcan != '')//显示批量处理结果
	{
		varexportbatchcancelfile = filenamebatchcan;
		 
		wBox=jQuery("#wbox10").wBox({
		title: "",
		html: "<div style='width:300px;height:100px;'><div style='text-align=center;margin-left:30px;margin-top:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+"结果:"+"成功"+sucesscount+"个;"+"失败"+faildcount+"个</div>"+
		"<div style='margin-bottom:5px;margin-top:10px;'><table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div><input class='scbtn'  style='margin-left:80px;margin-top:5px;width:40%;' type='button' value='"+"结果导出' onclick='exportImportbatchFile();' /> </div>"
		});

		wBox.showBox();
	}else
	{
		if(msg != ''){
			wBox=$("#wbox3").wBox({
			title: "返回结果",
			html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
			});
			wBox.showBox();
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
 
function editbut(uid,usermsisdn,username,clientnum,clientname )
{
	vid =uid;
	vumsisdn =usermsisdn;
	vuname =username;
	vcnum =clientnum;
	vcname =clientname;

	var vhtml = "<div style='width:330px;height:180px;'>"+
				"<table style='margin-left:40px;width:80%;margin-top: 7px;'>"+
			 		"<tr>"+
					"<td style='font-size:14px;height:27px;'>业务员电话：</td>"+
					"<td><input  style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' id='usermsisdnTxt' name='usermsisdnTxt' value='"+usermsisdn+"'  /></td>"+
					"</tr>"+
					"<tr><td style='font-size:14px;height:27px;'>业务员姓名：</td>"+
					"<td><input  style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' id='usernameTxt' name='usernameTxt' value='"+username+"'  /></td>"+
					"</tr>"+
					"<tr>"+
					"<td style='font-size:14px;height:27px;'>客户电话：</td>"+
					"<td><input  style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' id='clientnumTxt' name='clientnumTxt' value='"+clientnum+"'  /></td>"+
					"</tr>"+
					"<tr><td style='font-size:14px;height:27px;'>客户姓名：</td>"+
					"<td><input  style='font-size:14px;width:150px ; height:20px;border:1px;border-style:solid;' type='text' id='clientnameTxt' name='clientnameTxt' value='"+clientname+"'  /></td>"+
					"</tr>"+
			"</table> "+
			"<div style='margin-bottom:10px;margin-top:10px;'>"+
			"<table width='330' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
			" <input class='cancel' style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
			"<input class='sure' style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='editDeal();' /> </div>";
	
       wBox=$("#wbox10").wBox({
	title: "",
	html: vhtml
	});
	wBox.showBox();
	 
}

function editDeal()
{
	var vum = document.getElementById('usermsisdnTxt').value;
	var vun = document.getElementById('usernameTxt').value;
	var vcm = document.getElementById('clientnumTxt').value;
	var vcn = document.getElementById('clientnameTxt').value;
	
	if(vum =='')
	{
		alert('请输入业务员电话');
		return;
	}
	if(vun == '')
	{
		alert('请输入业务员姓名');
		return;
	}
	if(vcm == '')
	{
		alert('请输入客户电话');
		return;
	}
	if(vcn == '')
	{
		alert('请输入客户姓名');
		return;
	}
	if(vum == vumsisdn && vun==vuname && vcm==vcnum && vcn ==vcname)
	{
		alert('您没有做修改');
		return;
	}
	
	//校验客户电话和客户姓名
	sendRequest2(vum,vun,vcm,vcn);
}

function sendRequest2(vum,vun,vcm,vcn)
{
 	createXMLHttpRequest();
 	XMLHttpReq.open("POST","${ctx}/selectenablenumber",true);
	XMLHttpReq.onreadystatechange = processResponse2;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type=9&usermsisdn="+vum+"&username="+vun+"&clientnum="+vcm+"&clientname="+vcn);

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
    var all=XMLHttpReq.responseText; 
    all =all.replace(/\n/g,"");
    if(all =='')
    {
    	var vum = document.getElementById('usermsisdnTxt').value;
		var vun = document.getElementById('usernameTxt').value;
		var vcm = document.getElementById('clientnumTxt').value;
		var vcn = document.getElementById('clientnameTxt').value;
    	document.location.href="callmanage.do?method=edit&cid="+vid+"&umsisdn="+vum
    		+"&uname="+vun+"&cnum="+vcm+"&cname="+vcn+"&usermsisdnOld="+vumsisdn+
    		"&usernameOld="+vuname;
    }else
    	alert(all);
    
}

function delbut(mid){
		
	vid =mid;
		
	wBox=$("#wbox2").wBox({
	title: "",
	html: "<div style='width:350px;height:120px;'>"+
		"<table style='margin-top:5px;margin-left:100px;width:90%;'>"+
		"<tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+
		"<td style='font-size:14px;'>确定要删除该客户吗？<br><br></td> </tr><br>  </table> "+
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
		alert('序号不能为空!');
		return;
	}
	document.location.href="callmanage.do?method=del&cid="+vid;
	wBox.close();
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

function checkimportbatch()
{
	var vfile = document.getElementById("uploadfileImport").value;
	if(vfile == '')
	{
		alert('请上传文件!');
		return false;
	}
}

function showuploadFileImport()
{
	document.getElementById("uploadfile_import_form").style.display= "inline";
}

function importbatch()
{
	var sre = "${ctx}/exportExcelTemp/calltemp.xls";
		document.location=sre;
}

function exportImportbatchFile()
{
	wBox.close();
	if(varexportbatchcancelfile !='')
		document.location="${ctx}/callResult/"+varexportbatchcancelfile;
}

function showRemarkBox(mid,remarkstr)
{
	vid = mid;
	vremark = remarkstr;
	
	wBox=jQuery("#wbox10").wBox({
	title: "添加备注",

	html: "<div style='width:380px;height:280px;'><textarea style='margin-top:10px;margin-left:10px;width:350px;height:186px;' id='remark' name='remark'>"+remarkstr+"</textarea>" +
		  "<br/>&nbsp;&nbsp;&nbsp;<font color='red'>注：输入限制256个字符</font>"+
		  "<div style='margin-bottom:28px;margin-top:17px;'><table width='380' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>" + 
		  "<div style='text-align:center;'><input  style='margin-top:-18px;width:30%;' class='scbtn' type='button' value='保存' onclick='onSave();' /></div> </div>"  });
	wBox.showBox();
}

function onSave()
{
	var vr= document.getElementById('remark').value;
	
	if(vr.length >256)
	{
		alert('备注信息长度不能超过256个字符!');
		return;
	}
	
	var vres = vr.replace(/\n/g,"\\n");
	document.location.href="callmanage.do?method=addRemark&cid="+vid+"&remark="+vres;
}

function exportCallInfo(){

	sendRequestExport();
}

function sendRequestExport(){
 	createXMLHttpRequest();
 	var startdate = document.getElementById("startdate").value;
 	 	 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseExport;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

	var varurl="type=30";
	if(startdate != '')
		varurl +="&startdate="+startdate;
	
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

function callNum(umsisdn,clientnum)
{
	if(umsisdn =='')
	{
		alert('业务员电话为空!');
		return;
	}
	if(clientnum =='')
	{
		alert('客户电话为空!');
		return;
	}
	
	createXMLHttpRequest();
 	 	 	 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseCall;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

	var varurl="type=33"+"&umsisdn="+umsisdn+"&clientnum="+clientnum;

	XMLHttpReq.send(varurl);
}

function processResponseCall(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageCall();
			//alert(XMLHttpReq.responseText);
			//Len.innerText='权限载入完毕';
		}
		else{
			alert(XMLHttpReq.status);
			//Len.innerText='权限载入错误';
		}
	}
 }
 
  function parseMessageCall()
{
	var res=XMLHttpReq.responseText;
	alert(res);
	
}

</script>
<style type="text/css">
.longdata{
   width: 120px;
   height: 35px;
   white-space:nowrap;
   text-overflow:ellipsis;
   overflow: hidden;
   text-align:center;
}
</style>
</head>
<body onLoad="init('${msg}','${filenamebatchcan}','${sucesscount }','${faildcount }');" topmargin="0">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">呼叫管理</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<input type="hidden" id="pagetemp" value="${pagetemp }" />	
<form method="post" action="callmanage.do" name="mainfm" >  
    <table width="100%" align="center">
    <tr style="height:40px;">
		<td class="t6" style="text-indent:10px;" >日期：</td>
		<td class="t7" width="250"  >
			&nbsp;<label style="margin-top:15px;"></label>&nbsp;<input class="scinput" type="text" id="startdate"  name="startdate" value="${startdate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:150px"/> 
        </td>
    	<td class="t7" colspan="6">
			<div>
				<input class="scbtn" type="submit" value="查询"/>&nbsp;&nbsp;&nbsp;
		       	&nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		       	<input class="scbtn" type="button" value="导入" onClick="showuploadFileImport();" />&nbsp;&nbsp;&nbsp;
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

<form  style="display:none"  method="post" action="callmanage.do?method=importBatch"   name="uploadfile_import_form" id="uploadfile_import_form" onsubmit="return checkimportbatch();" enctype="MULTIPART/FORM-DATA" >	
	<table width="100%" align="center" style="border-collapse:collapse;font-size: 12px;" border="1" bordercolor="#3891a8">
		<tr>
			<td class="t8" style="font-size:15px;text-align:center;">导入</td>
		</tr>
		<tr>
			<td class="t8" style="height:54px;">
				<input style="width:520px; height:24px;" type="file" id="uploadfileImport" name="uploadfileImport" onchange="checkFile(this.value)" />&nbsp;&nbsp;&nbsp;
				<input class="scbtn" style="width:80px" type="submit" value="上传"  />&nbsp;&nbsp;&nbsp;
				<input class="scbtn" style="width:100px;" type="button" value="模板下载" onclick="importbatch();" />
			</td>
		</tr>
	</table >
</form>

<table class="tablelist" style="display: fixed;">
	<thead>        
  	<tr> 
        <th><b>序号</b></th>
        <th><b>业务员电话</b></th>
        <th><b>业务员姓名</b></th>
        <th><b>客户电话</b></th>
        <th><b>客户姓名</b></th>
        <th><b>状态</b></th>
        <th><b>备注</b></th>
        <th><b>呼叫日期</b></th>
        <th><b>操作</b></th>
 	</tr>
 	</thead>
 	<c:forEach items="${llist}" var="m">
 		<tbody>
        <tr> 
          <td>${m.id}</td>
          <td>${m.usermsisdn}</td>
          <td>${m.username}</td>
          <td>${m.clientnum}</td>
          <td>${m.clientname}</td>
          <td><c:if test="${m.callstatus==true}">已拨</c:if><c:if test="${m.callstatus==false}">未拨</c:if></td>
          <td style="width: 120px;"><div class="longdata">${m.remark}</div></td>
          <td><fmt:formatDate value="${m.calltime}" pattern="yyyy-MM-dd" /></td>
          <td>
          	<a href="#" style="color:#056dae;" onclick="editbut('${m.id}','${m.usermsisdn}','${m.username}','${m.clientnum}','${m.clientname}');" >编辑</a>&nbsp;&nbsp;&nbsp;
          	<a href="#" style="color:#056dae;" onClick="delbut('${m.id}');">删除</a>&nbsp;&nbsp;&nbsp;
          	<a href="#" style="color:#056dae;" onClick="showRemarkBox('${m.id}','${m.remark}');">添加备注</a>&nbsp;&nbsp;&nbsp;
          	<a href="#" style="color:#056dae;" onclick="callNum('${m.usermsisdn}','${m.clientnum}');" >呼叫</a>
          </td>
        </tr>
        </tbody>
 	</c:forEach>
</table>
<form method="post" action="callmanage.do" name="pageForm">
<input type="hidden"  name="startdate" value="${startdate}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="callmanage.do?page="/>	  			
</form>
<br/>
<br/>
<c:if test="${recordCount >0}">
<div style="width:100%;" align="right" >	<input class="scbtn1" style="width:120px ; height:30px;" type="button" value="呼叫信息导出" onclick="exportCallInfo();" /></div> 
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


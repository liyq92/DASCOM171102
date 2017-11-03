<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ page import="com.dascom.ssmn.util.Constants" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script language="JavaScript" type="text/JavaScript">
var wBox;
var opt;
var blacknumvar;
var varexportbatchfile;//批量处理导出文件名称

function cancel(){
	wBox.close();
}

function init(msg,filenamebatchcan,sucesscount,faildcount,errtext){

	if(filenamebatchcan != '')//显示批量处理结果
	{
		varexportbatchfile = filenamebatchcan;
		wBox=jQuery("#wbox10").wBox({
			title: "",
			html: "<div style='width:300px;height:100px;'><div style='text-align=center;margin-left:30px;margin-top:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>批量添加结果:成功"+sucesscount+"个;失败"+faildcount+"个</div>"+
			"<div style='margin-bottom:5px;margin-top:10px;'><table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div><input  style='margin-left:80px;margin-top:5px;width:40%;' type='button' value='结果导出' onclick='exportbatchFile();' /> </div>"
			});
		
		wBox.showBox();
	}else
	{
		if(msg != ''){
			wBox=jQuery("#wbox10").wBox({
			title: "返回结果",
			html: 	"<div style='width:280px;height:70px;'>"+
					"<table align='center' style='margin-top:40px;width:100%;'>"+
					"<tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr>"+
					"</table></div>"
			});
			wBox.showBox();
		}else
		{
			if(errtext !='')
			{
				var elist = errtext.split("#");
				if(elist.length >0)
				{
					//这种情况msg一定是空的，errList长度大于２,是因为第一个是头
		 		    var vstr = "<div style='width:310px;height:250px;'>" ;
					vstr +="<table align='center' style='margin-top:10px;margin-left:15px;width:95%;'>";
						
					for(var i=0;i<elist.length;i++)
					{
		 				vstr +="<tr><td align='left' style='font-size:15px;'>"+elist[i]+"</td></tr>";
		 		    }	
						
					vstr +="</table></div>";
		
					wBox=jQuery("#wbox10").wBox({
								title: "返回结果",
								html: 	vstr
							});
					wBox.showBox();
				}
			}
		}
	}	
}

function exportbatchFile()
{
	wBox.close();
	if(varexportbatchfile !='')
		document.location="${ctx}/batchDealBlackCallNumbers/"+varexportbatchfile;
}

function check()
{
	opt = document.getElementById('msisdn').value;
	if(opt == '')
	{
		alert('加入黑名单的号码!')
		return false;
	}
	/*var reg = new RegExp("^[0-9]*$");
	 if(!reg.test(opt))
	 {
	 	alert("加入黑名单的号码必须是数字!");
	 	return ;
	 }*/

	if(opt != '')
	{
		wBox=jQuery("#wbox10").wBox({
			title: "",
			html: 	"<div style='width:300px;height:100px;'>"+
					"<table align='center' style='margin-top:20px;width:100%;'>"+
						"<tr><td align='center'>"+
						"<div style='font-family:Arial;font-size:14px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>确认添加黑名单?</div></td></tr>"+
					"</table>"+
					"<div style='margin-bottom:5px;margin-top:10px;'>"+
						"<table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'>"+
							"<tr><td height='1'></td></tr>"+
						"</table></div><div> "+
					"<input class='cancel' style='margin-top:10px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />&nbsp;&nbsp;&nbsp;&nbsp;"+
					"<input class='sure' style='margin-top:8px;margin-left:10px;width:30%;' type='button' value='确定' onclick='resetpwd();' /> </div></div>"
			});
			wBox.showBox();
	}
}

function resetpwd()
{

	if(opt != '')
		document.location.href="addblackcallnumber.do?method=add&msisdn="+opt;
}

function showuploadFileDeal()
{
	document.getElementById("uploadfile_deal_form").style.display="inline";
}

function dealbatch()
{
var sre = "${ctx}/exportExcelTemp/blacknumdealbatchtemp.xls";
		document.location=sre;
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

function checkdealbatch()
{
	var vfile = document.getElementById("uploadfileDealUser").value;
	if(vfile == '')
	{
		alert('请上传文件!');
		return false;
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

function exportBlackNum()
{
	sendRequestExport();
}

function sendRequestExport(){
 	createXMLHttpRequest();
 	var blacknum = document.getElementById("blackNum").value;
	var startdate = document.getElementById("startdate").value;
	var enddate = document.getElementById("enddate").value;
 	 	
 	XMLHttpReq.open("POST","${ctx}/selectcdrsmsinfo",true);
	XMLHttpReq.onreadystatechange = processResponseExport;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

	var varurl="type=28";
	if(blacknum !='')
		varurl +="&blackNum="+blacknum;
	if(startdate != '')
		varurl +="&startdate="+startdate;
	if(enddate != '')
		varurl +="&enddate="+enddate;
	
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

function delBlackNum(num)
{
	blacknumvar =num;
	wBox=jQuery("#wbox2").wBox({
	title: "",
	html: "<div style='width:350px;height:120px;'>"+
		"<table style='margin-top:5px;margin-left:74px;width:90%;'>"+
		"<tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+
		"<td style='font-size:14px;'>确定从黑名单中删除该号码吗？<br><br></td> </tr><br>  </table> "+
		"<div style='margin-top:10px;'>"+
		"<table width='350' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel' style='margin-top:10px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
		"<input class='sure' style='margin-top:10px;margin-left:20px;width:30%;' type='button' value='确定' onclick='delconf();' /> </div>"
		});
	wBox.showBox();	
	
}

function delconf()
{
	if(blacknumvar != '')
		document.location.href="addblackcallnumber.do?method=del&delnum="+blacknumvar;
}

//-->
</script>
</head>
<body topmargin="0" onLoad="init('${msg}','${filenamebatchcan}','${sucesscount }','${faildcount }','${errtext}');">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">黑名单管理</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<form method="post" action="addblackcallnumber.do?method=add" onsubmit="return check();" name="mainfm">
<table width="100%" align="center">
    <tr style="height:40px;">
    	<td class="t6" style="text-indent:10px;width:90px;">黑名单号码：</td>
		<td  class="t7" style="width:190px;height:40px;border-right:none;">
			<input id="msisdn" style="width:980px;" class=scinput  name="msisdn"  value="${msisdn}" type="text" size="20" />
		</td>
		<td class="t7" style="border-left:none;">&nbsp;</td>
    </tr> 
     <tr>
        <td height="6" colspan="3" bgcolor="ECECF5"></td>
    </tr> 
    <tr>
    	<td colspan="3"><font size='2' color='red'>提示：输入多个副号码，请用分号分隔</font></td>
    </tr>
    <tr>
    	<td align=center colspan='3'> 
    		<input class="scbtn" type="button" value="添加"  onclick="check();" />&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    		<input class="scbtn" type="button" value="批量添加" onClick="showuploadFileDeal()"/>&nbsp; &nbsp; &nbsp; &nbsp;
    	</td>
    </tr>
    

</table>
</form>
<br/>
 <form  style="display:none"  method="post" action="addblackcallnumber.do?method=bindBatchDeal"   name="uploadfile_deal_form" id="uploadfile_deal_form" onsubmit="return checkdealbatch();" enctype="MULTIPART/FORM-DATA" >	
  <table width="100%" align="center" style="border-collapse:collapse;font-size: 12px;" border="1" bordercolor="#3891a8">
  <tr>
  	<td class="t8" >批量添加<br /></td>
  </tr>
  <tr>
  <td class="t8" style="height:54px;">
	  <input style="width:520px; height:24px;" type="file" id="uploadfileDealUser" name="uploadfileDealUser" onchange="checkFile(this.value)" />&nbsp;&nbsp;&nbsp;
	  <input class='scbtn' style="width:80px" type="submit" value="上传"  />&nbsp;&nbsp;&nbsp;
	  <input class='scbtn' style="width:100px;" type="button" value="模板下载" onclick="dealbatch();" /><br />
  </td>
  </tr>  
  </table >
</form>

<!-- 查询的form -->
<form method="post" action="addblackcallnumber.do"  name="mainfm1">
<table width="100%" align="center">
    <tr style="height:40px;">
    	<td class="t6" style="text-indent:10px;">黑名单号码：</td>
    	<td class="t7" width="160px;" >
    		<input class="scinput" type="text" id="blackNum" style='width:148px ; ' name="blackNum" value="${blackNum}" maxlength="20" />
  		</td> 
    	<td class="t6" style="text-indent:10px;width:90px;">添加时间：</td>
		<td class="t7" width="350"  >
			从&nbsp;<input class="scinput" type="text" id="startdate"  name="startdate" value="${startdate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:120px"/>
			&nbsp;&nbsp;到&nbsp;<input class="scinput" type="text" id="enddate" name="enddate" value="${enddate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:120px"/> 
		</td>
		<td class="t7" width="100px" style="text-align:center">
  			<input class="scbtn" type="submit" value="查询"/> 
    	</td>
    </tr> 
     <tr>
        <td height="6" colspan="8" bgcolor="ECECF5"></td>
    </tr> 
    <tr>
    	<td colspan="3">&nbsp;</td>
    </tr>  
</table>
</form>

<table class="tablelist">
	<thead>        
	  	<tr> 
	        <th><b>序号</b></th>
	        <th><b>号码</b></th>
	        <th><b>添加时间</b></th>
	        <th><b>操作</b></th>
	 	</tr>
 	</thead>
 	<c:forEach items="${list}" var="l">
 		<tbody>
        <tr> 
          <td>${l.orderNum }</td>
          <td>${l.blackNumber}</td>
          <td><fmt:formatDate type="both" value="${l.intime}"  /></td>
          <td><a href="javascript:" style="color:#056dae;" onclick="delBlackNum('${l.blackNumber }')">删除</a></td>
        </tr>
        </tbody>
 	</c:forEach>
</table>

<form method="post" action="addblackcallnumber.do" name="pageForm">
<input type="hidden" name="blackNum" value="${blackNum}" />
<input type="hidden" name="startdate" value="${startdate}" />
<input type="hidden" name="enddate" value="${enddate}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="addblackcallnumber.do?page="/>	  			
</form>
<br/>
<br/>
<div style="width:100%;" align="right" ><input class="scbtn1" style="width:120px ; height:30px;" type="button" value="黑名单信息导出" onclick="exportBlackNum();" /></div> 
</div>
<br/>


</body>
</html>

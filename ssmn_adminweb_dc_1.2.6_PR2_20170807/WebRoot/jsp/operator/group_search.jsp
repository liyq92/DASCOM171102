<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script language="JavaScript" type="text/JavaScript">
var wBox;
function checkDelete(delNo,name){
	if(delNo == ${loginOperator.groupId}){
		wBox=$("#wbox1").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>不允许删除当前在线操作员所属的分组！</td></tr></table></div>"
		});
		wBox.showBox();
		return false;	
	}	
	document.getElementById("tempvalue").value=name;
	wBox=$("#wbox2").wBox({
	title: "确定删除该权限组吗？",
	html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'><button onclick='delconf("+delNo+");' class='sure'>确&nbsp;&nbsp;定</button>&nbsp;&nbsp;<button onclick='cancel();' class='cancel'>取&nbsp;&nbsp;消</button></td></tr><tr><td>&nbsp;</td></tr></table></div>"
	});
	wBox.showBox();
}
function delconf(delNo){
	var name=document.getElementById("tempvalue").value
	document.location.href="OPgroupDelete.do?groupid="+delNo+"&groupname="+name;
	wBox.close();
}
function cancel(){
	wBox.close();
}
function init(msg) {
	if(msg != ''){
		wBox=$("#wbox3").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
}

function exportcdr(){
	sendRequestExport();
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
 
function sendRequestExport(){
 	createXMLHttpRequest();
 	var groupname = document.getElementById("groupName").value;
 	 	 	
 	XMLHttpReq.open("POST","${ctx}/exportoperatorinfo",true);
	XMLHttpReq.onreadystatechange = processResponseExport;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

	var varurl="type=3";
	if(groupname !='')
		varurl +="&groupname="+groupname;
	
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
		var sre = "${ctx}/exportOperatorFiles/"+ssmnall;
		document.location=sre;
	}
	
}
</script>
</head>
<body topmargin="0" onLoad="init('${msg}');">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">权限管理</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<form method="post" action="OPgroupSearch.do" name="mainfm">
  	<table width="100%" align="center">
        <tr style="height:40px;"> 
          	<td class="t6" style="text-indent:10px;">权限组名：</td>
          	<td class="t7" style="width:190px;height:40px;" >
          		<input class="scinput" type="text" maxlength="32" id="groupName" name="groupName" value="${groupName}" />
          	</td>
          	<td class="t7">
          		&nbsp;&nbsp;
       			<input  class="scbtn" type="button" value="增加权限组" onClick="javascript:document.location='OPgroupAdd.do'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       			<input class="scbtn" type="submit" value="查询"/>&nbsp;&nbsp;&nbsp;
      		</td>
        </tr>
        <tr>
        	<td height="6" colspan="3" bgcolor="ECECF5">
        	<input type="hidden" id="tempvalue" value="" />
        	</td>
      	</tr>  
      	 <tr><td>&nbsp;</td></tr>    
     </table> 
</form>
          
<table class="tablelist">
	<thead>
    <tr>
        <th><b>组名</b></th>
        <th><b>组描述</b></th>
        <th><b>创建时间</b></th>
        <th><b>创建人</b></th>
        <th><b>操作</b></th>      
    </tr> 
    </thead>     
    <c:forEach items="${groupList}" var="group">
    	<tbody>
		<tr>
	        <td>${group.groupName}</td>
	        <td>${group.description}</td>
	        <td>${group.createDateByString}</td>
	        <td>${group.operatorId}</td>
	        <td>&nbsp;&nbsp;	  
	        	<a style="color:#056dae;" href="OPgroupModify.do?groupid=${group.id}" ><img src="images/icon/xg1.gif" name="Image3" width="14" height="15" border="0" id="Image3" title="编辑"></a>
	        	<c:if test="${group.groupName != '超级管理员'}">
	        	     		&nbsp;&nbsp;&nbsp;
	        		<a href="javascript:" style="color:#056dae;"  onClick="checkDelete('${group.id}','${group.groupName }');"><img src="images/icon/del1.gif" name="Image4" border="0" id="Image4" title="删除"></a> 
	        	</c:if>
      	</tr>
      	</tbody>
	</c:forEach>      
</table>
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>

</body>
</html>

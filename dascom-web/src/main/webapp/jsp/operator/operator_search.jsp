<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<%  
  response.setHeader("Cache-Control",   "Public");  
  response.setHeader("Pragma",   "no-cache");  
  response.setDateHeader("Expires",   0);   
%>

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

function checkDelete(delNo){
	if(delNo == '${loginOperator.id.openo}'){
		wBox=jQuery("#wbox1").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>不允许删除当前在线的操作员！</td></tr></table></div>"
		});
		wBox.showBox();
		return false;	
	}
	document.getElementById("tempopeno").value=delNo;
	wBox=jQuery("#wbox2").wBox({
	title: "",
	html: "<div style='width:350px;height:120px;'>"+
		"<table style='margin-top:5px;margin-left:100px;width:90%;'>"+
		"<tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>"+
		"<td style='font-size:14px;'>确定要删除该操作员吗？<br><br></td> </tr><br>  </table> "+
		"<div style='margin-top:10px;'>"+
		"<table width='350' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel' style='margin-top:10px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' />"+
		"<input class='sure' style='margin-top:10px;margin-left:20px;width:30%;' type='button' value='确定' onclick='delconf();' /> </div>"
		});
	wBox.showBox();	
}
function delconf(){
	delNo=document.getElementById("tempopeno").value;
	document.location.href="operatorDelete.do?opeNo="+delNo;
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
	
		//document.mainfm.action="operatorSearch.do?method=exportcdr";
  		//document.mainfm.submit();
  		//document.mainfm.action="operatorSearch.do";
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
 	var openo = document.getElementById("openo").value;
 	var selectvbdbox1 = document.getElementById("bdbox1");
 	var selectvzonebox1 = document.getElementById("zonebox1");
 	var selectvareabox1 = document.getElementById("areabox1");
 	var selectvbagbox1 = document.getElementById("bagbox1");
 	 	
 	XMLHttpReq.open("POST","${ctx}/exportoperatorinfo",true);
	XMLHttpReq.onreadystatechange = processResponseExport;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

	var varurl="type=1&openo="+openo;
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
		var sre = "${ctx}/exportOperatorFiles/"+ssmnall;
		document.location=sre;
	}
	
}

function editbut(openoo)
{
	if(openoo == '')
		return false;
	var vaopeno1 = document.getElementById('openo').value;
	var pagetemp = document.getElementById("pagetemp").value;
	
	var selectvbdbox1 = document.getElementById("bdbox1");
 	var selectvzonebox1 = document.getElementById("zonebox1");
 	var selectvareabox1 = document.getElementById("areabox1");
 	var selectvbagbox1 = document.getElementById("bagbox1");
	
	var vurl = "operatorModify.do?opeNo="+openoo;
	if(vaopeno1 != '')
		vurl +="&seopeno1="+vaopeno1;
	if(selectvbdbox1.selectedIndex != 0)
		vurl +="&selectbdbox1="+selectvbdbox1.value;
	if(selectvzonebox1.selectedIndex != 0)
		vurl +="&selectzonebox1="+selectvzonebox1.value;
	if(selectvareabox1.selectedIndex != 0)
		vurl +="&selectareabox1="+selectvareabox1.value;
	if(selectvbagbox1.selectedIndex != 0)
		vurl +="&selectbagbox1="+selectvbagbox1.value; 
	if(pagetemp != '')
		vurl +="&pagetemp="+pagetemp;
	document.location.href=vurl; 
	 
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

//-->
</script>
</head>
<body onLoad="init('${msg}');" topmargin="0">
<%java.util.List authlist = (java.util.List)request.getAttribute("authMethodList");%>
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">操作员管理</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<input type="hidden" id="pagetemp" value="${pagetemp }" />	
<form method="post" action="operatorSearch.do" name="mainfm" >  
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
    	<td class="t6" style="text-indent:10px;">${warName }：</td>
    	<td class="t7">
	    	<select class="select3" id='zonebox1' name='zonebox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(2)' <c:if test="${ slevelid==6 || slevelid==5 || slevelid==4 }"> disabled="disabled" </c:if> >
				<option value=''>全部</option>
				<c:forEach items="${wzlist}" var="wz" varStatus="s">
					<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
				</c:forEach>
			</select>
		</td>
    	<td class="t6" style="text-indent:10px;">${areaName }：</td>
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
    	<td class="t6" style="text-indent:10px;">账号：</td>
    	<td class="t7" >
    		<input class="scinput" type="text" id="openo" style='width:148px ; ' name="openo" value="${openo1 }" maxlength="20" />				  	
  		</td> 
  		<td class="t7" colspan="6">
  			<input class="scbtn"  type="submit" value="查询"/>&nbsp;&nbsp;&nbsp;
		 	<%if(authlist != null && authlist.contains("operatorAdd")){%>
		       		&nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		       		<input class="scbtn" type="button" value="新建操作员" onClick="javascript:document.location='operatorAdd.do'"/>&nbsp;&nbsp;&nbsp;
		  	<%} %>
  		</td>
    </tr>
    <tr>
        <td height="6" colspan="8" bgcolor="ECECF5">
		<input type="hidden" id="tempopeno" value="" />
	</td>
    </tr> 
     <tr><td>&nbsp;</td></tr>     
 </table>  
</form>

<table class="tablelist">
	<thead>        
  	<tr> 
        <th><b>操作员帐号</b></th>
        <th><b>真实姓名</b></th>
        <th><b>${businName }</b></th>
        <th><b>${warName }</b></th>
        <th><b>${areaName }</b></th>
        <th><b>${branName }</b></th>
        <th><b>创建时间</b></th>
         <%if(authlist != null && (authlist.contains("operatorModify") || authlist.contains("operatorDelete") )){%>
        <th><b>操作</b></th>
        <%} %>	 
 	</tr>
 	</thead>
 	<c:forEach items="${opelist}" var="operator">
 		<tbody>
        <tr> 
          <td>${operator.id.openo}</td>
          <td>${operator.agentInfo}</td>
          <td>${operator.businessdepartment}</td>
          <td>${operator.warzone}</td>
          <td>${operator.area}</td>
          <td>${operator.branchactiongroup}</td>
          <td>${operator.create_timebystring}</td>
          <%if(authlist != null && (authlist.contains("operatorModify") || authlist.contains("operatorDelete") )){%>
	          <td>
				<%if(authlist != null && authlist.contains("operatorModify") ){%>
	          		<a href="javascript:" style="color:#056dae;" onclick="editbut('${operator.id.openo}');" >编辑</a>&nbsp;&nbsp;&nbsp;
	         	<%} %>	   
				<%if(authlist != null && authlist.contains("operatorDelete") ){%>
		          	<a href="javascript:"  style="color:#056dae;" onClick="checkDelete('${operator.id.openo}');">删除</a>
		        <%} %>	  
	          </td>
           <%} %>	  
        </tr>
        </tbody>
 	</c:forEach>
</table>

<form method="post" action="operatorSearch.do" name="pageForm">
<input type = "hidden" name= "openo" value="${openo1 }"/>
<input type = "hidden" name= "agentinfo" value="${agentinfo1 }"/>
<input type="hidden" name="bdbox1" value="${bdparam}" />
<input type="hidden" name="zonebox1" value="${zoneparam}" />
<input type="hidden" name="areabox1" value="${areaparam}" />
<input type="hidden" name="bagbox1" value="${bagparam}" />
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="operatorSearch.do?page="/>	  			
</form>
<br/>
<br/>
<c:if test="${isExport == 1}">
 <div style="width:100%;" align="right"><input class="scbtn1" style="width:120px ; height:30px;" type="button" value="操作员信息导出" onclick="exportcdr();" /></div> 
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


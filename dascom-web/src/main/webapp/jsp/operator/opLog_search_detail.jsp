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

<script language="JavaScript" type="text/JavaScript">
//<!--

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

function returntoOpera(selectopeno1,selectstartdate,selectenddate,selectislogin,selectbdparam)
{
	
	var vurl = "OperatorLoginSearch.do?type=0";
	if(selectopeno1 != '')
		vurl +="&openo1="+selectopeno1;
	if(selectstartdate !='')
		vurl +="&startdate="+selectstartdate;
	if(selectenddate != '')
		vurl +="&enddate="+selectenddate;
	if(selectislogin != '')
		vurl +="&islogin="+selectislogin;
	if(selectbdparam !='')
		vurl +="&bdbox1="+selectbdparam;
	
	document.location.href=vurl;
	  
}

//-->
</script>
</head>
<body onLoad="init('${msg}');" topmargin="0">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">登录详情</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;"> 
<table class="tablelist">
	<thead>        
	  	<tr> 
	        <th><b>操作员账号</b></th>
	        <th><b>${businName }</b></th>
	        <th><b>${warName }</b></th>
	        <th><b>${areaName }</b></th>
	        <th><b>${branName }</b></th>
	        <th><b>登录时间</b></th>
	        <c:if test="${dischannel ==1}">
	        	<th><b>录音A+次数</b></th>
	        	<th><b>录音渠道次数</b></th>
	        </c:if>
	        <c:if test="${dischannel ==0}">
	        	<th><b>录音次数</b></th>
	        </c:if>
	        <th><b>备注次数</b></th>
	 	</tr>
 	</thead>
 	<c:forEach items="${loginfo}" var="operator">
 		<tbody>
        <tr> 
          <td>${operator.openo}</td>
          <td>${operator.businessdepartment}</td>
          <td>${operator.warzone}</td>
          <td>${operator.area}</td>
          <td>${operator.branchactiongroup}</td>
          <td>${operator.lastLoginTime}</td>
          <c:if test="${dischannel ==1}">
          	<td>${operator.cdrCountA}</td>
          	<td>${operator.cdrCountChannel}</td>
          </c:if>
          <c:if test="${dischannel ==0}">
          	<td>${operator.cdrCountTotal}</td>
          </c:if>
          <td>${operator.cdrRemarkCount}</td>
        </tr>
        </tbody>
 	</c:forEach>
</table>

<form method="post" action="OperatorLoginSearch.do" name="pageForm">
<input type = "hidden" name= "selectopeno1" value="${selectopeno1}"/>
<input type = "hidden" name= "selectstartdate" value="${selectstartdate }"/>
<input type = "hidden" name= "selectenddate" value="${selectenddate }"/>
<input type = "hidden" name= "selectislogin" value="${selectislogin }"/>
<input type = "hidden" name= "selectbdparam" value="${selectbdparam }"/>
<input type = "hidden" name= "openo2" value="${openo2}"/>
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="OperatorLoginSearch.do?type=1&page="/>
<br/>
<br/>
 <div align="center"><input class="scbtn" type="button" value="返回"  onClick="returntoOpera('${selectopeno1}','${selectstartdate }','${selectenddate }','${selectislogin }','${selectbdparam }');"  /></div>
<br/>

</form>
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>

</body>
</html>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ page import="com.dascom.ssmn.util.Constants" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script language="JavaScript" type="text/JavaScript">
<!--
var wBox;

function clean(){
	document.getElementById("name1").value='';
	document.getElementById("cardgrade1").value='1';
}
function checkDelete(){	
	if(window.confirm('您确定要删除吗？')){
		return true;
	} return false;
}

function check(){
	var startdate=document.getElementById("startdate").value;
	var enddate=document.getElementById("enddate").value;
	if(startdate!=""){
		if(enddate!=""){
			if(isStartEndDate(startdate,enddate)){
				return true;
			}else{
				alert("查询结束时间应晚于开始时间！");
				return false;
			}
		}else{
			alert("请输入查询结束时间！");
		return false;
		}
	}else{
		alert("请输入查询开始时间！");
		return false;
	}
}
function isStartEndDate(startDate,endDate){   
     if(startDate.length>0&&endDate.length>0){  
      var startDateTemp = startDate.split(" ");  
      var endDateTemp = endDate.split(" ");  
      //分开日期   
      var arrStartDate = startDateTemp[0].split("-");   
      var arrEndDate = endDateTemp[0].split("-");   
      //分开时间
      // var arrStartTime = startDateTemp[1].split(":");   
      // var arrEndTime = endDateTemp[1].split(":");   
      //                          年                月                    日            时                    分              秒
      var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2],"00","00","00");   
      var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2],"00","00","00");  
      if(allStartDate.getTime()>allEndDate.getTime()){
       return false;   
      }   
     }   
     return true;   
    } 


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

function onclickdownmonth(sisMonthdown)
{
	if(sisMonthdown !='1')
	{
		alert('暂无上月报表!');
	}
}

//-->
</script>
</head>
<style type="text/css">
#yesBtn{
	margin-left: 20px;
    margin-bottom: 5px;
}
</style>
<body topmargin="0" onLoad="init('${msg}');">
<%java.util.List authlist = (java.util.List)request.getAttribute("authMethodList");%>
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">统计报表下载</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<form method="post" action="queryinfo.do?method=datesearch" onSubmit="return check();" name="mainfm">
 <table width="100%" align="center" >
        <tr style="HEIGHT:70px;padding-bottom:10px;"> 
          	<td class="t7" width="500" valign="top" style="font-size:13px;" >
	          	<br/>
	          	<b><font size="5" >昨日（${filelist[0].date }）数据下载  </font></b>
	            <br/>
	            <br/>
	             <c:if test="${filelist[0].execlname != null}">
	          	       <b>${filelist[0].execlname}</b>
	          	       <%if(authlist != null && (authlist.contains("reportDownloadStatis"))){ %>
		          	       <c:if test="${fileType1 !=2}">
		          	       		<input class="scbtn" id="yesBtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.EXECL_FOLDER_NAME%>/<%=Constants.DOWNLOAD_EXECL_NAME1%>${sprovinceFileName1}_${filelist[0].date}${showtypetemp}.${filelist[0].excelExtent}'" />
		          	 		</c:if>
		          	 		<c:if test="${fileType1 ==2}">
		          	 			<input class="scbtn" id="yesBtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.EXECL_FOLDER_NAME%>/<%=Constants.DOWNLOAD_EXECL_NAME2%>${sprovinceFileName2}_${filelist[0].date}${showtypetemp}.${filelist[0].excelExtent}'" />
		          	 		</c:if>
	          	 		<%} %> 
	          	 </c:if>
	          	  <c:if test="${filelist[0].execlname == null}">
	          	  		 <c:if test="${fileType1 !=2}">
	          	        	<b><%=Constants.DOWNLOAD_EXECL_NAME2%>${sprovinceFileName2}_${filelist[0].date}</b>
	          	        </c:if>
	          	         <c:if test="${fileType1 ==2}">
	          	        	<b><%=Constants.DOWNLOAD_EXECL_NAME2%>${sprovinceFileName2}_${filelist[0].date}</b>
	          	        </c:if>
	          	        <%if(authlist != null && (authlist.contains("reportDownloadStatis"))){ %>
	          	        	<input class="scbtn" id="yesBtn" type="button" value="点击下载" />  
	          	    	<%} %> 
	          	  </c:if>
	      	 
	          	  <br/>
	        
	          	  <c:if test="${filelist[0].zipname != null}">
	          	       <b>${filelist[0].zipname}</b>
	          	       <%if(authlist != null && (authlist.contains("downloadCDRStatis"))){ %>
		          	       <c:if test="${fileType2 !=2}">
		          	       		<input class="scbtn" id="yesBtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.ZIP_FOLDER_NAME%>/<%=Constants.DOWNLOAD_FILENAME1%>${sprovinceFileName1}_${filelist[0].date}.zip'" /> 
		          	 		</c:if>
		          	 		 <c:if test="${fileType2 ==2}">
		          	       		<input class="scbtn" id="yesBtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.ZIP_FOLDER_NAME%>/<%=Constants.DOWNLOAD_FILENAME2%>${sprovinceFileName2}_${filelist[0].date}.zip'" /> 
		          	 		</c:if>
	          	 		<%} %>
	          	  </c:if>
	          	  <c:if test="${filelist[0].zipname == null}">
	          	  		 <c:if test="${fileType2 !=2}">
	          	        	<b><%=Constants.DOWNLOAD_FILENAME2%>${sprovinceFileName2}_${filelist[0].date}</b>
	          	        </c:if>
	          	        <c:if test="${fileType2 ==2}">
	          	        	<b><%=Constants.DOWNLOAD_FILENAME2%>${sprovinceFileName2}_${filelist[0].date}</b>
	          	        </c:if>
	          	        <%if(authlist != null && (authlist.contains("downloadCDRStatis"))){ %>
	          	        	<input class="scbtn" id="yesBtn" type="button" value="点击下载" /> 
	          	        <%} %>
	          	  </c:if> 
         	</td>
          	 
         <td class="t7" width="500" valign="top"  style="font-size:13px;">
         	<br/>
          	<b><font size="5" >上月（${lastmonth}）数据下载  </font></b>
            <br/><br/>
        
             <c:if test="${sisMonthdown == '1'}">
             	  <c:if test="${fileType3 !=2}">
          	       		&nbsp;<b><%=Constants.DOWNLOAD_EXECL_NAME1%>${sprovinceFileName1}_${lastmonth}</b>&nbsp;&nbsp;&nbsp; 
          	       		<%if(authlist != null && (authlist.contains("reportDownloadStatis"))){ %>
          	       			<input class="scbtn" id="yesBtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.EXECL_FOLDER_NAME%>/${lastmonth }/<%=Constants.DOWNLOAD_EXECL_NAME1%>${sprovinceFileName1}_${lastmonth}${showtypetemp}.${stateExtentMonth }'" />
          	 	   		<%} %> 
          	 	   </c:if>
          	 	   <c:if test="${fileType3 ==2}">
          	       		&nbsp;<b><%=Constants.DOWNLOAD_EXECL_NAME2%>${sprovinceFileName2}_${lastmonth}</b>&nbsp;&nbsp;&nbsp; 
          	       		<%if(authlist != null && (authlist.contains("reportDownloadStatis"))){ %>
          	       			<input class="scbtn" id="yesBtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.EXECL_FOLDER_NAME%>/${lastmonth }/<%=Constants.DOWNLOAD_EXECL_NAME2%>${sprovinceFileName2}_${lastmonth}${showtypetemp}.${stateExtentMonth }'" />
          	 	   		<%} %> 
          	 	   </c:if>
          	  </c:if>
          	  <c:if test="${sisMonthdown != '1'}">
          	  		<c:if test="${fileType3 !=2}">
          	        	&nbsp;<b><%=Constants.DOWNLOAD_EXECL_NAME2%>${sprovinceFileName2}_${lastmonth}</b>
          	        </c:if>
          	        <c:if test="${fileType3 ==2}">
          	        	&nbsp;<b><%=Constants.DOWNLOAD_EXECL_NAME2%>${sprovinceFileName2}_${lastmonth}</b>
          	        </c:if>
          	        <%if(authlist != null && (authlist.contains("reportDownloadStatis"))){ %>
          	        	<input class="scbtn" id="yesBtn" onclick="onclickdownmonth('${sisMonthdown}');"  type="button" value="点击下载" />  
          	    	<%} %> 
          	  </c:if>	      	 
      		  <br/>   	 
          </td>
        </tr>

        <tr>
        	<td height="2" colspan="2"  bgcolor="ECECF5"/>
      	</tr>   
      	 <tr><td>&nbsp;</td></tr>   
     </table>

</form>
<br/>

<table class="tablelist" align="center" border="1">
	<thead>    
		<tr> 
	        <th><b>时间</b></th>
	        <th><b>通话详情报表</b></th>
	        <th><b>通话录音</b></th>
	 	</tr>
 	</thead>
 	 <c:forEach items="${filelist}" var="f" varStatus="s">
 	 <tbody>
 	    <tr>
 			<td>${f.date}</td> 
 			<td> 
 			  <c:if test="${f.execlname != null}">
 			      ${f.execlname}   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
 			      <%if(authlist != null && (authlist.contains("reportDownloadStatis"))){ %>
	 			       <c:if test="${fileType1 !=2}">
	       		      		<input class="scbtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.EXECL_FOLDER_NAME%>/<%=Constants.DOWNLOAD_EXECL_NAME1%>${sprovinceFileName1}_${f.date}${showtypetemp}.${f.excelExtent}'" /> 
	 			  		</c:if>
	 			  	   <c:if test="${fileType1 ==2}">
	 			  	   		<input class="scbtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.EXECL_FOLDER_NAME%>/<%=Constants.DOWNLOAD_EXECL_NAME2%>${sprovinceFileName2}_${f.date}${showtypetemp}.${f.excelExtent}'" /> 
	 			  	   </c:if>
 			  	   <%} %>
 			  </c:if>
 			</td>
 			<td>
 			  <c:if test="${f.zipname != null}">
 			    ${f.zipname}   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
 			    <%if(authlist != null && (authlist.contains("downloadCDRStatis"))){ %>
	       		    <c:if test="${fileType2 !=2}">
	       		    	<input class="scbtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.ZIP_FOLDER_NAME%>/<%=Constants.DOWNLOAD_FILENAME1%>${sprovinceFileName1}_${f.date}.zip'" />  
	 			  	</c:if>
	 			  	<c:if test="${fileType2 ==2}">
	       		    	<input class="scbtn" type="button" value="点击下载" onclick="javascript:document.location='${ctx}/<%=Constants.ZIP_FOLDER_NAME%>/<%=Constants.DOWNLOAD_FILENAME2%>${sprovinceFileName2}_${f.date}.zip'" />  
	 			  	</c:if>
 			  	<%} %>
 			  </c:if>
 		    </td>
 		</tr>
 		</tbody>
 	 </c:forEach>
</table>
<br/>
<br/>
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>
</body>
</html>

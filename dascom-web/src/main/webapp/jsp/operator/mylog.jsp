<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>
<title>我的日志</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<script language="JavaScript" type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
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
var wBox;
function check(){
	var fm=document.forms[0];
    if(fm.startTime=='') {
     	alert("起始时间不能为空！");  
     	return false;
     }
     if(fm.stopTime=='') {
	     alert("截止时间不能为空！");  
	     return false;
      }
  if(fm.startTime.value != '') {
		if(!isDate(fm.startTime.value)){
			wBox=$("#wbox1").wBox({
			title: "返回结果",
			html: "<div style='width:300px;height:100px;font-size:14px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>起始日期不正确，请重新输入！</td></tr></table></div>"
			});
			wBox.showBox();
			fm.startTime.focus();
			return false;
		}
	}
	if(fm.stopTime.value != '') {
		if(!isDate(fm.stopTime.value)){
			wBox=$("#wbox2").wBox({
			title: "返回结果",
			html: "<div style='width:300px;height:100px;font-size:14px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>终止时间不正确，请重新输入！</td></tr></table></div>"
			});
			wBox.showBox();
			fm.stopTime.focus();
			return false;
		}
	}
	if(fm.startTime.value!="" && fm.stopTime.value!="") {
         if(!dateIsMax(fm.startTime,0,0,fm.stopTime,0,0,'起始时间必须小于终止时间')){return false;}
    }
    return true;
}
function export_file()
{ 
  if(check()){
  document.mainfm.action="myexportFile.do?flag=2";
  document.mainfm.submit();
  }
}
function my_log_search()
{ 
  if(check()){
  document.mainfm.action="mylogSearch.do";
  document.mainfm.submit();
  }
}
function cleElements()
{
   document.mainfm.logType.value="";
   document.mainfm.startTime.value="";
   document.mainfm.stopTime.value="";
   document.mainfm.msgName.value="";
   document.mainfm.patternName.value="";
}

function init(msg) {
	if(msg != ''){
		wBox=$("#wbox3").wBox({
		title: "返回结果",
		html: "<div style='width:300px;height:100px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
}
//-->
</script>
</head>
<body topmargin="0" onLoad="MM_preloadImages('images/button/button-see2.gif','images/button/button-search2.gif');init('${msg}');">
<div id="title" style="height:20px"><img src="images/500000002.gif" width="10" height="11" />&nbsp;当前位置：我的操作日志查询</div>
<hr/>
<br/>
<form method="post" action="mylogSearch.do" onSubmit="return check();" name="mainfm">
<IFRAME id="CalFrame"  style="DISPLAY: none; Z-INDEX: 100; WIDTH: 148px; POSITION: absolute;HEIGHT: 194px; left: 42px; top: 50px "
 marginWidth="0" marginHeight="0" src="scripts/calendar/calendar.htm" frameBorder="0"  noResize scrolling="no"></IFRAME>
<table align="center" width="90%">
  <tr>
                <td class="t6">操作类型</td>
<td class="t7"><select name="logType">
<option value="">全部</option>
    <c:forEach items="${operationType}" var="string">
        <option value="${string}" <c:if test="${logType==string}">selected</c:if>>${string}</option>                		
    </c:forEach>
     </select><font color="red">*</font></td>
   <!-- 全部、创建消息模板、查询和编辑消息模板、固定模板发布消息、定制模板发布广播消息、定制模板发布个人消息、消息查询、消息推送结果查询、用户查询、登记信息修改、我的操作日志查询、增加操作员、操作员修改、增加群组、群组修改、日志信息查询 -->
				  </tr>
				  <tr>
               <td class="t6">操作时间范围：</td>
                <td class="t7">                  
                 从
                    <input type="text" name="startTime" maxlength="10" size="12" value="${startTime}" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:true,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:150px"/>  
                    到
                    <input type="text" name="stopTime" maxlength="10" size="12" value="${stopTime}" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:true,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:150px"/>&nbsp;
                    <font color="red">*</font>                                 
                </td>
                </tr>  
                <tr>
        <td height="6" colspan="2" bgcolor="ECECF5"></td>
      </tr>  
      <tr>
        <td height="40" colspan="4" align="center">
        	<input class="btnstyle" onmouseout="this.style.backgroundImage='url(images/btnbg.png)';" onmouseover="this.style.backgroundImage='url(images/btnbg2.png)';" type="button" value="查询" onClick="my_log_search();"/>&nbsp;&nbsp;&nbsp;
 			<input class="btnstyle" onmouseout="this.style.backgroundImage='url(images/btnbg.png)';" onmouseover="this.style.backgroundImage='url(images/btnbg2.png)';" type="submit" value="清除" onClick="cleElements();"/>&nbsp;&nbsp;&nbsp;
    		<c:if test="${recordCount!=0}">
    		<input class="btnstyle" onmouseout="this.style.backgroundImage='url(images/btnbg.png)';" onmouseover="this.style.backgroundImage='url(images/btnbg2.png)';" type="submit" value="导出文件" onClick="export_file()"/>
    		</c:if>	
        </td>
      </tr>  
      </table>
            <br />	
            
      </form>
<c:if test="${logList!=null}">
	<table width="90%" align="center"  style="border-collapse:collapse;font-size: 12px;" border="1" bordercolor="#3891a8">
		 <tr>
		 	<td class="t8" colspan="7">日志列表</td>
		 </tr>
                   <tr>
                      <td class="t8">序号</td>
                      <td class="t8">操作员</td> 
                      <td class="t8">操作员组</td>          
                      <td class="t8">操作时间</td>
                      <td class="t8">操作类型</td>
                      <td class="t8">操作描述</td>
                    </tr>
                    <c:forEach items="${logList}" var="log" varStatus="s">
                    <tr class="t12" onMouseOut="mout(this);" onMouseOver="mover(this);">
                    	<td>${(pageNum-1)*pageSize+s.index+1}</td>
                      	<td>${log.openo}</td>
                      	<td>${log.groupName}</td>
                      	<td>${log.logTimeByString}</td>
                      	<td>${log.logType}</td>
                      	<td>${log.logDes}</td>
                 	</tr>
                  </c:forEach>
                </table>
                
<form method="post" action="mylogSearch.do" name="pageForm">
<input type="hidden" name="opeNo" value="${opeNo}"/>
<input type="hidden" name="msgName" value="${msgName}"/>
<input type="hidden" name="patternName" value="${patternName}"/>
<input type="hidden" name="logType" value="${logType}"/>
<input type="hidden" name="startTime" value="${startTime}"/>
<input type="hidden" name="stopTime" value="${stopTime}"/> 
<common:pageLocator recordCount="${recordCount}" pageSize="${pageSize}" currentPage="${pageNum}" formName="pageForm" url="mylogSearch.do?page="/>
</form>
</c:if>

<script language="JavaScript" type="text/JavaScript">
<c:if test="${logType != null and logType != ''}">
document.all.mainfm.logType.value="${logType}";
</c:if>
</script>

</body>
</html>					

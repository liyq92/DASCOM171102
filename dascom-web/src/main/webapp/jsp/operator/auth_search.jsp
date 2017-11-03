<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>
<title>权限管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<script language="JavaScript" type="text/JavaScript">
<!--

var XMLHttpReq = false;
var serviceKey;

function searchAuth(){
    var fm=document.forms[0];
	serviceKey=fm.servicekey.value;
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
	XMLHttpReq.open("POST","${ctx}/authSearch.do",true);
	XMLHttpReq.onreadystatechange = processResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("serviceKey=" + serviceKey);
}

function processResponse(){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
			clearPreviousResults();
			parseResults();
		}
		else{
			alert(XMLHttpReq.status);
		}
	}
 }
 

function clearPreviousResults(){
    var tableBody = document.getElementById("resultsBody");
    while(tableBody.childNodes.length > 0){
        tableBody.removeChild(tableBody.childNodes[0]);
    }
}
 
function parseResults(){
	var results = XMLHttpReq.responseXML;
    var auth = null;
    var authdesc = "";
	var authmethod = "";
	var servicekey = "";

    var auths = results.getElementsByTagName("Auth");

    for(var i = 0; i < auths.length; i++) {
        auth = auths[i];
        authdesc = auth.getElementsByTagName("authdesc")[0].firstChild.nodeValue;
		authmethod = auth.getElementsByTagName("authmethod")[0].firstChild.nodeValue;
		servicekey = auth.getElementsByTagName("servicekey")[0].firstChild.nodeValue;
        addTableRow(authdesc,authmethod,servicekey);
	}
}

function addTableRow(authdesc,authmethod,servicekey){
		var row = document.createElement("tr");
		var cell = createCellWithText(authdesc);
		row.appendChild(cell);
		cell = createCellWithText(authmethod);
		row.appendChild(cell);
		cell = createCellWithText(servicekey);
		row.appendChild(cell);
		document.getElementById("resultsBody").appendChild(row);
}

function createCellWithText(text) {
		var cell = document.createElement("td");
		var textNode = document.createTextNode(text);
		cell.appendChild(textNode);
		return cell;
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

//-->
</script>
</head>
<body onLoad="MM_preloadImages('images/button/button-search2.gif','images/button/button-add-operator2.gif','images/icon/xg2.gif','images/icon/del2.gif')" topmargin="0">
 <div id="title" style="height=20"><img src="images/500000002.gif" width="10" height="11" />&nbsp;当前位置：权限管理</div><br />	
 	<hr/>
 	<br/>
 	<form method="post" action="operatorSearch.do">  
<logic:messagesPresent message="true">
  <html:messages id="msg" message="true">
	  <font color="red"><bean:write name="msg"/></font>
  </html:messages>
</logic:messagesPresent>
<br/>
    <div align="center">
		权限归属：
		<select name="servicekey" onChange="searchAuth()">
			<option value="">---------</option> 
    			<c:forEach items="${keyList}" var="key">
        			<option value="${key.dicvalue}">${key.dicvalue}</option>                		
    			</c:forEach>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button"  value="增加权限" onClick="javascript:document.location='authAdd.do'"/>
    </div><br />

</form>

<table width="90%" align="center" cellspacing="1">
	<tr> 
          <td class="t8">权限名称</td>
          <td class="t8">鉴权方法</td>
          <td class="t8">业务键</td>
	</tr>
	<tbody id="resultsBody">
	</tbody>
</table>
</body>
</html>

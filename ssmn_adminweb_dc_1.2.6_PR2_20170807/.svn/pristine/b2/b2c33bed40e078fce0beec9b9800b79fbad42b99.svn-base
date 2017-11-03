<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>登记信息修改</title>

<script language="JavaScript" type="text/JavaScript">
<!--
<!--

var modifyflg = 0;

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

var wBox;
function check(){
	var fm=document.forms[0];
	var exp = new RegExp("^[A-Za-z0-9_]*$");	
	if(fm.opepwd.value!=''){
		 if (!exp.test(fm.opepwd.value) || fm.opepwd.value.length < 4 ||fm.opepwd.value.length > 20) 
       	 {
       		wBox=$("#wbox1").wBox({
			title: "返回结果",
			html: "<div style='width:300px;height:100px;font-size:14px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>密码必须是4~8位普通英文字符、数字或下划线！</td></tr></table></div>"
			});
			wBox.showBox();
			return false;
         }
		if(fm.pswdConfirm.value=='') {
			wBox=$("#wbox2").wBox({
			title: "返回结果",
			html: "<div style='width:300px;height:100px;font-size:14px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>若要修改密码，密码与确认密码必须都输入！</td></tr></table></div>"
			});
			wBox.showBox();
		    return false;
		}
		if(fm.opepwd.value != fm.pswdConfirm.value)
        {
  	        wBox=$("#wbox3").wBox({
			title: "返回结果",
			html: "<div style='width:300px;height:100px;font-size:14px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>密码不一致，请重新输入！</td></tr></table></div>"
			});
			wBox.showBox();
  	        return false;
        }
	}
	if(fm.pswdConfirm.value!=''&&fm.opepwd.value=='') {
		wBox=$("#wbox4").wBox({
			title: "返回结果",
			html: "<div style='width:300px;height:100px;font-size:14px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>若要修改密码，密码与确认密码必须都输入！</td></tr></table></div>"
			});
			wBox.showBox();
		       return false;	
	}                
    if (fm.modifyflg.value == "0") {
    	wBox=$("#wbox5").wBox({
			title: "返回结果",
			html: "<div style='width:300px;height:100px;font-size:14px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>您的信息未被修改！</td></tr></table></div>"
			});
			wBox.showBox();
	 		return false;
	}
	 
	    return true;
}

function changSelSta(itemValue){
   var i;
   var opObj = document.forms[0].selectId;
 //  var desObj = document.forms[0].selurlDes;
   for (i=0;i<opObj.length;i++) {
      if (opObj[i].value == itemValue) {
        opObj[i].status = true;
        break;
      }
   }
}

function changevalue(){
	document.all.modifyflg.value = "1";	
}


function init(msg) {
	if(msg != ''){
		wBox=$("#wbox6").wBox({
		title: "返回结果",
		html: "<div style='width:300px;height:100px;font-size:14px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
}

//-->
</script>
</head>
<body onLoad="init('${msg}');">
<div id="title" style="height:20px"><img src="images/500000002.gif" width="10" height="11" />&nbsp;当前位置：我的帐号密码修改</div>
<hr/>
<br/>
	
<form name="submitform" method="post" action="reginfoModify.do" onsubmit="return check(); ">
<input type="hidden" name="modifyflg" value = "0"/>
<table align="center" width="90%">
    <tr> 
            <td class="t6">操作员帐号</td>
          	<td class="t7"><c:out value="${opermodify.id.openo}"/>
			<input type="hidden" name="openo" value = "${opermodify.id.openo}"/>
			</td>
	
        </tr>
        <tr> 
           <td class="t6">原密码</td>
          <td class="t7"><input type="password" name="oldopepwd" maxlength="20"></td>
        </tr>
        <tr> 
           <td class="t6">新密码</td>
          <td class="t7"><input type="password" name="opepwd" maxlength="20" onChange="changevalue()"></td>
        </tr>
        <tr> 
          <td class="t6">确认密码</td>
          <td class="t7"><input type="password" name="pswdConfirm" maxlength="20"></td>
        </tr>
        <tr>
        <td height="6" colspan="2" bgcolor="ECECF5"></td>
      </tr>  
      <tr>
        <td height="40" colspan="4" align="center">
        	<input class="btnstyle" onmouseout="this.style.backgroundImage='url(images/btnbg.png)';" onmouseover="this.style.backgroundImage='url(images/btnbg2.png)';" type="submit" value="保存"/>
        </td>
      </tr>  
</table>
<table align="center" width="90%" style="vertical-align:bottom">
      <tr>
        <td height="6"  bgcolor="ECECF5"></td>
      </tr>
      <tr>
      <td>
      <font color="red">
      操作指南：<br/>
      1.如果修改密码，请在“新密码”和“确认新密码”两个输入框中，输入希望修改的新密码的内容，然后点击“保存”按钮，即完成新密码的修改；<br/>
      2.如果不输入密码，密码则保持不变。<br/>
      </font>
      </td>
      </tr>
</table>
 </form>
</body>
</html>

// Window Popup Style Constance

WS_NORMAL = ""; // Normal Style
WS_DIALOG = ""; // Fixed Border and no any bars and can not resize like a Dialog Window
WS_SIMPLE = ""; // Resizable and scrollable and no bars Window
WS_AL_CENTER = ""; // Center
WS_AL_LT = ""; // Left and Top Conner

// A boolean value express if the browser is Microsoft Internet Explorer
IS_IE = (navigator.appName == "Microsoft Internet Explorer");

if (IS_IE) {
    WS_NORMAL = ",statusbar,menubar,toolbar,scrollbars,resizable,location,directories";
    WS_DIALOG = ",statusbar=0,menubar=0,toolbar=0,scrollbars=0,resizable=0,location=0,directories=0";
    WS_SIMPLE = ",statusbar=0,menubar=0,toolbar=0,scrollbars,resizable,location=0,directories=0";
    WS_AL_CENTER = ",top=,left=";
    WS_AL_LT = ",top=0,left=0";
    WS_AL_RT = ",top=240,left=580";
    WS_AL_RIGHT = ",top=2000,left=2000";

}
else {
    WS_NORMAL = ",statusbar,menubar,toolbar,scrollbars,resizable,location,directories";
    WS_DIALOG = ",statusbar=0,menubar=0,toolbar=0,scrollbars=0,resizable=0,location=0,directories=0";
    WS_SIMPLE = ",statusbar=0,menubar=0,toolbar=0,scrollbars,resizable=1,location=0,directories=0";
    WS_AL_CENTER = ",screenX=,screenY=";
    WS_AL_LT = ",screenX=0,screenY=0";
    WS_AL_RT = ",screenX=300,screenY=700";
}


//reloads the window if Nav4 resized
function MM_reloadPage(init) {
    if (init==true) with (navigator) {
	if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
	    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage;
	}
    }
    else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
//MM_reloadPage(true);


// 将页面导入的Excel
 function exportPageToExcel(but)
{
	but.blur();
	document.execCommand('selectAll');
	document.execCommand('copy');
	document.execCommand('unselect');
	var oExcel = new ActiveXObject("Excel.Application");
	var oBook = oExcel.Workbooks.Add;
	var oSheet = oBook.Worksheets(1);
	oSheet.Paste();
    	//oSheet.Shapes("Picture 1").Select();
    	//oExcel.Selection.Delete;
	oExcel.Visible = true;
	oExcel.UserControl = true;

}

/**
 * Form Submit to specific URL in specific window
 * @param nform Name of form to be submited
 * @param toURL Action URL for the form
 * @param target	Name of target window
 */
function frmSubmit(nform, toURL, target) {
    obj = eval("document." + nform);
    if (toURL) obj.action = toURL;
    if (target) obj.target = target;
    //alert(obj);
    obj.submit();
}
/**
 * 将指定的表单提交给指定的URL
 * @param toURL  - Action URL for the form
 * @param msg    - confirm message
 * @param nform  - Name of form to be submited
 * @param target - Name of target window
 */

function formSubmit(toURL, msg, nform, target) {

    if(nform)
      obj = eval("document." + nform);
    else
      obj = eval("document.forms[0]");

    if(msg) {
      var con = window.confirm(msg);
      if(con) {
        if (toURL) obj.action = toURL;
        if (target) obj.target = target;
        obj.submit();
      }
    } else {

      if (toURL) obj.action = toURL;
      if (target) obj.target = target;
      obj.submit();
    }

}

/**
 * Form reset
 * @param nform Name of form to be reset
 */
function frmReset(nform) {
    if(nform)
      obj = eval("document." + nform);
    else
      obj = eval("document.forms[0]");
    obj.reset();
}
/**
 * Get value of field in the form
 * @param nform Name of form to be retrieved
 * @param nfield	Name of the field to be get value
 * @return	Value for field, if field is an array return value of the first item
 */
function frmGetValue(nform, nfield) {
    var obj = eval("document." + nform + "." + nfield);
    if (!obj.length) return obj.value;
    else return obj[0].value;
}

/**
 * Set value of field in the form
 * @param nform Name of form to be retrieved
 * @param nfield	Name of the field to be set value
 * @param value Value of the field to be set, if field is an array, set value of the first item
 */
function frmSetValue(nform, nfield, value) {
    obj = eval("document." + nform + "." + nfield);
    if(obj){
    	if (!obj.length) obj.value = value;
    	else obj[0].value = value;
    }
}

function frmSetMultValue(nform, nfield, value) {
    obj = eval("document." + nform + "." + nfield);
    if(obj){
    	var objvalue = obj.value;
    	if(!isInString(obj.value, value)){
    		if (!obj.length) obj.value += (' ' + value );
    		else obj[0].value += (' ' + value );
    	}
    }
}

/**
 * Get value array of field in the form
 * @param nform Name of form to be retrieved
 * @param nfield	Name of the field to be get value
 * @return	Value Array for field, if field is not an array,
 * return one length Array contains an element with value of the field.
 */
function frmGetValues(nform, nfield) {
    obj = eval("document." + nform + "." + nfield);
    vArray = new Array;
    if (!obj.length) {
	    vArray[0] = obj.value;
	    return vArray;
	}
    for (i=0; i<obj.length; i++) vArray[i] = obj[i].value;
    return vArray;
}

/**
 * Set values of field in the form
 * @param nform Name of form to be retrieved
 * @param nfield	Name of the field to be set value
 * @param valueArray	Value array of the field to be set,
 * if valueArray is not an array return
 * if field is not an array, set value with the first item of valueArray
 * The minimal length for valueArray and field of values will be set.
 */
function frmSetValues(nform, nfield, valueArray) {
    if (!valueArray.length) return;
    obj = eval("document." + nform + "." + nfield);
    if (!obj.length) obj.value = valueArray[0];
    else {
	uBound = obj.length;
	if (uBound>valueArray.length) uBound = valueArray.length;
	for (i=0; i<uBound; i++) {
	    obj[i].value = valueArray[i];
	}
    }
}

 function setSelectedValue(selobj, itemValue){

   // judge whether some records were selected.
   var size = 0
   var i
  // var opObj = eval("document.forms[0]." + selobj );
   if(selobj){
   if (selobj.length) {
      for (i=0;i<selobj.length;i++) {
          if (selobj[i].value == itemValue) {
		selobj[i].selected = true;
          }
      }
   }
  }

 }


  function setSelected(selobj, itemValue){

   // judge whether some records were selected.
   var size = 0
   var i
   var opObj = eval("document.forms[0]." + selobj );
   if(opObj){
   if (opObj.length) {
      for (i=0;i<opObj.length;i++) {
          if (opObj[i].value == itemValue) {
		opObj[i].selected = true;
          }
      }
   }
  }

 }


  function chkSelectMore(){

   // judge whether some records were selected.
   var size = 0
   var i
   var opObj = document.forms[0].selectId
   if(opObj){
 	if (opObj.length) {
      		for (i=0;i<opObj.length;i++) {
        	  if (opObj[i].status) {
  	       		 size++
            	  }
      	 	}
   	}
   	else {
        	if (opObj.status) size++
   	}
   	if (size == 0 ) {
   	// if no select any record, do nothing except alert
        	 alert ("您必须至少选择一条记录做此操作！")
         	return false;
   	}
   }else{
         return false;
   }
    return true;
  }

  function chkSelectOne(){

   // judge whether some records were selected.
   var size = 0
   var i
   var opObj = document.forms[0].selectId
   if(opObj){

   if (opObj.length) {
      for (i=0;i<opObj.length;i++) {
          if (opObj[i].status) {
  	        size++
          }
      }
   }
   else {
        if (opObj.status) size++
   }

   if (size != 1 ) {
   // if no select any record, do nothing except alert
         alert ("您必须选择一条记录做此操作！")
         return false;
   }
   }else{
         return false;
   }
    return true;
 }



/**
 * Popup a window
 * @param nWindow Popup window's name
 * @param URL	Popup window's URL
 * @param width Popup window's width, 0 means default width
 * @param height	Popup window's height, 0 means default height
 * @param style Popup window style can be WS_DIALOG, WS_NORMAL or WS_SIMPLE + WS_AL_CENTER or WS_AL_LT
 * @return	Popuped window object
 */
function Popup(nWindow, URL, width, height, style) {
    //URL = escape(URL);
    if (width==0) width = 600;
    if (height==0) height = 400;
    centerX = (window.screen.width - width);
    centerY = (window.screen.height - height);
    centerX = (centerX - (centerX % 2)) / 2 ;
    centerY = (centerY - (centerY % 2)) / 2;
    if (IS_IE) style = exchangeString(style, ",top=,left=", ",top=" + centerY + ",left=" + centerX);
    else style = exchangeString(style, ",screenX=,screenY=", ",screenX=" + centerX + ",screenY=" + centerY);

    var newWindow = window.open(URL, nWindow, "width=" + width + ",height=" + height + "copyhistory=0" + style);
    newWindow.focus();
    return newWindow;
}



function ValidateEmail(IsItReal)
{
	var ret = false;
	if (typeof(IsItReal) != "undefined")
	{
		IsItReal = IsItReal.match(/(\w+)@(.+)\.(\w+)$/);
		if (IsItReal !=null)
		{
			if ((IsItReal[3].length==2) || (IsItReal[3].length==3))
			ret = true;
		}
	}
	return ret;
}




var zf = new Array();
zf[0] = "";
zf[1] = "0";
zf[2] = "00";
zf[3] = "000";
zf[4] = "0000";
zf[5] = "00000";
zf[6] = "000000";
zf[7] = "0000000";


function trimToInt(str){
//	if (isNaN(str)) return "";
	var str = trim(str);
	if (str=="") return "";
	if (str=="0") return "0";
	while (str.charAt(0)=="0") str = str.substring(1);
	if (str=="") str = "0";
	return str;
}

function trimToHex(str){
	var str = trim(str.toUpperCase());
	if (str=="") return "";
	if (str=="0") return "0";
	while (str.charAt(0)=="0") str = str.substring(1);
	var idxC=0;
	while (idxC<str.length) {
		if ((str.charAt(idxC)>="0" && str.charAt(idxC)<="9") || (str.charAt(idxC)>="A" && str.charAt(idxC)<="F")) idxC++;
		else return "";
	}
	if (str=="") str = "0";
	return str;
}

function zeroFill(str, bit) {
	var str = trim(str);
	if (str.length<bit) str = zf[bit - str.length] + str;
	return str;
}


function chkLength(str, iLen){
	var iTotal = 0;
	for(i=0; i<str.length;i++) {
		iTotal += (str.charCodeAt(i)>255?2:1);
	}
	return (iTotal<=iLen);
}

function fixLength(str, iLen){
	var iTotal = 0;
	var iL=0;
	for(iL=0; iL<str.length; iL++) {
		if ( (iTotal+ (str.charCodeAt(iL)>255?2:1)) >iLen) break;
		iTotal += (str.charCodeAt(iL)>255?2:1);
	}
	return str.substring(0, iL);
}

function fixValue(oInput, iLen) {
	iL = 0;
	if (iLen) iL=iLen;
	else if (oInput.maxLength) iL = oInput.maxLength;
	if (!oInput) return;
	if (!iL) return;
	oInput.value = fixLength(oInput.value, iL);
}
function getFormatDate(iY, iM, iD, strFMT) {
	var sYYYY = "" + iY;
	var sMM = "" + iM;
	var sDD = "" + iD;
	sYYYY = zeroFill(sYYYY, 4);
	sMM = zeroFill(sMM, 2);
	sDD = zeroFill(sDD, 2);

	if (strFMT=="YYYY.MM.DD") return sYYYY + "." + sMM + "." + sDD;
	else if (strFMT=="MM/DD/YYYY") return sMM + "/" + sDD + "/" + sYYYY;
	else if (strFMT=="DD/MM/YYYY") return sDD + "/" + sMM + "/" + sYYYY;
	else return sYYYY + "." + sMM + "." + sDD;

}

function getYMD(strDate, strFMT) {
        var sYYYY = "";
        var sMM = "";
        var sDD = "";

        if (strFMT=="YYYY.MM.DD") {
                var vDate = strDate.match(/\d{4}\.\d{1,2}\.\d{1,2}/);
                if (!vDate) return 0;
                sYYYY = strDate.substring(0, strDate.indexOf("."));
                sMM = strDate.substring(strDate.indexOf(".")+1, strDate.lastIndexOf("."));
                sDD = strDate.substring(strDate.lastIndexOf(".")+1);
        }
        else if (strFMT=="MM/DD/YYYY") {
                var vDate = strDate.match(/\d{1,2}\/\d{1,2}\/\d{4}/);
                if (!vDate) return 0;
                sMM = strDate.substring(0, strDate.indexOf("/"));
                sDD = strDate.substring(strDate.indexOf("/")+1, strDate.lastIndexOf("/"));
                sYYYY = strDate.substring(strDate.lastIndexOf("/")+1);
        }
        else if (strFMT=="DD/MM/YYYY") {
                var vDate = strDate.match(/\d{1,2}\/\d{1,2}\/\d{4}/);
                if (!vDate) return 0;
                sDD = strDate.substring(0, strDate.indexOf("/"));
                sMM = strDate.substring(strDate.indexOf("/")+1, strDate.lastIndexOf("/"));
                sYYYY = strDate.substring(strDate.lastIndexOf("/")+1);
        }
        else return 0;
        if (sYYYY=="") return 0;
        if (sMM=="") return 0;
        if (sDD=="") return 0;
        var av = new Array();
        av[0] = sYYYY;
        av[1] = sMM;
        av[2] = sDD;
        return av;
}

function getFormatDate(strDate, strFMT) {
        var sYYYY = "";
        var sMM = "";
        var sDD = "";

        if( strDate.match(/\d{4}\-\d{1,2}\-\d{1,2}/) ) {      //  "YYYY.MM.DD" formated
                sYYYY = strDate.substring(0, strDate.indexOf("-"));
                sMM = strDate.substring(strDate.indexOf("-")+1, strDate.lastIndexOf("-"));
                sDD = strDate.substring(strDate.lastIndexOf("-")+1);
        }
        else if( strDate.match(/\d{4}\.\d{1,2}\.\d{1,2}/) ) {      //  "YYYY.MM.DD" formated
                sYYYY = strDate.substring(0, strDate.indexOf("."));
                sMM = strDate.substring(strDate.indexOf(".")+1, strDate.lastIndexOf("."));
                sDD = strDate.substring(strDate.lastIndexOf(".")+1);
        }
        else if( strDate.match(/\d{2}\.\d{1,2}\.\d{1,2}/) ) {      //  "YY.MM.DD" formated
                sYYYY = strDate.substring(0, strDate.indexOf("."));
                sMM = strDate.substring(strDate.indexOf(".")+1, strDate.lastIndexOf("."));
                sDD = strDate.substring(strDate.lastIndexOf(".")+1);
        }
        else if( strDate.match(/\d{1,2}\/\d{1,2}\/\d{4}/) ) {  //   "MM/DD/YYYY" formated
                sMM = strDate.substring(0, strDate.indexOf("/"));
                sDD = strDate.substring(strDate.indexOf("/")+1, strDate.lastIndexOf("/"));
                sYYYY = strDate.substring(strDate.lastIndexOf("/")+1);
        }
        else if ( strDate.match(/\d{4}\/\d{1,2}\/\d{1,2}/) ) { //   "YYYY/MM/DD" formated
                sYYYY = strDate.substring(0, strDate.indexOf("/"));
                sMM = strDate.substring(strDate.indexOf("/")+1, strDate.lastIndexOf("/"));
                sDD = strDate.substring(strDate.lastIndexOf("/")+1);
        }
        else if ( strDate.match(/\d{2}\/\d{1,2}\/\d{1,2}/) ) { //   "YY/MM/DD" formated
                sYYYY = strDate.substring(0, strDate.indexOf("/"));
                sMM = strDate.substring(strDate.indexOf("/")+1, strDate.lastIndexOf("/"));
                sDD = strDate.substring(strDate.lastIndexOf("/")+1);
        }
        else return 0;
        sYYYY = trimToInt(sYYYY);
        sMM = trimToInt(sMM);
        sDD = trimToInt(sDD);
        if (sYYYY=="") return 0;
        if (sMM=="") return 0;
        if (sDD=="") return 0;
        var av = new Array();
        av[0] = parseInt(sYYYY);
        av[1] = parseInt(sMM);
        av[2] = parseInt(sDD);
        if(checkDate(av)){
          sYYYY = zeroFill(sYYYY, 4);
          sMM = zeroFill(sMM, 2);
          sDD = zeroFill(sDD, 2);
          if (strFMT=="YYYY-MM-DD") {
            return sYYYY + "-" + sMM + "-" + sDD;
          }
          else if (strFMT=="YYYY.MM.DD") {
            return sYYYY + "." + sMM + "." + sDD;
          }
          else if (strFMT=="YY.MM.DD") {
            return sYYYY + "." + sMM + "." + sDD;
          }
          else if (strFMT=="MM/DD/YYYY") {
            return sMM + "/" + sDD + "/" + sYYYY;
          }
          else if (strFMT=="DD/MM/YYYY") {
            return sDD + "/" + sMM + "/" + sYYYY;
          }
          else if (strFMT=="YY/MM/DD") {
            return sYYYY + "/" + sMM + "/" + sDD;
          }
          else if (strFMT=="YY/DD/MM") {
            return sYYYY + "/" + sDD + "/" + sMM;
          }
        }
        else
          return 0;
}

function checkDate(av){
        var iY = av[0];
        var iM = av[1];
        var iD = av[2];
        if (iM<=0 || iM>12) return false;
        if (iD<=0 || iD>31) return false;
        if (iY<=999 || iY>9999) return false;
        if (iD<29) return true;
        if (iM==4 || iM==6 || iM==9 || iM==11) {
                if (iD>30) return false;
        }
        else if (iM==2) {
                if (iD>29) return false;
                if (iY<10000) {
                        if ((iY % 4)!=0) return false;
                        else if ((iY % 100)==0) return false;
                }
        }
        return true;

}
function checkDate(iM, iD, iY){
	if (iM<=0 || iM>12) return false;
	if (iD<=0 || iD>31) return false;
	if (iY<=999 || iY>9999) return false;
	if (iD<29) return true;
	if (iM==4 || iM==6 || iM==9 || iM==11) {
		if (iD>30) return false;
	}
	else if (iM==2) {
		if (iD>29) return false;
		if (iY<10000) {
			if ((iY % 4)!=0) return false;
			else if ((iY % 100)==0) return false;
		}
	}
	return true;

}

/**
 * Exchange the first matching string
 * @param orignal	String to be processed
 * @param oldString String to be exchanged in orignal
 * @param newString	String to be instead of oldString
 * @return	Processed string
 */
function exchangeString(orignal, oldString, newString) {
    pos = orignal.indexOf(oldString);
    if (pos<0) return orignal;
    prev = orignal.substr(0, pos);
    next = orignal.substr(pos + oldString.length);
    return prev + newString + next;
}

function isInString(orignal, str) {
    pos = orignal.indexOf(str);
    if (pos<0) return false;
    else return true;
}
/**************************
*
* 功能:
*  去掉字符串左边空格
*
 ***************************/
function ltrim(str) {
        while ( (str.charAt(0)==' ') && str.length > 0 )
        {
                str = str.substring(1);
        }
        return str;
}
/***************************
 *
 * 功能:
 *  去掉字符串右边空格
 *
 ***************************/
function rtrim(str) {
        while ( (str.charAt(str.length-1)==' ') && str.length > 0 )
        {
                str = str.substring(0,str.length-1);
        }
        return str;
}
/***************************
*
* 功能:
*  去掉字符串左右空格
*
 ***************************/

function trim(str) {
        return ltrim(rtrim(str));
}



/***************************
 * 1.功能:
 *  (1) 用来判断数据类型的一些基本函数
 *  (2) 数据格式转换函数
 *  (3) 校验合法性
 * 2.文件:common.js
 * 3.开发:David
 * 4.日期:2003/2/11
 * 5.修改:
 * 6.函数列表及其功能简述
 *
 *   (1) function isNull(pvalue)  //判断是否为空。如果为空返回true
 * *********************************************************************
 *   (2) function isInt(pvalue)  //判断已输入值是否为整数 如果为空值也返回true
 * *********************************************************************
 *   (3) function isFloat(pvalue) //判断已输入值是否为浮点数 如果为空值也返回true
 * *********************************************************************
 *   (4) function isNum(pvalue)   //判断已输入值是数值（包括整数和浮点数） 如果为空值也返回true
 * *********************************************************************
 *   (5) function isDate(pvalue)  //判断已输入值是日期（2002-03-03 & 2002-3-3） 如果为空值也返回true
 * *********************************************************************
 *   (6) function isHour(pvalue)  //判断已输入值是小时（0 -23） 如果为空值也返回true
 * *********************************************************************
 *   (7) function isMinute(pvalue)  //判断已输入值是小时（0 -59） 如果为空值也返回true
 * *********************************************************************
 *   (8) function replaceStr(Str,toMoveStr,toReplaceStr)  //将原字符串中指定的值用指定的值替换
 * *********************************************************************
 *   (9) function formatData(inputData)                  //将原字符串中影响HTML的字符替换成合理的格式
 * *********************************************************************
 *   (10) function dateCompare(SD,SH,SM,ED,EH,EM) 比较日期大小
 *      当开始时间大于结束时间时返回false
 *      反之 返回true
 *      参数：SD，SH，SM 分别是开始日期，时，分
 *           ED，EH，EM 分别是结束日期，时，分
 ************************************************************************
 *   (11)   function checkValue(pname,memo,type)
 *     校验合法性
 *         pname:form字段名
 *         memo:pname 描述
 *         type：指明pname.value 的数据类型 EMPTY(是否为空,较验必填项时应用);INT(int 数字) FLOAT(是有小数点)
 *               NUMBER(数字，包括整数，小数) ;DATE(日期 2002-12-12) ;HOUR(小时0-23)；MINUTE(分钟,0-59)
 *
 * * *********************************************************************
 *   (12) function dateIsMax(BD,BH,BM,SD,SH,SM) 比较日期大小
 *      当开始时间大于结束时间时返回false
 *      反之 返回true
 *      参数：SD，SH，SM 分别是认为小的日期，时，分
 *           BD，BH，BM 分别是认为大的日期，时，分
 ************************************************************************
 *
 ********************************************/


/***************************
 * 功能:
 * (1) 用来判断数据类型的一些基本函数
 ***************************/
 /********************************************************************************************
  * 功能:
  *  (3) 校验合法性 checkValue(pname,memo,type)
  *     pname:form字段名
  *     memo:pname 描述
  *     type：指明pname.value 的数据类型 EMPTY(是否为空,较验必填项时应用);INT(int 数字) FLOAT(是有小数点)
  *           NUMBER(数字，包括整数，小数) ;DATE(日期 2002-12-12) ;HOUR(小时0-23)；MINUTE(分钟,0-59)
  **********************************************************************************************/

 function checkValue(pname,memo,type)
 {
   var intType=0;
   if(type.toUpperCase()=="EMPTY"){intType=1;}
   if(type.toUpperCase()=="INT"){intType=2;}
   if(type.toUpperCase()=="FLOAT"){intType=3;}
   if(type.toUpperCase()=="NUMBER"){intType=4;}
   if(type.toUpperCase()=="DATE"){intType=5;}
   if(type.toUpperCase()=="HOUR"){intType=6;}
   if(type.toUpperCase()=="MINUTE"){intType=7;}
   if(intType==0){ alert("type参数错误，请指定正确的校验类型");pname.focus();return false;}

   //校验必填项
   if(intType==1)
   {
     if(isNull(pname.value)){alert("\""+memo+"\""+"为必填项！");pname.focus();return false;}
   }
   //校验整数数字
   if(intType==2)
   {
     if(!isInt(pname.value)){alert("\""+memo+"\""+"必须为整数！");pname.focus();return false;}
   }
   //校验小数数字
   if(intType==3)
   {
     if(!isFloat(pname.value)){alert("\""+memo+"\""+"必须为浮点数！");pname.focus();return false;}
   }
   //校验数字
   if(intType==4)
   {
     if(!isNum(pname.value)){alert("\""+memo+"\""+"必须为数值！");pname.focus();return false;}
   }
   //校验日期
   if(intType==5)
   {
   if(!isDate(pname.value)){alert("\""+memo+"\""+"必须为日期格式！参考：'2001-01-01'");pname.focus();return false;}
   }
   //校验小时
   if(intType==6)
   {
   if(!isHour(pname.value)){alert("\""+memo+"\""+"必须为时间小时格式！范围：'0-23'");pname.focus();return false;}
   }
   //校验分钟
   if(intType==7)
   {
   if(!isMinute(pname.value)){alert("\""+memo+"\""+"必须为时间分钟格式！范围：'0-59'");pname.focus();return false;}
   }
   return true;
 }
//判断是否为空。
function isNull(pvalue)
 {
   var RegularExp=/^\s*$/;
   if(RegularExp.test(pvalue))
   {
     return true;
   }
   return false;
 }

//判断已输入值是否为整数 如果为空值也返回true
 function isInt(pvalue)
 {
   var RegularExp = /^(\-)?(\d)*$/;
   if(!RegularExp.test(pvalue))
   {
     return false;
   }
   return true;
 }

//判断已输入值是否为浮点数 如果为空值也返回true
 function isFloat(pvalue)
 {
   if(pvalue==""){ return true;}
   var separator="."; //小数点
   var dateArray;
   dateArray= pvalue.split(separator);

   if(dateArray.length!=2)
   {
     return false;
   }
   else
   {
     if(!isInt(dateArray[0]))
     {
       return false;
     }
     if(!isInt(dateArray[1]))
     {
       return false;
     }
   }
   return true;
 }

//判断已输入值是数值（包括整数和浮点数） 如果为空值也返回true
 function isDigital(pvalue) {
   if(isFloat(pvalue)){return true;}
   if(isInt(pvalue)){return true;}
   return false;
 }

//判断已输入值否为数字字符
 function isNum(pvalue)
 {
	var exp = new RegExp("^[0-9]*$");
	return exp.test(pvalue);
 }


//判断已输入值否为字符、数字字符、中文
 function isValuableChar(pvalue)
 {

	var exp = new RegExp("^[A-Za-z0-9\u4E00-\u9FA5\uF900-\uFA2D_?？！，。”“：；]*$");
	return exp.test(pvalue); 
	
}

//判断已输入值是日期（2002-03-03 & 2002-3-3） 如果为空值也返回true
 function isDate(pvalue)
 {
   if(isNull(pvalue)){return true;}
   var strSeparator = "-"; //日期分隔符
   var strDateArray;
   var intYear;
   var intMonth;
   var intDay;
   var boolLeapYear;   //判断是平年还是润年
   strDateArray = pvalue.split(strSeparator);

   if(strDateArray.length!=3)
   {
     return false;
   }
   if(strDateArray[0].length!=4)
   {
     return false;
   }
   if(strDateArray[1].length>2)
   {
     return false;
   }
   if(strDateArray[2].length>2)
   {
     return false;
   }

   intYear = parseInt(strDateArray[0],10);
   intMonth = parseInt(strDateArray[1],10);
   intDay = parseInt(strDateArray[2],10);
   if(intYear<1000)
   {
     return false;
   }
   if(intYear>9999)
   {
     return false;
   }
   if(isNaN(intYear)||isNaN(intMonth)||isNaN(intDay))
   {
     return false;
   }

   if(intMonth>12||intMonth<1)
   {
     return false;
   }

   if((intMonth==1||intMonth==3||intMonth==5||intMonth==7||intMonth==8||intMonth==10||intMonth==12)&&(intDay>31||intDay<1))
   {
     return false;
   }

   if((intMonth==4||intMonth==6||intMonth==9||intMonth==11)&&(intDay>30||intDay<1))
   {
     return false;
   }

   if(intMonth==2){
     if(intDay<1)
     {
       return false;
     }

     boolLeapYear = false;
     if((intYear%100)==0){
       if((intYear%400)==0) boolLeapYear = true;
     }
     else{
       if((intYear%4)==0) boolLeapYear = true;
     }

     if(boolLeapYear){
       if(intDay>29)
       {
         return false;
       }
     }
     else{
       if(intDay>28)
       {
         return false;
       }
     }
   }
   return true;
 }
//判断已输入值是小时（0 -23） 如果为空值也返回true
 function isHour(pvalue)
 {
   if(isNull(pvalue))
   {
     return true;
   }
   if(!isInt(pvalue))
   {
     return false;
   }

   if(pvalue.length>2)
   {
     return false;
   }
   intPvalue = parseInt(pvalue,10);
   if((intPvalue>=0)&&(intPvalue<=23))
   {
     return true;
   }
   return false;
 }

//判断已输入值是小时（0 -59） 如果为空值也返回true
 function isMinute(pvalue)
 {
   if(isNull(pvalue))
   {
     return true;
   }
   if(!isInt(pvalue))
   {
     return false;
   }

   if(pvalue.length>2)
   {
     return false;
   }
   intPvalue = parseInt(pvalue,10);
   if((intPvalue>=0)&&(intPvalue<=59))
   {
     return true;
   }
   return false;
 }
 function isPhoneNum(pvalue)
 {
    var exp
	var exp = new RegExp("^[0-9]*-?[0-9]*-?[0-9]*$");
	return exp.test(pvalue);
 }
 function isEmail(pvalue)
 {
    var exp = new RegExp("^[A-Za-z0-9\u4E00-\u9FA5\uF900-\uFA2D_?？！，。. ”“：；]+@[A-Za-z0-9\u4E00-\u9FA5\uF900-\uFA2D_?？！，。. ”“：；]+$");
	return exp.test(pvalue); 
 }
 /***************************
  *
  * 功能:  dateIsMax(BD,BH,BM,SD,SH,SM,errorMemo) 比较日期大小
  * 当开始时间大于结束时间时返回false
  * 反之 返回true
  *   (12)  比较日期大小
  *      当开始时间大于结束时间时返回false
  *      反之 返回true
  *      参数：SD，SH，SM 分别是认为小的日期，时，分
  *           BD，BH，BM 分别是认为大的日期，时，分
  *           errorMemo 当返回false时，的提示信息。
  *
  ***************************/
function dateIsMax(BD,BH,BM,SD,SH,SM,errorMemo)
  {
    if(trim(BD.value) == '')
    {
      if(trim(SD.value) != '')
      {
        alert("起始时间不能为空");
        BD.focus();
        return false;
      }
    }
    else
    {
      if(dateCompare(SD.value,SH.value,SM.value,BD.value,BH.value,BM.value))
      {
        alert(errorMemo);
        BD.focus();
        return false;
      }
    }
    return true;
  }
 /***************************
  *
  * 功能: dateCompare() 比较日期大小
  * 当开始时间大于结束时间时返回false
  * 反之 返回true
  * 参数：SD，SH，SM 分别是开始日期，时，分
  *     ED，EH，EM 分别是结束日期，时，分
  *
  ***************************/

 function dateCompare(SD,SH,SM,ED,EH,EM){
  var strSeparator = "-"; //日期分隔符
  var SDateArray;
  var intSYear;
  var intSMonth;
  var intSDay;
  var EDateArray;
  var intEYear;
  var intEMonth;
  var intEDay;

  SDateArray = SD.split(strSeparator);
  intSYear = parseInt(SDateArray[0],10);
  intSMonth = parseInt(SDateArray[1],10);
  intSDay = parseInt(SDateArray[2],10);

  EDateArray = ED.split(strSeparator);
  intEYear = parseInt(EDateArray[0],10);
  intEMonth = parseInt(EDateArray[1],10);
  intEDay = parseInt(EDateArray[2],10);


  if(intSYear<intEYear)
  {
    return true;
  }
  if((intSYear==intEYear)&&(intSMonth<intEMonth))
  {
    return true;
  }
  if((intSYear==intEYear)&&(intSMonth==intEMonth)&&(intSDay<intEDay))
  {
    return true;
  }
  if((intSYear==intEYear)&&(intSMonth==intEMonth)&&(intSDay==intEDay)&&(SH<EH))
  {
    return true;
  }
  if((intSYear==intEYear)&&(intSMonth==intEMonth)&&(intSDay==intEDay)&&(SH==EH)&&(SM<EM))
  {
    return true;
  }
  return false;
 }

 /***************************
  *
  * 功能:
  *  (2) 数据格式转换函数
  *
  ***************************/

//将原字符串中指定的值用指定的值替换
 function replaceStr(Str,toMoveStr,toReplaceStr)
 {
   var returnStr="";
   var sourceStr=Str.toString();
   while (sourceStr.length>0 && sourceStr.indexOf(toMoveStr)!=-1)
   {
     var curPlace=sourceStr.indexOf(toMoveStr);
     returnStr+=sourceStr.substring(0,curPlace)+toReplaceStr;
     sourceStr=sourceStr.substring(curPlace+toMoveStr.length,sourceStr.length);
   }
   returnStr+=sourceStr;
   return returnStr;
 }

//将原字符串中影响HTML的字符替换成合理的格式
 function formatData(inputData)
 {
   var strData="";
   strData=replaceStr(inputData,"&","&amp;");
   strData=replaceStr(strData,"\"","&quot;");
   strData=replaceStr(strData,"'","&apos;");
   strData=replaceStr(strData,"<","&lt;");
   strData=replaceStr(strData,">","&gt;");
   strData=replaceStr(strData,"\n","&#13;");
   return strData;
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
/***************************
*
* 功能:
*  任意日期类型的校验yyyy-MM-dd
*
 ***************************/
function isDate(str){
	var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
	if(r==null)return false; var d = new Date(r[1], r[3]-1, r[4]);
		return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
}

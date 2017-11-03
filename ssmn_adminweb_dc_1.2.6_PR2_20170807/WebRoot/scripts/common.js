/***************************
 * 1.����:
 *  (1) �����ж��������͵�һЩ��������
 *  (2) ���ݸ�ʽת������
 *  (3) У��Ϸ���
 * 2.�ļ�:common.js
 * 3.����:David
 * 4.����:2003/2/11
 * 5.�޸�:
 * 6.�����б��书�ܼ���
 *
 *   (1) function isNull(pvalue)  //�ж��Ƿ�Ϊ�ա����Ϊ�շ���true
 * *********************************************************************
 *   (2) function isInt(pvalue)  //�ж�������ֵ�Ƿ�Ϊ���� ���Ϊ��ֵҲ����true
 * *********************************************************************
 *   (3) function isFloat(pvalue) //�ж�������ֵ�Ƿ�Ϊ������ ���Ϊ��ֵҲ����true
 * *********************************************************************
 *   (4) function isNum(pvalue)   //�ж�������ֵ����ֵ�����������͸������� ���Ϊ��ֵҲ����true
 * *********************************************************************
 *   (5) function isDate(pvalue)  //�ж�������ֵ�����ڣ�2002-03-03 & 2002-3-3�� ���Ϊ��ֵҲ����true
 * *********************************************************************
 *   (6) function isHour(pvalue)  //�ж�������ֵ��Сʱ��0 -23�� ���Ϊ��ֵҲ����true
 * *********************************************************************
 *   (7) function isMinute(pvalue)  //�ж�������ֵ��Сʱ��0 -59�� ���Ϊ��ֵҲ����true
 * *********************************************************************
 *   (8) function replaceStr(Str,toMoveStr,toReplaceStr)  //��ԭ�ַ�����ָ����ֵ��ָ����ֵ�滻
 * *********************************************************************
 *   (9) function formatData(inputData)                  //��ԭ�ַ�����Ӱ��HTML���ַ��滻�ɺ���ĸ�ʽ
 * *********************************************************************
 *   (10) function dateCompare(SD,SH,SM,ED,EH,EM) �Ƚ����ڴ�С
 *      ����ʼʱ����ڽ���ʱ��ʱ����false
 *      ��֮ ����true
 *      ������SD��SH��SM �ֱ��ǿ�ʼ���ڣ�ʱ����
 *           ED��EH��EM �ֱ��ǽ������ڣ�ʱ����
 ************************************************************************
 *   (11)   function checkValue(pname,memo,type)
 *     У��Ϸ���
 *         pname:form�ֶ���
 *         memo:pname ����
 *         type��ָ��pname.value ���������� EMPTY(�Ƿ�Ϊ��,���������ʱӦ��);INT(int ����) FLOAT(����С����)
 *               NUMBER(���֣�����������С��) ;DATE(���� 2002-12-12) ;HOUR(Сʱ0-23)��MINUTE(����,0-59)
 *
 * * *********************************************************************
 *   (12) function dateIsMax(BD,BH,BM,SD,SH,SM) �Ƚ����ڴ�С
 *      ����ʼʱ����ڽ���ʱ��ʱ����false
 *      ��֮ ����true
 *      ������SD��SH��SM �ֱ�����ΪС�����ڣ�ʱ����
 *           BD��BH��BM �ֱ�����Ϊ������ڣ�ʱ����
 ************************************************************************
 *
 ********************************************/


/***************************
 * ����:
 * (1) �����ж��������͵�һЩ��������
 ***************************/
 /********************************************************************************************
  * ����:
  *  (3) У��Ϸ��� checkValue(pname,memo,type)
  *     pname:form�ֶ���
  *     memo:pname ����
  *     type��ָ��pname.value ���������� EMPTY(�Ƿ�Ϊ��,���������ʱӦ��);INT(int ����) FLOAT(����С����)
  *           NUMBER(���֣�����������С��) ;DATE(���� 2002-12-12) ;HOUR(Сʱ0-23)��MINUTE(����,0-59)
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
   if(intType==0){ alert("type����������ָ����ȷ��У������");pname.focus();return false;}

   //У�������
   if(intType==1)
   {
     if(isNull(pname.value)){alert("\""+memo+"\""+"Ϊ�����");pname.focus();return false;}
   }
   //У����������
   if(intType==2)
   {
     if(!isInt(pname.value)){alert("\""+memo+"\""+"����Ϊ������");pname.focus();return false;}
   }
   //У��С������
   if(intType==3)
   {
     if(!isFloat(pname.value)){alert("\""+memo+"\""+"����Ϊ��������");pname.focus();return false;}
   }
   //У������
   if(intType==4)
   {
     if(!isNum(pname.value)){alert("\""+memo+"\""+"����Ϊ��ֵ��");pname.focus();return false;}
   }
   //У������
   if(intType==5)
   {
   if(!isDate(pname.value)){alert("\""+memo+"\""+"����Ϊ���ڸ�ʽ���ο���'2001-01-01'");pname.focus();return false;}
   }
   //У��Сʱ
   if(intType==6)
   {
   if(!isHour(pname.value)){alert("\""+memo+"\""+"����Ϊʱ��Сʱ��ʽ����Χ��'0-23'");pname.focus();return false;}
   }
   //У�����
   if(intType==7)
   {
   if(!isMinute(pname.value)){alert("\""+memo+"\""+"����Ϊʱ����Ӹ�ʽ����Χ��'0-59'");pname.focus();return false;}
   }
   return true;
 }
//�ж��Ƿ�Ϊ�ա�
function isNull(pvalue)
 {
   var RegularExp=/^\s*$/;
   if(RegularExp.test(pvalue))
   {
     return true;
   }
   return false;
 }

//�ж�������ֵ�Ƿ�Ϊ���� ���Ϊ��ֵҲ����true
 function isInt(pvalue)
 {
   var RegularExp = /^(\-)?(\d)*$/;
   if(!RegularExp.test(pvalue))
   {
     return false;
   }
   return true;
 }

//�ж�������ֵ�Ƿ�Ϊ������ ���Ϊ��ֵҲ����true
 function isFloat(pvalue)
 {
   if(pvalue==""){ return true;}
   var separator="."; //С����
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

//�ж�������ֵ����ֵ�����������͸������� ���Ϊ��ֵҲ����true
 function isDigital(pvalue) {
   if(isFloat(pvalue)){return true;}
   if(isInt(pvalue)){return true;}
   return false;
 }

//�ж�������ֵ��Ϊ�����ַ�
 function isNum(pvalue)
 {
	var exp = new RegExp("^[0-9]*$");
	return exp.test(pvalue);
 }


//�ж�������ֵ��Ϊ�ַ��������ַ�������
 function isValuableChar(pvalue)
 {

	var exp = new RegExp("^[A-Za-z0-9\u4E00-\u9FA5\uF900-\uFA2D_?����������������]*$");
	return exp.test(pvalue); 
	
}

//�ж�������ֵ�����ڣ�2002-03-03 & 2002-3-3�� ���Ϊ��ֵҲ����true
 function isDate(pvalue)
 {
   if(isNull(pvalue)){return true;}
   var strSeparator = "-"; //���ڷָ���
   var strDateArray;
   var intYear;
   var intMonth;
   var intDay;
   var boolLeapYear;   //�ж���ƽ�껹������
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
//�ж�������ֵ��Сʱ��0 -23�� ���Ϊ��ֵҲ����true
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

//�ж�������ֵ��Сʱ��0 -59�� ���Ϊ��ֵҲ����true
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
    var exp = new RegExp("^[A-Za-z0-9\u4E00-\u9FA5\uF900-\uFA2D_?��������. ��������]+@[A-Za-z0-9\u4E00-\u9FA5\uF900-\uFA2D_?��������. ��������]+$");
	return exp.test(pvalue); 
 }
 /***************************
  *
  * ����:  dateIsMax(BD,BH,BM,SD,SH,SM,errorMemo) �Ƚ����ڴ�С
  * ����ʼʱ����ڽ���ʱ��ʱ����false
  * ��֮ ����true
  *   (12)  �Ƚ����ڴ�С
  *      ����ʼʱ����ڽ���ʱ��ʱ����false
  *      ��֮ ����true
  *      ������SD��SH��SM �ֱ�����ΪС�����ڣ�ʱ����
  *           BD��BH��BM �ֱ�����Ϊ������ڣ�ʱ����
  *           errorMemo ������falseʱ������ʾ��Ϣ��
  *
  ***************************/
function dateIsMax(BD,BH,BM,SD,SH,SM,errorMemo)
  {
    if(trim(BD.value) == '')
    {
      if(trim(SD.value) != '')
      {
        alert("��ʼʱ�䲻��Ϊ��");
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
  * ����: dateCompare() �Ƚ����ڴ�С
  * ����ʼʱ����ڽ���ʱ��ʱ����false
  * ��֮ ����true
  * ������SD��SH��SM �ֱ��ǿ�ʼ���ڣ�ʱ����
  *     ED��EH��EM �ֱ��ǽ������ڣ�ʱ����
  *
  ***************************/

 function dateCompare(SD,SH,SM,ED,EH,EM){
  var strSeparator = "-"; //���ڷָ���
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
  * ����:
  *  (2) ���ݸ�ʽת������
  *
  ***************************/

//��ԭ�ַ�����ָ����ֵ��ָ����ֵ�滻
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

//��ԭ�ַ�����Ӱ��HTML���ַ��滻�ɺ���ĸ�ʽ
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
* ����:
*  �����������͵�У��yyyy-MM-dd
*
 ***************************/
function isDate(str){
	var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
	if(r==null)return false; var d = new Date(r[1], r[3]-1, r[4]);
		return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
}

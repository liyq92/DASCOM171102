package com.dascom.OPadmin.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 分页显示器。格式为：
 * <table width="200" border="0">
 * <tr>
 * <td width = "40%">共3页 当前第1页</td>
 *   <td width="30%">上一页</td>
 *   <td width="30%">下一页</td>
 * </tr>
 * </table>
 * 调用方式为：<public:page currentPage = "1" url="design.jsp?page="/>
 * 注：不需要写htt://localhost:8080。系统将自动得到其端口和应用名
 * */
public class PageLocator
    extends TagSupport {
  //private int pageCount = -1; //总页数。总页数为-1时，不显示"共3页"字样
  private int currentPage;
  private int recordCount = 0;//纪录总数
  private int pageSize = 10;//每页的纪录数
  private String url = "#";
  private String function = "";
  private String align = "right";
  private String formName = "";//form's name
  /**
   * 设置记录总数
   * */
  public void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }
  /**
   * 设置每页的纪录数
   * */
  public void setPageSize(int pageSize) {
    if(pageSize>0) {
      this.pageSize = pageSize;
    }
  }
  /**
   * 设置当前页数
   * @param currentPage int 当前页数
   * */
  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * 设置url
   * @param url String
   * */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * set form's name
   * */
  public void setFormName(String name) {
    this.formName = name;
  }
  /**
   * 设置函数名。可空
   * */
  public void setFunction(String function) {
    this.function = function;
  }
  /**
   * 设置排列方式
   * */
  public void setAlign(String align) {
    this.align = align;
  }

  /**
   * doEndTag
   * */
  public int doEndTag() throws javax.servlet.jsp.JspException {
    //得到总页数
    int pageCount;
   // float f = Float.intBitsToFloat(recordCount)/Float.intBitsToFloat(pageSize);
    if(recordCount%pageSize > 0) {
      pageCount = recordCount/pageSize + 1;
    } else {
      pageCount = recordCount/pageSize;
    }


    //根据参数得到
    javax.servlet.http.HttpServletRequest request = (HttpServletRequest)
        pageContext.getRequest();
    StringBuffer contextPath = new StringBuffer("http://");
    contextPath.append(request.getServerName()).append(":");
    contextPath.append(request.getServerPort()).append("/");
    contextPath.append(request.getContextPath()).append("/");
    //    request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
    StringBuffer html = new StringBuffer("");
    int priorPage, nextPage;
    priorPage = currentPage - 1;
    nextPage = currentPage + 1;
    if(formName.equals("")) {
      this.formName = "pageFrm";
      html.append("<form name=pageFrm method=get >");
    }
    
    ///----------------------------------------------------
    
    html.append("<div class='pagin' style='width:99%;'>");
    html.append(" <div class='message'> ");
    html.append("<div id='labelid' style='height: 28px;width:70px;text-align:center;border: 1px solid #DDD;line-height:30px;"+
    	    "border-bottom-left-radius: 5px;"+
    	    "border-top-left-radius: 5px;border-bottom-right-radius: 5px;border-top-right-radius: 5px;color: #3399d5;"+
    	    "position: absolute;top: 0;right:380px;'><label class='paginItem'>共")
    	    .append(Integer.toString(recordCount))
    		.append("条</label></div>&nbsp;");
    html.append("<ul class='paginList'> ");
   
    //第1页：　< 1 2 ... 235 >
    //第2页：　< 1 2 3 ... 235 >
    //第3页：　< 1 2 3 4 ... 235 >
    //第>=4页：　< 1 ... 3 4 5 ... 235 >
    int pcount =0;
    if(pageCount >0)
    {
    	String spageItemFlag ="paginItem";
    	if(currentPage >1)//前一页
    	{
    		html.append("<li class='paginItem' style='border-bottom-left-radius: 5px;border-top-left-radius: 5px;'><a href=javascript:gotoPageNum('")
    		.append(Integer.toString(priorPage)).append("');><span class='pagepre'></span></a></li>");
    		pcount++;
    	}
    	//第1页
    	if(currentPage == 1)
    		spageItemFlag = "paginItem current";
    	else
    		spageItemFlag = "paginItem";
    	html.append("<li class='").append(spageItemFlag).append("' style='border-bottom-left-radius: 5px;border-top-left-radius: 5px;'><a href=javascript:gotoPageNum('1');>1</a></li>");
    	pcount++;
    	if(currentPage >=4)//第一个省略号 ...
    	{
    		html.append("<li class='paginItem more'><a href=javascript:;>...</a></li>");
    		pcount++;
    	}
    	
    	//第2,3,4页
    	if(currentPage <=3)
    	{
    		for(int i=2;i<=currentPage+1;i++)
    		{
    			if(currentPage == i)
    				spageItemFlag = "paginItem current";
    			else
    	    		spageItemFlag = "paginItem";
    			if(i<=pageCount)
    			{
    				html.append("<li class='").append(spageItemFlag).append("'><a href=javascript:gotoPageNum('")
    					.append(Integer.toString(i)).append("');>").append(Integer.toString(i)).append("</a></li>");
    				pcount++;
    			}
    		}
    	}
    	if(currentPage >3 && currentPage<=pageCount-3)
    	{
    			html.append("<li class='paginItem'><a href=javascript:gotoPageNum('")
    				.append(Integer.toString(currentPage-1)).append("');>").append(Integer.toString(currentPage-1)).append("</a></li>");
    			html.append("<li class='paginItem current'><a href=javascript:gotoPageNum('")
					.append(Integer.toString(currentPage)).append("');>").append(Integer.toString(currentPage)).append("</a></li>");
    			html.append("<li class='paginItem'><a href=javascript:gotoPageNum('")
					.append(Integer.toString(currentPage+1)).append("');>").append(Integer.toString(currentPage+1)).append("</a></li>");
    			pcount= pcount+3;
    	}
    	
    	if( currentPage<=pageCount-3)
    	{
    		//第二个省略号 ...
    		html.append("<li class='paginItem more'><a href=javascript:;>...</a></li>");
    		pcount++;
    	
	    	//最后一页
    		if(currentPage == pageCount)
				spageItemFlag = "paginItem current";
			else
	    		spageItemFlag = "paginItem";
	    	html.append("<li class='").append(spageItemFlag).append("'><a href=javascript:gotoPageNum('")
	    		.append(Integer.toString(pageCount)).append("')>").append(pageCount).append("</a></li>");
	    	pcount++;
    	}
    	//例如：一共325页：　 325时：　<1 ... 324 325 
    	//				   324时：  <1 ... 323 324 325 >
    	//                 323时：  <1 ... 322 323 324 325 >
    	if(currentPage>pageCount-3 && pageCount>3)
    	{
    		for(int i=(pageCount-currentPage+1);i>=0;i--)
    		{
    			if(pageCount == currentPage+i)
    				spageItemFlag = "paginItem current";
    			else
    	    		spageItemFlag = "paginItem";
    			if(pageCount-i<=pageCount)
    			{
    				html.append("<li class='").append(spageItemFlag).append("'><a href=javascript:gotoPageNum('")
    					.append(Integer.toString(pageCount-i)).append("');>").append(Integer.toString(pageCount-i)).append("</a></li>");
    				pcount++;
    			}
    		}
    	}
    	
    	//下一页
    	if(currentPage < pageCount)
    	{
    		html.append("<li class='paginItem'><a href=javascript:gotoPageNum('")
    			.append(Integer.toString(nextPage)).append("');><span class='pagenxt'></span></a></li>");
    		pcount++;
    	}
    }
    
    html.append("</ul>");
    html.append("</div>");
   
    if(formName.equals("")) {
      html.append("</form>");
    }
    html.append("<script language=javascript>");
    html.append("function gotoPage() {  ");
    html.append(" var page = ").append(formName).append(".__page.value;");
    html.append("if(page>").append(Integer.toString(pageCount)).append(") {page=").append(Integer.toString(pageCount)).append(";}");
    html.append("if(page<=0) {return;}");
    html.append(" var url=\"").append(url).append("\"+page; ");
    //html.append(" document.location.href=url;");
    html.append(formName).append(".action = url; ");
    html.append(formName).append(".submit(); ");
    html.append("}");

    html.append("function gotoPageNum(page) {  ");
    html.append(" var url=\"").append(url).append("\"+page; ");
    html.append(formName).append(".action = url; ");
    html.append(formName).append(".submit(); ");
    html.append("   }");

    html.append("</script>");

    //左边页数,这样计算: 31(每个格宽度)*5(一共有几个格)+25(这个宽度大概估计的)
    try {
    	int totalwid = 31*pcount+25;
    	String shtml = html.toString();
    	if(html.length() >0)
    		shtml = shtml.replaceAll("right:380px;", "right:"+totalwid+"px;");
    	
    	pageContext.getOut().println(shtml);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return EVAL_PAGE;
  }

}

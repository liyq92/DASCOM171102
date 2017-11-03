package com.dascom.ssmn.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorService;
import com.dascom.OPadmin.service.impl.OperatorServImpl;
import com.dascom.OPadmin.util.Md5PasswordEncoder;
import com.dascom.ssmn.dao.*;

import com.dascom.ssmn.util.Constants;

public class ModifyOperatorInfo  extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(ModifyOperatorInfo.class);
	private static int icountnum = 0;
	private  zydcOperatorDao dao = new zydcOperatorDao();
	private IAdminOperator operatorDao = new AdminOperatorImpl();
	private IOperatorService operatorServ = new OperatorServImpl();
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		String msg ;
		String sname = opera.getLoginName();
		String spwd= opera.getOpepwd();
		String service = Constants.servicekey_dc;
		String stype = req.getParameter("type");
		String newopepwd = null;
		String opepwdok = null;
		String opepwd = null;
		
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		if(!"".equals(stype) && stype.equals("2"))//密码确认
		{
			opepwd = req.getParameter("opepwd");
			newopepwd = req.getParameter("newopepwd");
			opepwdok = req.getParameter("opepwdok");
			
			String md5opepwd = md5.encodePassword(opepwd, "12345");
			if(!"".equals(md5opepwd) && md5opepwd.equals(spwd))
			{
				//输入的原密码是对的
					if(!"".equals(newopepwd) && !"".equals(opepwdok) && newopepwd.equals(opepwdok))
					{
						//新密码和确认密码一致
						
						String newopepwdencode = md5.encodePassword(newopepwd, "12345");
						String opepwdokencode = md5.encodePassword(opepwdok, "12345");
						if(newopepwdencode.equals(opepwdokencode) )
						{
							//修改密码
											
							List<TyadminOperators> tempList = operatorDao.findByOpeno(sname,service);
							if(tempList.size() <= 0) {
								msg = Constants.MESSAGE_OPERATOR_NOT_EXSIT;
							} else {
								TyadminOperators operator = (TyadminOperators)tempList.get(0);
								operator.setOpepwd(newopepwdencode);
								
								dao.updateOpera(operator, service,sname);
									
							}
		
							//req.setAttribute("msg", "修改密码成功,请重新登录！");
							//修改完密码，要退出登录
							req.setAttribute("msg1", "修改密码成功,请重新登录！");
							//删除session
							session.removeAttribute(Constants.OPERATOR_IN_SESSION);
							session.invalidate();
							return new ActionForward("/forward.jsp");
						}
						else
						{
							req.setAttribute("msg", "新密码和确认密码不一致!");
						}
					}else
					{
						req.setAttribute("msg", "新密码和确认密码不一致!");
					}
			}else
			{
				req.setAttribute("msg", "输入的原密码有误，请重新输入!");
			}
			
			req.setAttribute("opepwd", opepwd);
			req.setAttribute("newopepwd", newopepwd);
			req.setAttribute("opepwdok", opepwdok);
			return mapping.findForward("formPasswordView");
		}
		else if(!"".equals(stype) && stype.equals("3"))//密码重置
		{
			String sopeno = (String)req.getParameter("openoo");

			List<TyadminOperators> tempList = operatorDao.findByOpeno(sopeno,service);
			if(tempList.size() <= 0) {
				msg = Constants.MESSAGE_OPERATOR_NOT_EXSIT;
			} else {
				TyadminOperators operator = (TyadminOperators)tempList.get(0);
				String opepwdokencode = md5.encodePassword("000000", "12345");
				operator.setOpepwd(opepwdokencode);
				msg = "重置密码成功!";
				
				dao.updateOpera(operator, service,sopeno);			
			}
			req.setAttribute("msg", msg);
			req.setAttribute("openoo", sopeno);
			return mapping.findForward("formResetPasswordView");
		}
	
		if(!"".equals(stype) && stype.equals("1"))
		{
			req.setAttribute("loginname", sname);
			return mapping.findForward("formPasswordView");
		}
		if(!"".equals(stype) && stype.equals("4"))
			return mapping.findForward("formResetPasswordView");
		
		return mapping.findForward("formPasswordView");
		
	}

	
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		return this.processSubmit(mapping, form, req, res);
	}
}

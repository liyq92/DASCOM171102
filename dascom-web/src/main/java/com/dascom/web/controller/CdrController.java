package com.dascom.web.controller;

import com.jfinal.core.Controller;

public class CdrController extends Controller{

	public void querycdrinfo(){
		render("/jsp/zydc/cdr_list.jsp");
	}
}

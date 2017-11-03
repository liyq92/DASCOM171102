package com.dascom.web.config;

import javax.sql.DataSource;

import org.beetl.sql.ext.jfinal.JFinalBeetlSql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.dascom.web.controller.BaseController;
import com.dascom.web.controller.CdrController;
import com.dascom.web.util.BaseConsts;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.FakeStaticHandler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.render.ViewType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setViewType(ViewType.JSP);
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", BaseController.class);
		me.add("/cdr", CdrController.class);

	}

	@Override
	public void configPlugin(Plugins me) {
		Prop prop2 = PropKit.use("db.properties", BaseConsts.ENCODING);
		DataSource dataSource = null;
		try {
			dataSource = DruidDataSourceFactory.createDataSource(prop2.getProperties());
		} catch (Exception e) {
			log.info("创建数据库连接失败 ：\n" + e);
			return;
		}

		JFinalBeetlSql.init(dataSource, null);
	}

	@Override
	public void configInterceptor(Interceptors me) {

	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new FakeStaticHandler(".do"));
	}

}

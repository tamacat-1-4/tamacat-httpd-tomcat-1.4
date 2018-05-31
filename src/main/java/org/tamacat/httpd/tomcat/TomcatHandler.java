/*
 * Copyright (c) 2012 tamacat.org
 * All rights reserved.
 */
package org.tamacat.httpd.tomcat;

import java.io.File;
import java.net.URL;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.tamacat.httpd.config.DefaultReverseUrl;
import org.tamacat.httpd.config.ReverseUrl;
import org.tamacat.httpd.config.ServiceUrl;
import org.tamacat.httpd.handler.ReverseProxyHandler;
import org.tamacat.httpd.tomcat.util.ServerUtils;
import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;
import org.tamacat.util.StringUtils;

/**
 * The reverse proxy handler using the embedded Tomcat.
 */
public class TomcatHandler extends ReverseProxyHandler {

	static final Log LOG = LogFactory.getLog(TomcatHandler.class);

	protected String serverHome;
	protected String tomcatHost = "http://localhost";
	protected int port = 8080;
	protected String webapps = "./webapps";
	protected String work = "${server.home}";
	protected Tomcat tomcat;
	
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) {
		super.handle(request, response, context);
	}

	@Override
	public void setServiceUrl(ServiceUrl serviceUrl) {
		super.setServiceUrl(serviceUrl);
		ReverseUrl reverseUrl = new DefaultReverseUrl(serviceUrl);
		try {
			reverseUrl.setReverse(new URL(tomcatHost + ":" + port + serviceUrl.getPath()));
			serviceUrl.setReverseUrl(reverseUrl);

			tomcat = TomcatManager.getInstance(port);
			tomcat.setBaseDir(getWork());
			String contextRoot = getWebapps() + serviceUrl.getPath();

			// ProtectionDomain domain = TomcatHandler.class.getProtectionDomain();
			// URL location = domain.getCodeSource().getLocation();
			String baseDir = new File(contextRoot).getAbsolutePath();
			Context ctx = tomcat.addWebapp(serviceUrl.getPath().replaceAll("/$", ""), baseDir);
			ctx.setParentClassLoader(getClassLoader());
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}
	}

	public void setPort(int port) {
		this.port = port;
	}
	
    public void setServerHome(String serverHome) {
    	this.serverHome = serverHome;
    }
    
    protected String getServerHome() {
    	if (StringUtils.isEmpty(serverHome)) {
    		serverHome = ServerUtils.getServerHome();
    	}
    	return serverHome;
    }

	public void setWebapps(String webapps) {
		if (work.indexOf("${server.home}") >= 0) {
			this.webapps = webapps.replace("${server.home}", getServerHome()).replace("\\", "/");
		} else {
			this.webapps = webapps;
		}
	}

	protected String getWebapps() {
		return webapps;
	}

	public void setWork(String work) {
		this.work = work;
	}

	protected String getWork() {
		if (work.indexOf("${server.home}") >= 0) {
			this.work = work.replace("${server.home}", getServerHome()).replace("\\", "/");//.replaceAll("/work$", "");
		}
		System.out.println(work);
		return work;
	}
}

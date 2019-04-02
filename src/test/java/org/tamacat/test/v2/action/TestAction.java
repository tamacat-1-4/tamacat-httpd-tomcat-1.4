package org.tamacat.test.v2.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestAction {
	
	public void main(HttpServletRequest req, HttpServletResponse resp) {
		req.setAttribute("version", "v2");
	}
}

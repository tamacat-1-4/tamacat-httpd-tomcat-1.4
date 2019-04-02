package org.tamacat.test.v1.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestAction {

	public void main(HttpServletRequest req, HttpServletResponse resp) {
		req.setAttribute("version", "v1");
	}
}

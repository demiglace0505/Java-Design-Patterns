package com.demiglace.patterns.frontcontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewStudentCommand implements Command {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		StudentVo vo = new StudentVo(1, "Doge");
		request.setAttribute("studentDetails", vo);
		return "showStudentDetails";
	}
}

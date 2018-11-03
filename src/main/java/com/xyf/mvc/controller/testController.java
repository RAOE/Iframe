package com.xyf.mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xyf.mvc.annotation.Autowired;
import com.xyf.mvc.annotation.RequestMapping;
import com.xyf.mvc.annotation.RequestParam;
import com.xyf.mvc.annotation.xyfController;
import com.xyf.mvc.service.XyfService;

@xyfController
@RequestMapping("/test")
public class testController {
	
	@Autowired("ServiceImp")
	private XyfService xyfService;
	@RequestMapping("/query")
	public void query(HttpServletRequest request, HttpServletResponse response, @RequestParam("text") String text) throws IOException {

		 PrintWriter ps=response.getWriter();
		 String result=xyfService.query(text);
		 ps.write(result); 
		 
		 
	}

}

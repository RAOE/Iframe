package com.xyf.mvc.serviceImp;

import com.xyf.mvc.annotation.Service;
import com.xyf.mvc.service.XyfService;

@Service("ServiceImp") // 从注解里获得实例 放入map
public class ServiceImp implements XyfService {

	public String query(String text) {
		return "query ,result :" + text;
	}

}

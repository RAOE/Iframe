package com.xyf.mvc.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xyf.mvc.annotation.Autowired;
import com.xyf.mvc.annotation.RequestMapping;
import com.xyf.mvc.annotation.RequestParam;
import com.xyf.mvc.annotation.Service;
import com.xyf.mvc.annotation.xyfController;
import com.xyf.mvc.controller.testController;

/**
 * Servlet implementation class dispatcherServlet
 */
public class dispatcherServlet extends HttpServlet {

	List<String> classNames = new ArrayList<String>();
	Map<String, Object> map = new HashMap<String, Object>();
	Map<String, Object> handerMap = new HashMap<String, Object>();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public dispatcherServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) {
		// scan扫描 包
		doScan("com.xyf");// 扫描这个包下面的所有类
		doInstance();// 创建实例并保存
		doAutowired();// 射入
		doMapping();// 根据映射找到方法 --> 找到method

	}

	private void doScan(String basePackage) {

		URL url = this.getClass().getClassLoader().getResource(("/" + basePackage.replaceAll("\\.", "/")));// 转换为文件类型
		String str = url.getFile();
		File file = new File(str);

		String[] fileStr = file.list(); // 拿到当前包下面的所有 .class文件
		for (String path : fileStr) {
			File filePath = new File(str + path); //
			if (filePath.isDirectory()) {
				doScan(basePackage + "." + path);
			} else // java类
			{
				classNames.add(basePackage + "." + filePath.getName());
			}
		}

	}

	private void doInstance() {

		for (String className : classNames) {
			// 去掉class后缀名
			String cn = className.replace(".class", "");
			try {
				Class<?> clazz = Class.forName(cn);

				if (clazz.isAnnotationPresent(xyfController.class)) {
					Object instace = clazz.newInstance();
					// map.put instace
					RequestMapping reqMap = clazz.getAnnotation(RequestMapping.class);
					String key = reqMap.value();
					map.put(key, instace); // 放入map

				} else if (clazz.isAnnotationPresent(Service.class)) {
					Object instace = clazz.newInstance();
					// map.put instace
					Service reqMap = clazz.getAnnotation(Service.class); // 拿到service作为一个key
					String key = reqMap.value();
					map.put(key, instace); // 放入map
				} else {
					continue;
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void doAutowired() {

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object instance = entry.getValue();// 拿到对象
			Class<?> clazz = instance.getClass();

			if (clazz.isAnnotationPresent(xyfController.class)) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field fd : fields) {
					if (fd.isAnnotationPresent(Autowired.class)) {
						// 如果有autowired 则注入
						Autowired auto = fd.getAnnotation(Autowired.class);
						String key = auto.value();
						Object value = map.get(key);
						fd.setAccessible(true);// 打开私处
						try {
							fd.set(instance, value);// 射入
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}

					}
				}
			}

		}

	}

	private void doMapping() {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object instance = entry.getValue();// 拿到对象
			Class<?> clazz = instance.getClass();
			if (clazz.isAnnotationPresent(xyfController.class)) {
				RequestMapping reqMapping = clazz.getAnnotation(RequestMapping.class);
				String classPath = reqMapping.value(); //

				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					System.out.println(method);
					if (method.isAnnotationPresent(RequestMapping.class)) {
						RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
						String methodPath = requestMapping.value();
						handerMap.put(classPath + methodPath, method);

					} else {
						continue;

					}
				}

			}

		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取请求路径
		String uri = request.getRequestURI(); // projectName
		String context = request.getContextPath();
		String path = uri.replace(context, ""); // key
		Method method = (Method) handerMap.get(path); // method --
		if (!path.equals("/")) {
			testController instance = (testController) map.get("/" + path.split("/")[1]);
			Object args[] = han(request, response, method);

			try {
				method.invoke(instance, args);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private Object[] han(HttpServletRequest request, HttpServletResponse response, Method method) {

		Class<?>[] paramClazzs = method.getParameterTypes();
		Object[] args = new Object[paramClazzs.length];
		int args_i = 0;
		int index = 0;
		for (Class<?> paramClazz : paramClazzs) {
			if (ServletRequest.class.isAssignableFrom(paramClazz)) {
				args[args_i++] = request;
			}
			if (ServletResponse.class.isAssignableFrom(paramClazz)) {
				args[args_i++] = response;
			}
			Annotation[] paramAns = method.getParameterAnnotations()[index];
			if (paramAns.length > 0) {

				for (Annotation paramAn : paramAns) {
					if (RequestParam.class.isAssignableFrom(paramAn.getClass())) {
						RequestParam rp = (RequestParam) paramAn;
						args[args_i++] = request.getParameter(rp.value());
					}

				}

			}
			index++;
		}
		return args;
	}

}

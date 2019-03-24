#Iframe
模仿SpringMVC框架, 并实现轻量级的web框架，可用作小型网站的搭建，对servle进行封装。

<h1>目录介绍</h1>
<p>com.xyf.mvc.annotation 注解类 springmvc 的注解 如requestMapping requstParam 都在这里</p>
<p>com.xyf.mvc.controller 控制层 用法和springmvc相同</p>
<p>com.xyf.mvc.service  服务层</p>
<p>com.xyf.mvc.serviceimp  服务实现层</p>
<p>com.xyf.mvc.servlet    核心层，对servlet进行封装  初始化顺序依次为</br>
        <p>public void init(ServletConfig config) { </p>
		<p>// scan扫描 包                               </p>
		<p>doScan("com.xyf");// 扫描这个包下面的所有类</p>
		<p>doInstance();// 创建实例并保存</p>
		<p>doAutowired();// 射入</p>
		<p>doMapping();// 根据映射找到方法 --> 找到method</p>
            
<h1>如何使用</h1>
<p>参考 类com.xyf.mvc.controller.testController</p>


<h1>其他</h1>
<p>本项目模仿了springmvc框架源码，从中学到很多。项目会不定期更新，欢迎关注。</p>
<p>本项目更大的用处是学习，而不是要开发一个全新的web框架。</p>

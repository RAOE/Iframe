# MySpringMvCFrame
参考SpringMVC框架, 实现轻量级的web框架

<h1>目录介绍</h1>
<p>com.xyf.mvc.annotation 注解类 springmvc 的注解 如requestMapping requstParam 都在这里</p>
<p>com.xyf.mvc.controller 控制层 用法和springmvc相同</p>
<p>com.xyf.mvc.service  服务层</p>
<p>com.xyf.mvc.serviceimp  服务实现层</p>
<p>com.xyf.mvc.servlet    核心层，对servlet进行封装  初始化顺序依次为</br>
        doScan("com.xyf");// 扫描这个包下面的所有类</br>
		doInstance();// 创建实例并保存</br>
		doAutowired();// 射入</br>
		doMapping();// 根据映射找到方法 --> 找到method</br></p>
<h1>如何使用</h1>
<p>参考 testController</p>


<p>本项目参考了springmvc框架源码，从中学到很多。项目会不定期更新，欢迎关注。</p>
<p>本项目更大的用处是学习，而不是要开发一个全新的web框架。</p>

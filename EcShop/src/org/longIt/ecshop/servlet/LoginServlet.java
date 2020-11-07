package org.longIt.ecshop.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.longIt.ecshop.bean.Article;
import org.longIt.ecshop.bean.ArticleType;
import org.longIt.ecshop.bean.User;
import org.longIt.ecshop.service.EcShopService;
import org.longIt.ecshop.service.impl.EcShopServiceImpl;
import org.longIt.ecshop.service.proxy.ServiceProxy;
import org.longIt.ecshop.utils.ShopContant;

/**
 * 处理用户登录请求
 */
@WebServlet("/login.action")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//创建代理类
		ServiceProxy serviceProxy = new ServiceProxy();
		//创建服务层对象
		EcShopService service = serviceProxy.bind(new EcShopServiceImpl());
		
		//获取所有的一级物品类型
		List<ArticleType> firstTypes = service.findAllFArticleType();
		request.setAttribute("firstTypes", firstTypes);
		
		//获取标识符
		String flag = request.getParameter("flag");
		
		if("userLogin".equals(flag)){
			//处理用户登录
			String loginName = request.getParameter("loginName");
			String password = request.getParameter("password");
			//根据用户名以及密码获取用户信息
			User user = service.findUserByNameAndPass(loginName,password);
			
			//定义跳转地址
			String url = "/WEB-INF/jsp/login.jsp";
			if(user==null){
				//用户名或密码不正确
				request.setAttribute("message", "用户名或密码不正确!");
			}else if(user.getDisabled().equals("0")){
				//未激活
				request.setAttribute("message", "您尚未激活，请登录邮箱["+user.getEmail()+"]进行激活！");	
			
			}else if(user.getRole()==1){
				//买家
				request.getSession().setAttribute(ShopContant.SESSION_USER, user);
				url = "index.action";
			}else{
				//卖家
				request.getSession().setAttribute(ShopContant.SESSION_USER, user);
               //跳转至卖家的后台管理页面
				url = "/admin/jsp/main.jsp";
			}
			request.getRequestDispatcher(url).forward(request, response);			
		}else{
			
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
		}
		
		

	}

}

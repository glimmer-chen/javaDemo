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
import org.longIt.ecshop.bean.ShopCar;
import org.longIt.ecshop.bean.User;
import org.longIt.ecshop.service.EcShopService;
import org.longIt.ecshop.service.impl.EcShopServiceImpl;
import org.longIt.ecshop.service.proxy.ServiceProxy;
import org.longIt.ecshop.utils.ShopContant;

/**
 * 用户激活
 */
@WebServlet("/active.action")
public class ActiveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActiveServlet() {
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
		
	   //获取激活码
		String activeCode = request.getParameter("activeCode");
		try {
			service.activeUser(activeCode);
            request.setAttribute("message", "恭喜您激活成功！");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
            request.setAttribute("message", "激活失败："+e.getMessage());
		}
		
		request.getRequestDispatcher("login.action").forward(request, response);
		
	}

}

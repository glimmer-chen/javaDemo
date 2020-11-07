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
import org.longIt.ecshop.service.EcShopService;
import org.longIt.ecshop.service.impl.EcShopServiceImpl;
import org.longIt.ecshop.service.proxy.ServiceProxy;

/**
 * 展示商品详细信息
 */
@WebServlet("/item.action")
public class ItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ItemServlet() {
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
		
		
		
		//获取商品的id
		String id = request.getParameter("id");
        
		if(id==null||id.equals("")){
			//跳转至首页
			response.sendRedirect(request.getContextPath()+"/index.action");
		}else{
			  //根据商品的id获取商品信息
		    Article article = service.getArticleById(id);
		    request.setAttribute("article", article);
		
		    request.getRequestDispatcher("/WEB-INF/jsp/item.jsp").forward(request, response);
		   
		}

	  
	
	}

}

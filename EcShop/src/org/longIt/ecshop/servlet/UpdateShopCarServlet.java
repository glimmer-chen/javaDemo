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
 * 更新购物车中商品商品信息
 *  */
@WebServlet("/updateShopCar.do")
public class UpdateShopCarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateShopCarServlet() {
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
		
		
	   //获取需要更新的商品id
		String articleId = request.getParameter("articleId");
		User user = (User)request.getSession().getAttribute(ShopContant.SESSION_USER);
		String buyNum = request.getParameter("buyNum");
		
		if(articleId==null||articleId.equals("")||buyNum==null||buyNum.equals("")){
			response.sendRedirect(request.getContextPath()+"/index.action");
		}else{
			ShopCar shopCar = new ShopCar();
			shopCar.setArticleId(Integer.valueOf(articleId));
			shopCar.setUser(user);
			shopCar.setBuyNum(Integer.valueOf(buyNum));
			service.updateShop(shopCar);
		
			
			//展示购物车中商品信息
			response.sendRedirect(request.getContextPath()+"/showShopCar.do");
		}
		
		
	}

}

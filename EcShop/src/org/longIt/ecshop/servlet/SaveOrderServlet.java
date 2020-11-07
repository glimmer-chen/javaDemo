package org.longIt.ecshop.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.longIt.ecshop.bean.Article;
import org.longIt.ecshop.bean.ArticleType;
import org.longIt.ecshop.bean.Order;
import org.longIt.ecshop.bean.OrderItem;
import org.longIt.ecshop.bean.ShopCar;
import org.longIt.ecshop.bean.User;
import org.longIt.ecshop.service.EcShopService;
import org.longIt.ecshop.service.impl.EcShopServiceImpl;
import org.longIt.ecshop.service.proxy.ServiceProxy;
import org.longIt.ecshop.utils.ShopContant;

/**
 * 保存订单
 */
@WebServlet("/saveOrder.do")
public class SaveOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveOrderServlet() {
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
		
		User user = (User)request.getSession().getAttribute(ShopContant.SESSION_USER);
		
		//订单明细
	    //获取当前用户购物车中商品信息
	    List<ShopCar> shopCars = (List<ShopCar>)request.getSession().getAttribute("shopCars");
	    if(shopCars.size()>0){
	    	Double totalPrice = (Double)request.getSession().getAttribute("totalPrice");
			
			//创建订单对象
		    Order order = new Order();
		    //设置下单时间
		    order.setCreateDate(new Date());
		    
		    order.setUserId(user.getId());
		    
		    order.setAmount(totalPrice);
		    
		    //创建时间格式化工具
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		    
		    //生成订单编号  必须是唯一的  PO-20161214114012-1
		    StringBuffer orderNo = new StringBuffer();
		    orderNo.append("PO-").append(sdf.format(new Date())).append("-").append(user.getId());
		    order.setOrderCode(orderNo.toString());

		    List<OrderItem> items = new ArrayList<>();
		    
		    for(ShopCar shopCar : shopCars){
		    	OrderItem item = new OrderItem();
		    	item.setArticleId(shopCar.getArticle().getId());
		    	item.setOrderNum(shopCar.getBuyNum());
		    	items.add(item);
			}
		    //将订单明细存放在订单中
		    order.setItems(items);
		    //保存订单
		    service.saveOrder(order);
		    //跳转至展示订单信息的Servlet  
		    response.sendRedirect(request.getContextPath()+"/showOrderList.do");
	    }else{
	    	response.sendRedirect(request.getContextPath()+"/index.action");
	    }
	}

}

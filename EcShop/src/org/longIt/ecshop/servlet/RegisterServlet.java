package org.longIt.ecshop.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
 * 用户注册
 */
@WebServlet("/register.action")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//创建代理类
		ServiceProxy serviceProxy = new ServiceProxy();
		//创建服务层对象
		EcShopService service = serviceProxy.bind(new EcShopServiceImpl());
		
		//获取所有的一级物品类型
		List<ArticleType> firstTypes = service.findAllFArticleType();
		request.setAttribute("firstTypes", firstTypes);
		
		
		request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
	}
	
	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		    request.setCharacterEncoding("utf-8");
		    //创建代理类
			ServiceProxy serviceProxy = new ServiceProxy();
			//创建服务层对象
			EcShopService service = serviceProxy.bind(new EcShopServiceImpl());
	        String tip = "";
	        
            User user = new User();
	        //登录名
			String loginName = request.getParameter("loginName");
			user.setLoginName(loginName);
			//获取密码
			String passWord = request.getParameter("passWord");
			user.setPassword(passWord);
			//获取确认密码
			String okPassWord = request.getParameter("okPassWord");
		    if(passWord!=null&&!passWord.equals(okPassWord)){
		    	tip = "您输入的密码与确认密码不相符，请核实！";
		    }
		    //获取用户输入的验证码
		    String authcode = request.getParameter("authcode");
	        //从session中获取验证码
		    String randomData = (String)request.getSession().getAttribute(ShopContant.RANDOMDATA);
		    if(authcode!=null&&!authcode.equals(randomData)){
		    	tip = "您输入的验证码不正确，请核实！";
		    }

		    //获取真实姓名
		    String name = request.getParameter("name");
		    user.setName(name);
		    //获取性别
		    String sex = request.getParameter("sex");
		    user.setSex(Integer.valueOf(sex));
		    //获取地址
		    String address = request.getParameter("address");
		    user.setAddress(address);
		    //获取电话号码
		    String phone = request.getParameter("phone");
		    user.setPhone(phone);
		    //创建时间
		    user.setCreateDate(new Date());
		    //邮箱
		    user.setEmail(loginName);
		    String acticeCode = UUID.randomUUID().toString();
		    //设置激活码
		    user.setActive(acticeCode);
		    
		   
		    try {
				if(!tip.equals("")){
					request.setAttribute("message", tip);
					//获取所有的一级物品类型
					List<ArticleType> firstTypes = service.findAllFArticleType();
					request.setAttribute("firstTypes", firstTypes);
					
					request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, resp);;
				}else{
					 //保存用户信息
					service.save(user,request);
					request.setAttribute("message", "恭喜您，注册成功!请登录邮箱["+loginName+"]进行激活！");
					request.getRequestDispatcher("login.action").forward(request, resp);
				}
		    	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				request.setAttribute("message", "注册失败！");
			}
		
	}


}

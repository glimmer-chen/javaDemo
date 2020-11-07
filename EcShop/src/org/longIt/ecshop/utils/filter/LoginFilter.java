package org.longIt.ecshop.utils.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.longIt.ecshop.bean.User;
import org.longIt.ecshop.utils.ShopContant;

/**
 * 登录拦截器
 */
@WebFilter("*.do")   
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub

		//用户登录成功时，会将用户的信息存放在session中
		
		//在session中获取用户信息
		User user = (User)((javax.servlet.http.HttpServletRequest)request).getSession().getAttribute(ShopContant.SESSION_USER);
	    if(user==null){
	    	request.setAttribute("message", "您尚未登录，请登录再进行相关操作！");
	    	//跳转至登录页面
	    	request.getRequestDispatcher("/login.action").forward(request, response);
	    }else{
	    	//放行   
			chain.doFilter(request, response);
	    }
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

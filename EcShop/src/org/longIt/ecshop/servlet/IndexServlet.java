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
import org.longIt.ecshop.utils.webTag.PageModel;

/**
 * 展示商品首页信息
 */
@WebServlet("/index.action")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
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
		
		//设置编码,该方式只对post请求有效，对get请求无效
		request.setCharacterEncoding("utf-8");
		
		//由于用户刚进入首页的时候并没有选择物品类型   从firstTypes中第一个作为查询条件   typeCode不为空说明用户已经选择了物品类型
		String typeCode = request.getParameter("typeCode");
		
		//获取用户输入的查询关键字
		String keyword = request.getParameter("keyword");
		//将用户输入的查询关键字以及选择的物品类型存起来
		request.setAttribute("typeCode", typeCode);
		request.setAttribute("keyword", keyword);
		if(firstTypes.size()>0&&(typeCode==null||typeCode.equals(""))){
			typeCode = firstTypes.get(0).getCode();
		}
		
		//获取二级类型商品信息   必须截取用户选择typeCode的前四位
		String parentCode = typeCode.substring(0,4);
		List<ArticleType> seTypes = service.findSeArticleTypes(parentCode);
		request.setAttribute("seTypes", seTypes);
		
		
		//获取页码值
		String pageIndex = request.getParameter("pageIndex");
		
		
		//创建分页实体，进行分页查询
		PageModel pageModel = new PageModel();
		if(pageIndex!=null&&!pageIndex.equals("")){
			pageModel.setPageIndex(Integer.valueOf(pageIndex));
		}
		
		

		
		//根据物品类型以及用户输入的关键字获取物品信息
		List<Article> articles = service.findArticlesByCode(typeCode,keyword==null?"%%":"%"+keyword+"%",pageModel);
		
		request.setAttribute("articles", articles);
		//将pageModel存放在request中
		request.setAttribute("pageModel", pageModel);
		//根据用户选择的物品类型获取物品类型名字
		String typeName = service.findArticleTypeNameByCode(typeCode);
        request.setAttribute("typeName", typeName);
		//跳转至首页jsp页面    请求链不会断开
		request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
	
	}

}

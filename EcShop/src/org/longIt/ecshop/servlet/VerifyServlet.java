package org.longIt.ecshop.servlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
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
 * 生成验证码
 */
@WebServlet("/verify.action")
public class VerifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//生成验证码中的随机数
		String randomData = createRandomData();
		request.getSession().setAttribute(ShopContant.RANDOMDATA, randomData);
		
		//生成画布
		BufferedImage image = new BufferedImage(70,25,BufferedImage.TYPE_INT_RGB);
		//生成2d画笔
		Graphics2D d2 = image.createGraphics();
		//设置画笔的颜色
		d2.setColor(Color.white);
		//给画布上颜色
		d2.fillRect(0, 0, 70, 25);
		
		d2.setStroke(new BasicStroke(2.0f));
		
		Random random = new Random();
		//画干扰线
		for(int i=0;i<200;i++){
			int r = random.nextInt(255);
			int g = random.nextInt(255);
			int b = random.nextInt(255);
			
			int x = random.nextInt(70);
			int y = random.nextInt(25);
			
			Color c = new Color(r, g, b);
			//再次设置画笔的颜色
			d2.setColor(c);
			
			//画干扰线
			d2.drawLine(x, y, x, y);
		}
		
		//再次设置画笔的颜色
		d2.setColor(Color.red);
		d2.setFont(new Font("宋体",Font.BOLD,24));
		//将生成的随机数放在画布上
		d2.drawString(randomData, 10, 25);
		
		//设置数据的响应类型
		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();
		//将图片响应至客户端（浏览器）
		ImageIO.write(image, "png",out);
		
		out.flush();
		out.close();
	}

	private String createRandomData() {
		// TODO Auto-generated method stub
		StringBuffer sbf = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<4;i++){
			sbf.append(random.nextInt(10));
		}
		return sbf.toString();
	}

}

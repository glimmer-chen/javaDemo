package org.longIt.ecshop.service.impl;

import com.sun.mail.smtp.SMTPMessage;
import org.longIt.ecshop.annotation.AutoMapper;
import org.longIt.ecshop.bean.*;
import org.longIt.ecshop.exception.ShopException;
import org.longIt.ecshop.mapper.*;
import org.longIt.ecshop.service.EcShopService;
import org.longIt.ecshop.utils.webTag.PageModel;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/**
 * @author CHUNLONG.LUO
 * @version 1.0
 */
public class EcShopServiceImpl implements EcShopService {

	@AutoMapper
	private ArticleTypeMapper articleTypeMapper;
	
	@AutoMapper
	private ArticleMapper articleMapper;
	
	@AutoMapper
	private UserMapper userMapper;
	
	@AutoMapper
	private ShopCarMapper shopCarMapper;
	
	@AutoMapper
	private OrderMapper orderMapper;
	
	@AutoMapper
	private OrderItemMapper orderItemMapper;
	
	
	
	//获取所有的一级物品类型
	@Override
	public List<ArticleType> findAllFArticleType() {
		// TODO Auto-generated method stub
		
			//获取一级物品类型
			List<ArticleType> articleTypes = articleTypeMapper.findAllFArticleType();
	
	
		
		return articleTypes;
	}

	/* 
	 * 根据物品类型获取物品信息
	 */
	@Override
	public List<Article> findArticlesByCode(String typeCode,String keyword,PageModel pageModel) {
		// TODO Auto-generated method stub
		//获取总记录数
		int totalNum = articleMapper.findTotalNum(typeCode+"%",keyword);
		pageModel.setTotalNum(totalNum);
		
		//获取一级物品类型
		List<Article> articles = articleMapper.findArticlesByCode(typeCode+"%",keyword,pageModel);
		
		return articles;
	}

	/* (non-Javadoc)
	 * 获取二级物品类型
	 */
	@Override
	public List<ArticleType> findSeArticleTypes(String typeCode) {
		// TODO Auto-generated method stub
		
		//获取二级物品类型
		List<ArticleType> articleTypes = articleTypeMapper.findSeArticleTypes(typeCode+"%");

		return articleTypes;
	}

	/* (non-Javadoc)
	 * 根据用户选择的物品类型获取物品类型名字
	 */
	@Override
	public String findArticleTypeNameByCode(String typeCode) {
		// TODO Auto-generated method stub
	
		String typeName = articleTypeMapper.findArticleTypeNameByCode(typeCode);
	
		return typeName;
	}

	/* 
     *根据商品的id获取商品信息
	 */
	@Override
	public Article getArticleById(String id) {
		// TODO Auto-generated method stub

		Article article = articleMapper.getArticleById(id);
	
		return article;
	}

	/* 
	 * 根据用户名以及密码获取用户信息
	 */
	@Override
	public User findUserByNameAndPass(String loginName, String password) {
		// TODO Auto-generated method stub
		
		User user = userMapper.findUserByNameAndPass(loginName,password);
		
		return user;
	}
	

	/* (non-Javadoc)
	 * 加入商品至购物车
	 */
	@Override
	public void addShop(String shopId, int userId, String buyNum) {
		// TODO Auto-generated method stub
		
		shopCarMapper.addShop(shopId,userId,buyNum);
		
	}

	/* (non-Javadoc)
	 * 根据用户id以及商品id获取购物车中的当前用户购物车的商品信息
	 */
	@Override
	public ShopCar getShopCarByArticleIdAndUserId(String shopId, int userId) {
		// TODO Auto-generated method stub
		
		ShopCar shopCar = shopCarMapper.getShopCarByArticleIdAndUserId(shopId,userId);
		
		return shopCar;
	}

	/* (non-Javadoc)
	 * 更新购物车中商品信息
	 */
	@Override
	public void updateShop(ShopCar shopCar) {
		// TODO Auto-generated method stub
		
		shopCarMapper.updateShop(shopCar);
	
	}

	/* (non-Javadoc)
	 *  根据用户id获取购物车商品信息
	 */
	@Override
	public List<ShopCar> getShopCarByUserId(User user) {
		// TODO Auto-generated method stub
		
		List<ShopCar> shopCars = shopCarMapper.getShopCarByUserId(user);
	
		return shopCars;
	}

	/* (non-Javadoc)
	 *  删除购物车中商品信息
	 */
	@Override
	public void deleteShopCar(String articleId, int userId) {
		// TODO Auto-generated method stub
		
	
		shopCarMapper.deleteShopCar(articleId,userId);
		
	}

	/* (non-Javadoc)
	 * 根据登录名获取用户信息
	 */
	@Override
	public User getUserByLoginName(String loginName) {
		// TODO Auto-generated method stub
		
		return userMapper.getUserByLoginName(loginName);
	}

	/* (non-Javadoc)
	 * 保存用户信息
	 */
	@Override
	public void save(User user,HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
		userMapper.save(user);
		//发送邮件
		
		//创建Properties对象，用来封装邮件服务器相关信息
		Properties props = new Properties();
		//设置邮件服务器的地址
		props.setProperty("mail.smtp.host", "smtp.126.com");
		//邮件服务器需要权限，指定用户必须登录邮件服务器才能发送邮件
		props.setProperty("mail.smtp.auth", "true");
		
		//创建Authenticator的实例，实现账户、密码的鉴权。
        Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("luochunlong666@126.com", "luochunlong123");
            }
        };
	
			//通过Session与服务器建立连接
			Session session = Session.getInstance(props,auth);
			
			//创建发送邮件对象，该对象主要用于封装邮件相关信息，比如  主题  发件人  邮件内容等
			SMTPMessage message = new SMTPMessage(session);
			
			//设置邮件的主题
			message.setSubject("用户注册激活邮件，请勿回复，按照指引激活");
			//设置邮件的内容
			message.setContent("<a href='http://127.0.0.1:8080/"+request.getContextPath()+"/active.action?activeCode="+user.getActive()+"' target='_blank'>恭喜您注册成功，请点击该链接进行激活，无需回复！</a>", "text/html;charset=utf-8");
			
			//设置发件人
			message.setFrom(new InternetAddress("luochunlong666@126.com"));
			
			//设置收件人   接收者类型由：TO(收件人)、CC(抄送)、BCC(密送)
			message.setRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
			
			//发送邮件
			Transport.send(message);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShopException(e);
		}
		
	
		
	}

	/* (non-Javadoc)
	 * 用户激活
	 */
	@Override
	public void activeUser(String activeCode) {
		// TODO Auto-generated method stub
		User user = userMapper.getUserByCode(activeCode);
		if(user==null){
			throw new ShopException("激活码已失效，本次激活无效！");
		}else{
			userMapper.activeUser(activeCode);
		}
	}

	/* (non-Javadoc)
	 * 保存订单
	 */
	@Override
	public void saveOrder(Order order) {
		// TODO Auto-generated method stub

		//先保存订单再保存订单明细，订单以及订单明细要么同时保存成功，要么都失败
		orderMapper.saveOrder(order);
		int orderId = order.getId();
		System.out.println("order.Id====2===:"+order.getId());
		for(int i=0;i<order.getItems().size();i++){
			order.getItems().get(i).setOrderId(orderId);
			//保存订单明细
			orderItemMapper.saveOrderItem(order.getItems().get(i));
		}
		
		//清空当前用户的购物车
		shopCarMapper.removeShopCarByUserId(order.getUserId());
		
		
	}

	/* (non-Javadoc)
	 * 	根据用户id获取用户订单信息
	 */
	@Override
	public List<Order> getAllOrderByUserId(int userId) {
		// TODO Auto-generated method stub
		
		return orderMapper.getAllOrderByUserId(userId);
	}
}




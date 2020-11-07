package org.longIt.ecshop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.longIt.ecshop.bean.Article;
import org.longIt.ecshop.bean.ArticleType;
import org.longIt.ecshop.bean.Order;
import org.longIt.ecshop.bean.ShopCar;
import org.longIt.ecshop.bean.User;
import org.longIt.ecshop.utils.webTag.PageModel;

/**
 * @author CHUNLONG.LUO
 * @version 1.0
 */
public interface EcShopService {

	//获取所有的一级物品类型
	List<ArticleType> findAllFArticleType();

	//根据物品类型获取物品信息
	List<Article> findArticlesByCode(String typeCode,String keyword,PageModel pageModel);

	//获取二级物品类型
	List<ArticleType> findSeArticleTypes(String typeCode);

	//根据用户选择的物品类型获取物品类型名字
	String findArticleTypeNameByCode(String typeCode);

	//根据商品的id获取商品信息
	Article getArticleById(String id);

	//根据用户名以及密码获取用户信息
	User findUserByNameAndPass(String loginName,String password);

	//根据用户id以及商品id获取购物车中的当前用户购物车的商品信息
	ShopCar getShopCarByArticleIdAndUserId(String shopId, int id);
	
	//加入商品至购物车
	void addShop(String shopId, int id, String buyNum);

	//更新购物车中商品信息
	void updateShop(ShopCar shopCar);

	//根据用户id获取购物车商品信息
	List<ShopCar> getShopCarByUserId(User user);

	//删除购物车中商品信息
	void deleteShopCar(String articleId, int id);

	//根据登录名获取用户信息
	User getUserByLoginName(String loginName);

	//保存用户信息
	void save(User user,HttpServletRequest request);

	//用户激活
	void activeUser(String activeCode);

	//保存订单
	void saveOrder(Order order);

	//根据用户id获取用户订单信息
	List<Order> getAllOrderByUserId(int id);

}

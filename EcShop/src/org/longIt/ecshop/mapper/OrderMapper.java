package org.longIt.ecshop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.longIt.ecshop.bean.Order;

/**
 * OrderMapper 数据访问类
 * @author CHUNLONG.LUO
 * @version 1.0
 */
public interface OrderMapper {

	//保存订单
	void saveOrder(Order order);

	//根据用户id获取用户订单信息
	List<Order> getAllOrderByUserId(int userId);



}
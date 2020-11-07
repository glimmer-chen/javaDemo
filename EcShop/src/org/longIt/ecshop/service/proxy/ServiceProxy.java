package org.longIt.ecshop.service.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.ibatis.session.SqlSession;
import org.longIt.ecshop.annotation.AutoMapper;
import org.longIt.ecshop.exception.ShopException;
import org.longIt.ecshop.service.EcShopService;
import org.longIt.ecshop.service.impl.EcShopServiceImpl;
import org.longIt.ecshop.utils.ConnectionFactory;

/**
 * @author CHUNLONG.LUO
 * @version 1.0
 */
public class ServiceProxy {

	@SuppressWarnings("unchecked")
	public <T> T bind(T obj) {
		// TODO Auto-generated method stub
		
		return (T)Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				// TODO Auto-generated method stub
				SqlSession sqlSession = null;
				try {
					 sqlSession = ConnectionFactory.getSqlSession();
					
					//获取被代理对象的class类型
					Class<?> clazz = obj.getClass();
					//获取被代理对象中所有的属性,私有的以及非私有的属性
					Field[] fields = clazz.getDeclaredFields(); 
					
					for(Field field : fields){
						
						//判断属性上是否有注解@AutoMapper，有则注入值，否则无需注入
						AutoMapper autoMapper = field.getAnnotation(AutoMapper.class);
						//如果有注解，并且为属性required的值为true，才注入
						if(autoMapper!=null&&autoMapper.required()){
							//判断field是否私有的
							if(!field.isAccessible()){
								//将私有的属性设置成可访问状态
								field.setAccessible(true);
							}
							//给属性注入值   UserMapper userMapper = sqlSession.getMapper(UserMapper.class);  
                             System.out.println("field.getType():"+field.getType()+"   field.getClass():"+field.getClass());
							field.set(obj, sqlSession.getMapper(field.getType())); 
						}
						
					}					
					//回调目标方法
					Object result = method.invoke(obj, args);
					
					System.out.println("================关闭连接======================");
					return result;
				
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					//回滚
					sqlSession.rollback();
					throw new ShopException("操作失败:"+e.getMessage(),e);
				}finally {
					//提交事务
					sqlSession.commit();
					//关闭连接
					ConnectionFactory.closeSqlSession();
				}
			
			}
		});
	}

}

package org.longIt.ecshop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.longIt.ecshop.bean.Article;
import org.longIt.ecshop.utils.webTag.PageModel;

/**
 * ArticleMapper 数据访问类
 * @author CHUNLONG.LUO
 * @version 1.0
 */
public interface ArticleMapper {

	//根据物品类型获取物品信息  
	@Select("SELECT * FROM ec_article WHERE type_code LIKE #{typeCode} and title like #{keyword}  limit #{pageModel.startNum},#{pageModel.pageSize}")
	List<Article> findArticlesByCode(@Param("typeCode")String typeCode,@Param("keyword")String keyword,@Param("pageModel")PageModel pageModel);

	
	//根据商品的id获取商品信息
	@Select("SELECT * FROM ec_article WHERE id = #{id}")
	Article getArticleById(String id);


	//获取总记录数
	@Select("SELECT count(*) FROM ec_article WHERE type_code LIKE #{typeCode} and title like #{keyword}")
	int findTotalNum(@Param("typeCode")String typeCode,@Param("keyword")String keyword);



}
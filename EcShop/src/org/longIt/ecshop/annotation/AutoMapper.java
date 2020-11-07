package org.longIt.ecshop.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author CHUNLONG.LUO
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoMapper {
	
	public boolean required() default true; 

}

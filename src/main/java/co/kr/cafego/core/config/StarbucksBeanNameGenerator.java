/*
 * @(#) $Id: StarbucksBeanNameGenerator.java,v 1.1 2017/09/07 04:14:04 namgu1 Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2016 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, Digitalro 306, Guro-gu,
 * Seoul, Korea
 */

/**
 * 
 */
package co.kr.cafego.core.config;

import java.beans.Introspector;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * TODO Insert type comment for StarbucksBeanNameGenerator.
 *
 * @author Soonwoo
 * @version $Revision: 1.1 $
 */
public class StarbucksBeanNameGenerator implements BeanNameGenerator {
	
	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		String beanClassName = Introspector.decapitalize(definition.getBeanClassName());
		
		return beanClassName;
	}
}

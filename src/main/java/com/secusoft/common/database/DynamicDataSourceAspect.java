package com.secusoft.common.database;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DynamicDataSourceAspect {
	
    /**
     * Dao aspect.
     */
    @Pointcut("execution( * com.secusoft.mapper.*.*(..))")
    public void daoAspect() {
    }

    /**
     * Switch DataSource
     *
     * @param point the point
     */
    @Before("daoAspect()")
    public void switchDataSource(JoinPoint point) {
    	DynamicDataSourceContextHolder.setDataSourceKey(point.getArgs()[0].toString());
    	System.out.println("Switch DataSource to "+DynamicDataSourceContextHolder.getDataSourceKey()+" in Method "+point.getSignature());
    }

    /**
     * Restore DataSource
     *
     * @param point the point
     */
    @After("daoAspect())")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        System.out.println("Restore DataSource to "+DynamicDataSourceContextHolder.getDataSourceKey()+" in Method "+point.getSignature());
    }


}

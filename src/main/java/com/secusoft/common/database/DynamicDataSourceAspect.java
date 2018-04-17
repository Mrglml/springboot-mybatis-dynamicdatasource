package com.secusoft.common.database;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 多数据源动态切换
 * @author yaojiacheng
 * 2018年4月12日
 */
@Aspect
@Component
public class DynamicDataSourceAspect implements Ordered {
	
	private final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
	
    /**
     * 由于多数据源的aop的切点是service，所以事务只能在service层以下启用(包括service，比如service层和mapper层)
     * 不能在controller层启动事务，否则会导致事务的aop优于多数据源的aop而加载了错误的数据源
     */
	@Pointcut("execution( * com.secusoft.service.*.*(..))")
//    @Pointcut("execution( * com.secusoft.mapper.*.*(..))")
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
    	logger.info("Switch DataSource to [{}] in Method {}", DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }

    /**
     * Restore DataSource
     *
     * @param point the point
     */
    @After("daoAspect())")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        logger.info("Restore DataSource in Method {}", point.getSignature());
    }

    /**
     * 数据源的切换优先级要高于spring的声明式事务，不然会引起事务操作的数据源不是正确的数据源
     * 所以该aop的优先级顺序要早于spring事务的aop，该aop顺序设为1，spring事务的aop设为2
     */
	@Override
	public int getOrder() {
		return 1;
	}

}

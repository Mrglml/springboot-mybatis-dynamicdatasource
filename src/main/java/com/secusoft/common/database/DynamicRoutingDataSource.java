package com.secusoft.common.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Set dynamic DataSource to Application Context
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
    	logger.info("Thread [{}] Current DataSource is [{}]", Thread.currentThread().getName(), DynamicDataSourceContextHolder.getDataSourceKey());
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
}

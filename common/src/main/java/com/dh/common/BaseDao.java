/**
 * 友魄科技
 */
package com.dh.common;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haibin_chen 2015/4/6
 *
 */
public abstract class BaseDao extends SqlSessionDaoSupport {
	
	@Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }
	
	protected <T> T getMapper(Class<T> clazz) {
        return getSqlSession().getMapper(clazz);
    }
}

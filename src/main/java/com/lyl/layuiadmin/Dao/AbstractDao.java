package com.lyl.layuiadmin.Dao;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public abstract class AbstractDao {

	abstract protected EntityManager getEntityManager();

	@SuppressWarnings("rawtypes")
	protected DetachedCriteria dcForClass(Class clazz) {
		return DetachedCriteria.forClass(clazz);
	}

	/**
	 * 对于那些需要自行设定projection投影的 不应该调此方法，否则projection会被dc.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)破坏
	 */
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria dc) {
		dc.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//去除重复，对于那些需要自行设定projection投影的 不应该调研此方法
		Criteria executableCriteria = getExecutableCriteria(dc);

		return executableCriteria.list();
	}

	/**
	 * 通过enableFlag 过滤掉逻辑删除的数据
	 */
	@SuppressWarnings("rawtypes")
	public List findByCriteriaWithEnableFlag(DetachedCriteria dc) {
		dc.add(Restrictions.eq("enableFlag", true));
		return findByCriteria(dc);
	}

	public Criteria getExecutableCriteria(DetachedCriteria dc) {
		Criteria executableCriteria = dc.getExecutableCriteria(getEntityManager().unwrap(Session.class));
		return executableCriteria;
	}

	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria dc, int firstResult, int maxResults) {
		dc.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//去除重复，对于那些需要自行设定projection投影的 不应该调研此方法
		Criteria executableCriteria = getExecutableCriteria(dc);
		if (firstResult >= 0) {
			executableCriteria.setFirstResult(firstResult);
		}
		if (maxResults > 0) {
			executableCriteria.setMaxResults(maxResults);
		}
		return executableCriteria.list();
	}

	/**
	 * 通过enableFlag 过滤掉逻辑删除的数据
	 */
	@SuppressWarnings("rawtypes")
	public List findByCriteriaWithEnableFlag(DetachedCriteria dc, int firstResult, int maxResults) {
		dc.add(Restrictions.eq("enableFlag", true));
		return findByCriteria(dc, firstResult, maxResults);
	}

	@SuppressWarnings("rawtypes")
	public int countRecordsByCriteria(DetachedCriteria dc, String countDistinctProjections) {
		dc.setProjection(Projections.countDistinct(countDistinctProjections));

		Criteria executableCriteria = getExecutableCriteria(dc);

		List list = executableCriteria.list();
		int result = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			Integer item = Integer.parseInt(it.next() + "");
			result += item;
		}
		dc.setProjection(null);// 避免对dc.setProjection影响到其它地方
		return result;
	}

	public int countRecordsByCriteriaWithEnableFlag(DetachedCriteria dc, String countDistinctProjections) {
		dc.add(Restrictions.eq("enableFlag", true));
		return countRecordsByCriteria(dc, countDistinctProjections);
	}
}
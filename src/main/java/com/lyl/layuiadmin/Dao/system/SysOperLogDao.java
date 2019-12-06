package com.lyl.layuiadmin.Dao.system;

import com.lyl.layuiadmin.Dao.AbstractDao;
import com.lyl.layuiadmin.Dao.repo.system.SysOperLogRepo;
import com.lyl.layuiadmin.pojo.system.SysOperLogDO;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class SysOperLogDao extends AbstractDao {

	@PersistenceContext
	protected EntityManager	em;

	@Autowired
	SysOperLogRepo sysOperLogRepo;

	@Autowired
	JdbcTemplate			jdbcTemplate;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public SysOperLogDO findById(String id) {
		SysOperLogDO operLog = sysOperLogRepo.getOne(id);
		return operLog;
	}

	public List<SysOperLogDO> findAll() {
		return sysOperLogRepo.findAll();
	}

	@SuppressWarnings("unchecked")
	public List<SysOperLogDO> findAllEnableFlag() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysOperLogDO.class);
		return super.findByCriteriaWithEnableFlag(dc);
	}

	public SysOperLogDO saveOrUpdate(SysOperLogDO operLog) {
		return sysOperLogRepo.save(operLog);
	}

	/**
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	public SysOperLogDO logicDeleted(String id) {
		SysOperLogDO operLog = this.findById(id);
		operLog.setEnableFlag(false);
		return this.saveOrUpdate(operLog);
	}

}

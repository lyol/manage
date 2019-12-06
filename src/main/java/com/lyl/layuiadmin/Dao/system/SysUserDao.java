package com.lyl.layuiadmin.Dao.system;

import com.lyl.layuiadmin.Dao.AbstractDao;
import com.lyl.layuiadmin.Dao.repo.system.SysUserRepo;
import com.lyl.layuiadmin.util.DateUtils;
import com.lyl.layuiadmin.pojo.system.SysUserDO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SysUserDao extends AbstractDao {

	@PersistenceContext
	protected EntityManager	em;

	@Autowired
	SysUserRepo sysUserRepo;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public SysUserDO findSysUserById(String id) {
		SysUserDO cityCode = sysUserRepo.getOne(id);
		return cityCode;
	}

	public List<SysUserDO> findAll() {
		return sysUserRepo.findAll();
	}


	public void saveOrUpdate(SysUserDO sysUser) {
		sysUserRepo.save(sysUser);
	}

	
	@SuppressWarnings("unchecked")
	public SysUserDO findSysUserByUserName(String userName) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysUserDO.class);
		dc.add(Restrictions.eq("userName", userName));
		List<SysUserDO> list = super.findByCriteriaWithEnableFlag(dc);
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * 登录密码错误，记录错误次数
	 * @param sysUser
	 */
	public void updateLoginErrorCount(SysUserDO sysUser) {
		if (sysUser != null) {
			if (sysUser.getLastLoginErrorDay() == null || DateUtils.countDaysBetweenTwoDate(sysUser.getLastLoginErrorDay(), new Date()) == 0) {
				sysUser.setLoginErrorCount(sysUser.getLoginErrorCount() != null ? sysUser.getLoginErrorCount() + 1 : 1);
			} else {
				sysUser.setLoginErrorCount(1);
			}
			sysUser.setLastLoginErrorDay(DateUtils.localDate2Date(LocalDate.now()));
			this.saveOrUpdate(sysUser);
		}
	}


}

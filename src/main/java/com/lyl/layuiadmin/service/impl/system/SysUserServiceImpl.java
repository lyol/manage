package com.lyl.layuiadmin.service.impl.system;

import com.lyl.layuiadmin.Dao.system.SysUserDao;
import com.lyl.layuiadmin.pojo.system.SysUserDO;
import com.lyl.layuiadmin.service.system.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	SysUserDao sysUserDao;

	@Override
	public SysUserDO findById(String id) {
		SysUserDO sysUser = sysUserDao.findSysUserById(id);
		return sysUser;
	}

	@Override
	public List<SysUserDO> findAll() {
		return sysUserDao.findAll();
	}

	@Override
	public void savaOrUpdate(SysUserDO sysUser) {
		sysUserDao.saveOrUpdate(sysUser);
	}

}

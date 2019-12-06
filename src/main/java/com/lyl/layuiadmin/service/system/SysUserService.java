package com.lyl.layuiadmin.service.system;

import com.lyl.layuiadmin.pojo.system.SysUserDO;

import java.util.List;

public interface SysUserService {

	public SysUserDO findById(String id);

	public List<SysUserDO> findAll();

	public void savaOrUpdate(SysUserDO cityCode);
}

package com.lyl.layuiadmin.service.system;

import com.lyl.layuiadmin.common.Result;
import com.lyl.layuiadmin.pojo.system.SysOperLogDO;

import java.util.List;

public interface SysOperLogService {

	List<SysOperLogDO> list();

	Result save(SysOperLogDO menuDO);

	SysOperLogDO get(String id);

	void insertOperLog(String userid, String loginName, String ipAddr, String opertype, String memo);
}

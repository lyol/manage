package com.lyl.layuiadmin.service.impl.system;

import com.lyl.layuiadmin.Dao.system.SysOperLogDao;
import com.lyl.layuiadmin.common.Result;
import com.lyl.layuiadmin.enums.ResultEnum;
import com.lyl.layuiadmin.pojo.system.SysOperLogDO;
import com.lyl.layuiadmin.service.system.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOperLogServiceImpl implements SysOperLogService {
	@Autowired
	private SysOperLogDao sysOperLogDao;

	@Override
	public List<SysOperLogDO> list() {
		List<SysOperLogDO> list = sysOperLogDao.findAllEnableFlag();
		return list;
	}

	@Override
	public Result save(SysOperLogDO operLog) {
		SysOperLogDO oper = sysOperLogDao.saveOrUpdate(operLog);
		if (oper != null) {
			return Result.ok();
		}
		return Result.build(ResultEnum.FAIL.getCode(), "操作日志失败！");
	}

	@Override
	public SysOperLogDO get(String id) {
		SysOperLogDO operLog = sysOperLogDao.findById(id);
		return operLog;
	}

	@Override
	public void insertOperLog(String userid, String loginName, String ipAddr, String opertype, String memo) {
		SysOperLogDO operLog = new SysOperLogDO();
		operLog.setUserId(userid);
		operLog.setLoginName(loginName);
		operLog.setIpAddr(ipAddr);
		operLog.setOperType(opertype);
		operLog.setMemo(memo);
		sysOperLogDao.saveOrUpdate(operLog);
	}
}

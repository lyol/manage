package com.lyl.layuiadmin.config;

import com.lyl.layuiadmin.util.CheckUtil;
import com.lyl.layuiadmin.common.Result;
import com.lyl.layuiadmin.pojo.system.SysUserDO;
import com.lyl.layuiadmin.service.impl.system.SysOperLogServiceImpl;
import com.lyl.layuiadmin.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 	处理统一返回、日志切面、参数校验
 */
@Slf4j
@Aspect
@Component
public class ControllerAOP {

	@Autowired
	private SysOperLogServiceImpl sysOperLogServiceImpl;

	/**
	 * 	创建返回切点,这里只切返回Result的方法，没有切返回String和void的方法。
	 * */
	@Pointcut("execution(public com.lyl.layuiadmin.common.Result *(..))")
	public void resultPtCut() {}

	/**
	 * 	创建日志切点
	 * */
	@Pointcut("@annotation(com.lyl.layuiadmin.config.BizOperLog)")
	public void operLogCut() {}

	/**
	 * 	环绕通知处理
	 * */
	@Around("resultPtCut()")
	public Object handlerControllerMethods(ProceedingJoinPoint pjp) throws Throwable{
		long startTime = System.currentTimeMillis();
		log.debug(pjp.getSignature()+"请求参数:{}",pjp.getArgs());
		Result result;//业务返回结果

		CheckUtil.checkModel(pjp);//校验model上面的参数

		result = (Result) pjp.proceed();//业务处理

		log.debug("返回结果:{}",result.toString());
		log.debug(pjp.getSignature() + "处理耗费时间:" + (System.currentTimeMillis() - startTime)+"ms");
		return result;
	}

	/**
	 *	 应用日志存储
	 * */
	@After("operLogCut() && @annotation(bizOperLog)")
	public void logAdvisor(BizOperLog bizOperLog){
		log.info("进入操作日志切面");
		// 添加记录日志
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		SysUserDO user = (SysUserDO)SecurityUtils.getSubject().getPrincipal();
		String userid = user.getId();// 操作员id
		String loginName = user.getUserName();
		String ipAddr = RequestUtils.getClientIp(request);// 访问段ip

		//从注解中获取操作类型和备注
		String opertype =  bizOperLog.operType().getValue();
		String memo = bizOperLog.memo();
		sysOperLogServiceImpl.insertOperLog(userid,loginName,ipAddr,opertype,memo);
		log.info("记录操作日志成功");
	}

}

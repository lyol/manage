package com.lyl.layuiadmin.pojo.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lyl.layuiadmin.pojo.BaseHibernateModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = SysOperLogDO.TABLE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
public class SysOperLogDO extends BaseHibernateModel {

	private static final long	serialVersionUID	= 1L;
	
	public static final String	TABLE				= "SYS_OPER_LOG";
	
	/**用户Id*/
    private String userId;
    /**登录名*/
    private String loginName;
    /**ip地址*/
    private String ipAddr;
    /**操作类型*/
    private String operType;
    /**描述*/
    private String memo;
}

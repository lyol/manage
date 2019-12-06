package com.lyl.layuiadmin.pojo.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lyl.layuiadmin.pojo.BaseHibernateModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = SysUserDO.TABLE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Getter
@Setter
public class SysUserDO extends BaseHibernateModel {
	private static final long	serialVersionUID	= 1L;
	public static final String	TABLE				= "SYS_USER";

	private String				userName;

	private String				nickName;

	private String				password;
	
	private String				email;

	private String				telephone;

	private String				roleId;

	private Integer				loginErrorCount;

	private Date				lastLoginErrorDay;
}

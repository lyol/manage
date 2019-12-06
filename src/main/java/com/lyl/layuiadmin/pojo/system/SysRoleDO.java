package com.lyl.layuiadmin.pojo.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lyl.layuiadmin.pojo.BaseHibernateModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = SysRoleDO.TABLE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Getter
@Setter
public class SysRoleDO extends BaseHibernateModel {

	private static final long	serialVersionUID	= 1L;
	public static final String	TABLE				= "SYS_ROLE";

	private String				roleName;

	private String				roleCode;

	private String				remark;

	private Boolean				status;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_role_menu", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "menu_id") })
	@Fetch(FetchMode.SUBSELECT)
	private List<SysMenuDO> menuList = new ArrayList<SysMenuDO>();
}
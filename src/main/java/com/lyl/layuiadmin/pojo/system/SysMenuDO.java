package com.lyl.layuiadmin.pojo.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lyl.layuiadmin.pojo.BaseHibernateModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = SysMenuDO.TABLE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Getter
@Setter
public class SysMenuDO extends BaseHibernateModel {

	private static final long	serialVersionUID	= 1L;
	public static final String	TABLE				= "SYS_MENU";

	private String				pid;

	private String				title;

	private Integer				type;

	private String				font;

	private String				icon;

	private String				url;

	private String				perms;

	private Integer				sort;

	private String				param;

	private Boolean				spread;

	private Boolean				children;

	private Boolean				status;

	private String				remark;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "menuList")
	@Fetch(FetchMode.SUBSELECT)
	private List<SysRoleDO>	roleList		= new ArrayList<SysRoleDO>();
}
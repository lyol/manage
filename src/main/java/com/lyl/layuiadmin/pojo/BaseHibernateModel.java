package com.lyl.layuiadmin.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * Hibernate通用entity父类，统一主键策略
 */
@MappedSuperclass
public class BaseHibernateModel extends BaseHibernateModelNoId implements Cloneable {

	private static final long	serialVersionUID	= 1L;
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@GeneratedValue(generator = "idGenerator")
	@Column(length = 32, unique = true, nullable = false)
	protected String			id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
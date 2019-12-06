package com.lyl.layuiadmin.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * Hibernate通用entity父类，统一id以外的常用字段
 */
@MappedSuperclass
public class BaseHibernateModelNoId implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "crt_dttm", nullable = true, updatable = false)
	private Date				crtDttm				= new Date();

	@Transient
	private Date				crtDttmBefore;

	@Transient
	private Date				crtDttmAfter;

	@Column(length = 32, nullable = true, updatable = false)
	private String				crtUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date				lastUpdateDttm		= new Date();

	@Column(length = 32, nullable = true)
	private String				lastUpdateUser;

	@Column(nullable = false, columnDefinition = "char(1)")
	private Boolean				enableFlag			= Boolean.TRUE;

	@Version
	@Column
	private Long				version;

	/**
	 * 用于显示名称 ，不与数据库做映射
	 */
	@Transient
	private String				crtUserName;
	/**
	 * 用于显示名称 ，不与数据库做映射
	 */
	@Transient
	private String				lastUpdateUserName	= "";

	public Boolean getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(Boolean enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getEnableDisplayStyle() {
		if (getEnableFlag() == null || !getEnableFlag()) {
			return "text-decoration:line-through !important;";
		} else {
			return "";
		}
	}

	public Date getCrtDttm() {
		return crtDttm;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
	 */
	public String getCrtDttmString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return crtDttm != null ? sdf.format(crtDttm) : "";
	}

	public void setCrtDttm(Date crtDttm) {
		this.crtDttm = crtDttm;
	}

	public String getCrtUser() {
		return crtUser;
	}

	public void setCrtUser(String crtUserId) {
		this.crtUser = crtUserId;
	}

	public Date getLastUpdateDttm() {
		return lastUpdateDttm;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
	 */
	public String getLastUpdateDttmString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return lastUpdateDttm != null ? sdf.format(lastUpdateDttm) : "";
	}

	public void setLastUpdateDttm(Date lastUpdateDttm) {
		this.lastUpdateDttm = lastUpdateDttm;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUserId) {
		this.lastUpdateUser = lastUpdateUserId;
	}

	public String getCrtUserName() {
		return crtUserName;
	}

	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getCrtDttmBefore() {
		return crtDttmBefore;
	}

	public void setCrtDttmBefore(Date crtDttmBefore) {
		this.crtDttmBefore = crtDttmBefore;
	}

	public Date getCrtDttmAfter() {
		return crtDttmAfter;
	}

	public void setCrtDttmAfter(Date crtDttmAfter) {
		this.crtDttmAfter = crtDttmAfter;
	}
}
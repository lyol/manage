package com.lyl.layuiadmin.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统中所有上传的附件，附件统一存入Attachment表，
 * 以3个字段refByEntity refByEntityId refByEntityField逻辑关联方式 关联到各个使用附件的其它entity
 */
@Entity
@Table(name = Attachment.TABLE)
@Data
public class Attachment extends BaseHibernateModel {
	private static final long	serialVersionUID	= 1L;
	public static final String	TABLE				= "ATTACHMENT";

	/**
	 * 文件名称
	 */
	String						fileName;
	/**
	 * 文件类型
	 */
	String						fileType;
	/**
	 * 存储路径
	 */
	String						folderName;
	/**
	 * 存储桶名称
	 */
	String						bucketName;
}
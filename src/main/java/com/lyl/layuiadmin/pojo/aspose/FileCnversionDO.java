package com.lyl.layuiadmin.pojo.aspose;

import com.lyl.layuiadmin.enums.FileCnversionTypeEnum;
import com.lyl.layuiadmin.pojo.BaseHibernateModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 文件转换记录表
 */
@Entity
@Data
@Table(name = FileCnversionDO.TABLE)
public class FileCnversionDO extends BaseHibernateModel {

    private static final long serialVersionUID = 1L;
    public static final String TABLE = "FILE_CNVERSION_LOG";

    private String fileName;  //原文件名

    private String fileNameAfter;  //转换后文件名

    private String fileSize;   //原文件大小

    private String fileType;   //原文件类型

    private String fileTypeAfter;  //转换后文件类型

    @Enumerated(EnumType.STRING)
    private FileCnversionTypeEnum fileCnversionType;     //转换类型

    private String outputFilePath;  //保存路径

    private String inputFilePath;   //原文件路径

    private String fileSizeAfter;  //转换后文件大小

    private Integer status;  //操作状态

    private String timeConsuming; //消耗时长
}

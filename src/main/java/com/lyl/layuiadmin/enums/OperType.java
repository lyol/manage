package com.lyl.layuiadmin.enums;

/**
 * 操作类型枚举
 */
public enum OperType {
    Query("select"),//查询，一般不用，仅作为默认项
    ADD("add"),//新增操作
    DELETE("delete"),//删除操作
    UPDATE("update"),//更新操作
    UPLOAD("uploadFile"),//上传文件
    DOWNLOAD("downloadFile");//下载文件

    private String value;

    private OperType(String type){
        this.value = type;
    }

    public String getValue(){
        return this.value;
    }
}

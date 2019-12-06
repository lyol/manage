package com.lyl.layuiadmin.enums;

/**
 * 文件转换类型枚举
 */
public enum FileCnversionTypeEnum {

    DocToPDF("DocToPDF"),
    DocToDocx("DocToDocx"),
    DocToTexT("DocToTexT"),
    DocToXps("DocToXps"),
    DocToJPEG("DocToJPEG"),
    DocxToDoc("DocxToDoc"),
    DocxToPDF("DocxToPDF"),

    ExcelToPDF("ExcelToPDF"),
    HtmlToDoc("HtmlToDoc"),

    // PDF转换为其他的格式定义
    PdfTojpg("PdfTojpg"),
    PdfToDocx("PdfToDocx"),
    PdfToExcel("PdfToExcel"),
    PdfToPpt("PdfToPpt");

    private String value = null;

    private FileCnversionTypeEnum(String _value) {
        this.value = _value;
    }

    /**
     * 获取value
     */
    public String getValue() {
        return value;
    }

    }
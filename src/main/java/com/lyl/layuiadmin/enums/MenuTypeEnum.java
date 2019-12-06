package com.lyl.layuiadmin.enums;

/**
 * 菜单类型
 */
public enum MenuTypeEnum {

    DIRECTORY(0, "目录"),
    
    NAVIGATION(1, "导航"),
    
    BUTTON(2, "按钮");

    private String value = null;
    private Integer code = null;

    private MenuTypeEnum(Integer _code, String _value) {
        this.value = _value;
        this.code = _code;
    }

    public static MenuTypeEnum getEnumByKey(String key) {
        for (MenuTypeEnum e : MenuTypeEnum.values()) {
            if (e.getCode().equals(key)) {
                return e;
            }
        }
        return null;
    }

    /** 获取value */
    public String getValue() {
        return value;
    }

    /** 获取code */
    public Integer getCode() {
        return code;
    }
}
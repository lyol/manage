package com.lyl.layuiadmin.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 *	 模型视图封装
 */
@Data
public class ModelView {

    /**
     *	 填充model的内容
     * */
    private Map<String,Object> attributes;

    /**
     * 	返回视图名
     * */
    private String returnView;

    /**
     * 	构造函数
     * */
    public ModelView(){
        this.attributes = new HashMap<String, Object>();
    }
}

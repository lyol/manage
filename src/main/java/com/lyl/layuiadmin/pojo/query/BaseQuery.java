package com.lyl.layuiadmin.pojo.query;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * 分页基础查询
 */
@Getter
@Setter
public class BaseQuery implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int page;

    private int limit;

}

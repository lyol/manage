package com.lyl.layuiadmin.pojo.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysMenusVO {
	private String				id;

	private String				pid;

	private String				title;

	private String				font;

	private String				icon;

	private String				url;

	private String				param;

	private Boolean				spread;

	private Boolean				childrens;

	private List<SysMenusVO>	children;

}
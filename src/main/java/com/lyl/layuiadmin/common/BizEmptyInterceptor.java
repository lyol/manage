package com.lyl.layuiadmin.common;

import com.lyl.layuiadmin.controller.BaseController;
import com.lyl.layuiadmin.enums.OperType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.Date;

/**
 * 拦截器，使用切面的方法，拦截所有对DB的操作
 * 继承Hibernate的EmptyInterceptor重写onsave，ondelete，onFlushDirty， postFlush等方法
 */
@SuppressWarnings("serial")
@Slf4j
public class BizEmptyInterceptor extends EmptyInterceptor {

	ApplicationContext	applicationContext;

	/**
	 * 	保存
	 * @param entity 实体对象
	 * @param id 实体对象的ID
	 * @param state 属性值数组
	 * @param propertyNames 属性名数组
	 * @param types 属性值类型
	 * @return 成功为true
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		return onCommon(entity, id, OperType.ADD, propertyNames, state, null);
	}

	/**
	 * 	更新
	 * @param entity 实体对象
	 * @param id 实体对象的ID
	 * @param currentState 当前属性值数组
	 * @param previousState 原始属性值数组
	 * @param propertyNames 属性名数组
	 * @param types 属性值类型
	 * @return 成功为true
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
		log.debug("更新对象" + entity + "  " + id);
		return onCommon(entity, id, OperType.UPDATE, propertyNames, currentState, previousState);
	}

	/**
	 *	 删除对象
	 * @param entity 实体对象
	 * @param id 实体对象的ID
	 * @param state 属性值数组
	 * @param propertyNames 属性名数组
	 * @param types 属性值类型
	 * @return 成功为true
	 */
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.debug("删除对象" + entity + "  " + id);
		onCommon(entity, id, OperType.DELETE, propertyNames, null, state);
	}

	/**
	 *	 增删改公共方法
	 * @param entity 实体对象
	 * @param id 实体对象的ID
	 * @param currentState 对象的属性值
	 * @param propertyNames 对象的属性
	 * @param operType 增删改类型
	 * @param previousState 原对象的属性值
	 * @return
	 */
	private boolean onCommon(Object entity, Serializable id, OperType operType, String[] propertyNames,
			Object[] currentState, Object[] previousState) {
		if (operType != OperType.DELETE) {
			for (int i = 0; i < propertyNames.length; i++) {
				String propertyName = propertyNames[i];
				updateBaseProDefault(entity, currentState, i, propertyName);//即使不是审计对象，也设置默认值
			}
		}
		return true;
	}

	/**
	 *	 如果业务对象里没有赋值则此处设置 创建人、最后修改人、创建时间、最后修改时间 字段的默认值
	 * @param state 属性值数组
	 * @param i 属性值下标
	 * @param propertyName 属性名称
	 */
	private void updateBaseProDefault(Object entity, Object[] state, int i, String propertyName) {
		if ("crtDttm".equals(propertyName)) {
			Object currState = state[i];
			if (currState == null || currState.equals("")) {
				state[i] = new Date();
			}
		} else if ("crtUser".equals(propertyName)) {
			Object currState = state[i];
			if (currState == null || currState.equals("")) {
				String currentUser = "anonymousUser";
				if (BaseController.getUser() != null) {
					String userName = BaseController.getUserName();
					if (StringUtils.isNotBlank(userName)) {
						currentUser = userName;
					}
				}
				state[i] = currentUser;
			}
		} else if ("lastUpdateDttm".equals(propertyName)) {
			state[i] = new Date();
		} else if ("lastUpdateUser".equals(propertyName)) {
			if (BaseController.getUser() != null) {
				String currentUser = BaseController.getUserName();
				if (StringUtils.isBlank(currentUser)) {
					currentUser = "anonymousUser";
				}
				state[i] = currentUser;
			}
		}
	}
}
package com.lyl.layuiadmin.service.system;

import com.lyl.layuiadmin.common.Result;
import com.lyl.layuiadmin.pojo.query.BaseQuery;
import com.lyl.layuiadmin.pojo.system.SysMenuDO;

import java.util.List;
import java.util.Set;

public interface MenusService {
    Result listMenus(BaseQuery query);

    List<SysMenuDO> list();

    Result save(SysMenuDO menuDO);
    
    SysMenuDO get(String id);
    /**物理删除*/
    Result delete(String id);
    /**逻辑删除*/
    Result remove(String id,Boolean status);

    Result update(SysMenuDO menuDO);
    /**初始化菜单数据*/
    Result listInitMenus(String userName);
    /**授权菜单数据*/
    Result listGrantMenus(Long roleId);
    /**查询客户拥有的菜单权限*/
    Set<String> listPerms(String userName);
}

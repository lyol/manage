package com.lyl.layuiadmin.controller.system;

import com.lyl.layuiadmin.common.Result;
import com.lyl.layuiadmin.config.BizOperLog;
import com.lyl.layuiadmin.controller.BaseController;
import com.lyl.layuiadmin.enums.OperType;
import com.lyl.layuiadmin.pojo.query.BaseQuery;
import com.lyl.layuiadmin.pojo.system.SysMenuDO;
import com.lyl.layuiadmin.service.system.MenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/system")
public class MenusController extends BaseController {
    @Autowired
    private MenusService menusService;

    @GetMapping("/menus")
    public String page(Model model) {
        List<SysMenuDO> menus = menusService.list();
        model.addAttribute("menus", menus);
        return "system/menus";
    }
    
    @GetMapping("/menuedit")
    public String editPage(Model model,String id) {
    	SysMenuDO menuDO = menusService.get(id);
        List<SysMenuDO> menus = menusService.list();
        model.addAttribute("menus", menus);
        model.addAttribute("menu", menuDO);
        return "system/menuedit";
    }
    
    @PostMapping("/menuedit")
    @ResponseBody
    public Result edit(SysMenuDO menuDO) {
        return menusService.update(menuDO);
    }

    @PostMapping("/menus/list")
    @ResponseBody
    public Result listMenus(BaseQuery query) {
        Result result = menusService.listMenus(query);
        return result;
    }
    
    @PostMapping("/menus/add")
    @ResponseBody
    @BizOperLog(operType = OperType.ADD,memo = "添加菜单")
    public Result saveMenus(SysMenuDO menuDO) {
        return menusService.save(menuDO);
    }
    
    @PostMapping("/menus/delete")
    @ResponseBody
    @BizOperLog(operType = OperType.DELETE,memo = "物理删除菜单")
    public Result delete(String id) {
        return menusService.delete(id);
    }
    
    @PostMapping("/menus/remove")
    @ResponseBody
    @BizOperLog(operType = OperType.DELETE,memo = "逻辑删除菜单")
    public Result remove(String id ,Boolean status) {
        return menusService.remove(id,status);
    }
    /**初始化菜单数据*/
    @RequestMapping("/menus/init/list")
    @ResponseBody
    public Result initMenus() {
        Result result = menusService.listInitMenus(getUserName());
        return result;
    }
    
    /**授权菜单数据*/
    @RequestMapping("/menus/grant/list")
    @ResponseBody
    public Result grantMenus(Long roleId) {
        Result result = menusService.listGrantMenus(roleId);
        return result;
    }
}

package com.lyl.layuiadmin.Dao.system;

import com.lyl.layuiadmin.Dao.AbstractDao;
import com.lyl.layuiadmin.Dao.repo.system.SysMenuRepo;
import com.lyl.layuiadmin.pojo.system.SysMenuDO;
import com.lyl.layuiadmin.pojo.vo.SysMenusVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SysMenuDao extends AbstractDao {

	@PersistenceContext
	protected EntityManager	em;

	@Autowired
	SysMenuRepo sysMenuRepo;

	@Autowired
	JdbcTemplate			jdbcTemplate;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public SysMenuDO findById(String id) {
		SysMenuDO sysMenu = sysMenuRepo.getOne(id);
		return sysMenu;
	}

	public List<SysMenuDO> findAll() {
		return sysMenuRepo.findAll();
	}

	@SuppressWarnings("unchecked")
	public List<SysMenuDO> findAllEnableFlag_NoRoleList() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysMenuDO.class);
		//dc.setFetchMode("roleList", FetchMode.JOIN);
		List<SysMenuDO> menuList = super.findByCriteriaWithEnableFlag(dc);
		List<SysMenuDO> result = new ArrayList();
		for (SysMenuDO menu : menuList) {
			menu.setRoleList(null);
			result.add(menu);
		}
		return result;
	}

	public SysMenuDO saveOrUpdate(SysMenuDO sysMenu) {
		return sysMenuRepo.save(sysMenu);
	}

	/**
	 * 修改菜单状态
	 * @param id
	 * @return
	 */
	public SysMenuDO updateStatus(String id,Boolean status) {
		SysMenuDO sysMenu = this.findById(id);
		sysMenu.setStatus(status);
		return this.saveOrUpdate(sysMenu);
	}

	/**
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	public SysMenuDO logicDeleted(String id) {
		SysMenuDO sysMenu = this.findById(id);
		sysMenu.setEnableFlag(false);
		return this.saveOrUpdate(sysMenu);
	}

	/**
	 * 物理删除
	 * @param id
	 * @return
	 */
	public void deleted(String id) {
		SysMenuDO sysMenu = this.findById(id);
		sysMenuRepo.delete(sysMenu);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SysMenusVO> listInitMenus(Integer type, String pid, String userNmae) {
		String sql = "SELECT\r\n" + 
				"        csm.id,\r\n" + 
				"        csm.pid,\r\n" + 
				"        csm.title,\r\n" + 
				"        csm.font,\r\n" + 
				"        csm.icon,\r\n" + 
				"        csm.url,\r\n" + 
				"        csm.param,\r\n" + 
				"        csm.spread,\r\n" + 
				"        csm.children childrens\r\n" + 
				"    FROM\r\n" + 
				"        sys_menu csm\r\n" + 
				"    JOIN sys_role_menu csrm ON csrm.menu_id = csm.id\r\n" + 
				"    JOIN sys_user csu ON csu.role_id = csrm.role_id\r\n" + 
				"    WHERE\r\n" + 
				"        csm.status = 1\r\n" + 
				"        AND csu.enable_flag = 1\r\n" + 
				"        AND csm.type = " + type +
				"        AND csu.user_name = '" + userNmae + "'" +
				(StringUtils.isNoneBlank(pid)?  ("AND csm.pid = '" + pid + "'"):"")+
				"    ORDER BY\r\n" + 
				"        csm.sort ASC";
		List<SysMenusVO> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(SysMenusVO.class));
		return list;
	}

}

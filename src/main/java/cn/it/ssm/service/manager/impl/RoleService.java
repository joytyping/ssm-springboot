package cn.it.ssm.service.manager.impl;


import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.domain.auto.SysRole;
import cn.it.ssm.domain.auto.SysUserRole;
import cn.it.ssm.domain.auto.SysUserRoleExample;
import cn.it.ssm.mapper.auto.SysRoleMapper;
import cn.it.ssm.mapper.auto.SysUserRoleMapper;
import cn.it.ssm.service.manager.IRoleService;
import com.github.pagehelper.PageHelper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = true)
public class RoleService implements IRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<SysRole> findAllRoleList() {
        return sysRoleMapper.selectByExample(null);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean addRole(SysRole role) {
        return sysRoleMapper.insertSelective(role) != 0;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean editRole(SysRole role) {
        return sysRoleMapper.updateByPrimaryKeySelective(role) != 0;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteRole(List<SysRole> role) {
        SysRoleMapper templateMapper = sqlSessionTemplate.getMapper(SysRoleMapper.class);
        for (SysRole sysRole : role) {
            templateMapper.deleteByPrimaryKey(sysRole.getId());
        }
        return true;
    }

    @Override
    public boolean findRoleExistsUserByRoleId(SysRole sysRole) {
        SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
        SysUserRoleExample.Criteria criteria = sysUserRoleExample.createCriteria();
        criteria.andSysRoleIdEqualTo(sysRole.getId());
        PageHelper.startPage(1, 1);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByExample(sysUserRoleExample);
        return sysUserRoles.size() != 0;
    }

    @Override
    public List<SysPermission> findRolePermsByRoleId(SysRole sysRole) {
        return sysRoleMapper.findRolePermsByRoleId(sysRole);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean saveRolePermsByPermIds(Integer roleId, Integer[] rolePermIds) {
        sysRoleMapper.deleteRolePermsByRoleId(roleId);
        for (Integer rolePermId : rolePermIds) {
            sysRoleMapper.saveRolePermsByPermId(roleId, rolePermId);
        }
        return true;
    }
}

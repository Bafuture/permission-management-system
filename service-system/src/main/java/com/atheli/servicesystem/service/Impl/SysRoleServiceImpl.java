package com.atheli.servicesystem.service.Impl;

import com.atheli.model.system.SysRole;
import com.atheli.model.system.SysUserRole;
import com.atheli.model.vo.AssginRoleVo;
import com.atheli.model.vo.SysRoleQueryVo;
import com.atheli.servicesystem.mapper.SysRoleMapper;
import com.atheli.servicesystem.mapper.SysUserRoleMapper;
import com.atheli.servicesystem.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
   @Autowired
   private SysRoleMapper sysRoleMapper;
   @Autowired
   private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Page findPage(Page page, SysRoleQueryVo sysRoleQueryVo) {

        return sysRoleMapper.findPage(page,sysRoleQueryVo);
    }
    @Override
    public Map getRolesByUserId(Long userId) {
        Map map=new HashMap();
        List<SysRole> allRoles = sysRoleMapper.selectList(null);
        List<Long> userRoles = sysUserRoleMapper.getRoleIdByUserId(userId);
        if (userRoles == null) {
            userRoles = new ArrayList<>();
        }
        map.put("allRoles", allRoles);
        map.put("userRolesIds",userRoles);
        return map;
    }
    @Override
    public void doAssignRole(AssginRoleVo assginRoleVo) {
        Long userId = assginRoleVo.getUserId();
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        if(roleIdList != null && roleIdList.size() > 0){
            sysUserRoleMapper.batchInsert(userId, roleIdList);
        }
    }
}


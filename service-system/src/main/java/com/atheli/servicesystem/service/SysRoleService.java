package com.atheli.servicesystem.service;

import com.atheli.model.system.SysRole;
import com.atheli.model.vo.AssginRoleVo;
import com.atheli.model.vo.SysRoleQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    Page findPage(Page page,SysRoleQueryVo sysRoleQueryVos);
   Map getRolesByUserId(Long userId);
    void doAssignRole(AssginRoleVo assginRoleVo);}

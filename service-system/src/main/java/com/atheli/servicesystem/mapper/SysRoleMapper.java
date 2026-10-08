package com.atheli.servicesystem.mapper;

import com.atheli.model.system.SysRole;
import com.atheli.model.vo.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.ibatis.annotations.Param;

public interface SysRoleMapper extends BaseMapper<SysRole> {
  Page findPage(Page page, @Param("vo") SysRoleQueryVo sysRoleQueryVo);

}

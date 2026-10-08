package com.atheli.servicesystem.service;


import com.atheli.model.system.SysUser;
import com.atheli.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo adminQueryVo);

    void updateStatus(Long id, Integer status);

    SysUser getByUsername(String username);

    /**
     * 根据用户名获取用户登录信息
     * @param username
     * @return
     *
     */
    Map<String, Object> getUserInfo(String username);

    SysUser getUserByUserName(String username);
}

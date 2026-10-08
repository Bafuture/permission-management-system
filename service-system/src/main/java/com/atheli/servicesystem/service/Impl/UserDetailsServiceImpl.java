package com.atheli.servicesystem.service.Impl;

import com.atheli.common.result.ResultCodeEnum;
import com.atheli.model.system.SysUser;
import com.atheli.system.exception.GuiguException;
import com.atheli.system.custom.CustomUser;
import com.atheli.servicesystem.service.SysMenuService;
import com.atheli.servicesystem.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserDetailsServiceImpl {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    public CustomUser loadUserByUsername(String username) {
        SysUser sysUser = sysUserService.getByUsername(username);
        if(null == sysUser) {
            throw new GuiguException(ResultCodeEnum.ACCOUNT_ERROR);
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }
        List<String> userPermsList = sysMenuService.findUserPermsList(sysUser.getId());
        List<String> permissions = userPermsList.stream().filter(code -> !StringUtils.isEmpty(code.trim())).map(String::trim).collect(Collectors.toList());
        return new CustomUser(sysUser, permissions);
    }
}

package com.atheli.system.custom;

import com.atheli.model.system.SysUser;

import java.util.List;

public class CustomUser {

    private final SysUser sysUser;
    private final List<String> permissions;

    public CustomUser(SysUser sysUser, List<String> permissions) {
        this.sysUser = sysUser;
        this.permissions = permissions;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}

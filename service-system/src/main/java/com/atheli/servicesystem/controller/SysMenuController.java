package com.atheli.servicesystem.controller;

import com.atheli.common.result.Result;
import com.atheli.model.system.SysMenu;
import com.atheli.servicesystem.service.SysMenuService;
import com.atheli.model.vo.AssginMenuVo;
import com.atheli.system.annotation.Log;
import com.atheli.system.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Operation(summary = "获取菜单")
    @GetMapping("findNodes")
    public Result findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }

    @Operation(summary = "新增菜单")
    @PostMapping("save")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    public Result save(@RequestBody SysMenu permission) {
        sysMenuService.save(permission);
        return Result.ok();
    }

    @Operation(summary = "修改菜单")
    @PutMapping("update")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    public Result updateById(@RequestBody SysMenu permission) {
        sysMenuService.updateById(permission);
        return Result.ok();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("remove/{id}")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    public Result delete(@PathVariable Long id) {
        sysMenuService.deleteById(id);
        return Result.ok();
    }

    @Operation(summary = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @Operation(summary = "给角色分配权限")
    @PostMapping("/doAssign")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo) {
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();
    }
@Operation(summary = "回显")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id) {
        SysMenu sysMenu = sysMenuService.getById(id);
        return Result.ok(sysMenu);
    }

    @Operation(summary = "查询角色权限")
    @GetMapping("/getRoleMenuList/{roleId}")
    public Result getRoleMenuList(@PathVariable Long roleId) {
        List<SysMenu> roleMenuList = sysMenuService.getRoleMenuList(roleId);
        return Result.ok(roleMenuList);
    }

}


package com.atheli.servicesystem.controller;

import com.atheli.common.result.Result;
import com.atheli.common.util.MD5;
import com.atheli.model.system.SysUser;
import com.atheli.servicesystem.service.SysUserService;
import com.atheli.model.vo.SysUserQueryVo;
import com.atheli.system.annotation.Log;
import com.atheli.system.enums.BusinessType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
@CrossOrigin
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,

            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,

            @Parameter(name = "userQueryVo", description = "查询对象", required = false)
                    SysUserQueryVo userQueryVo) {
        Page<SysUser> pageParam = new Page<>(page, limit);
        IPage<SysUser> pageModel = sysUserService.selectPage(pageParam, userQueryVo);
        return Result.ok(pageModel);
    }

    @Operation(summary = "获取用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @Operation(summary = "保存用户")
    @PostMapping("/save")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    public Result save(@RequestBody SysUser user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        sysUserService.save(user);
        return Result.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping("/update")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public Result updateById(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.ok();
    }

    @Operation(summary = "回显")
    @PutMapping("/geById/{id}")
    public Result getById(@RequestBody Long id ) {
        SysUser byId = sysUserService.getById(id);
        return Result.ok(byId);
    }
    @Operation(summary = "用户管理")
    @DeleteMapping("/remove/{id}")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    public Result remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "用户管理")
    @GetMapping("/updateStatus/{id}/{status}")
    @Log(title = "用户管理", businessType = BusinessType.STATUS)
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.ok();
    }
}


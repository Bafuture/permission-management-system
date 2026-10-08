package com.atheli.servicesystem.controller;

import com.atheli.common.result.Result;
import com.atheli.model.system.SysRole;
import com.atheli.model.vo.AssginRoleVo;
import com.atheli.model.vo.SysRoleQueryVo;
import com.atheli.servicesystem.service.SysRoleService;
import com.atheli.system.annotation.Log;
import com.atheli.system.enums.BusinessType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/admin/system/sysRole")
@RestController
@Tag(name = "角色控制器")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询所有角色
     *
     * @return
     */
    /*@RequestMapping("/findAll")
    public List<SysRole> findAll() {
        List<SysRole> rolelist = sysRoleService.list();
        return rolelist;
    }*/
    @RequestMapping("/findAll")
    @Operation(summary = "查询所有角色")
    public Result findAll() {
        List<SysRole> rolelist = sysRoleService.list();
        return Result.ok(rolelist);
    }

    /**
     * 添加分页
     *
     * @param
     * @return
     */
    @GetMapping("/{pageNum}/{pageSize}")
    @Operation(summary = "分页查询")
    public Result index(@PathVariable Integer pageNum,
                        @PathVariable Integer pageSize,
                        @Parameter(required = false, description = "角色查询条件") SysRoleQueryVo sysRoleQueryVo) {

       /* //把pageNum和pageSize封装到page对象中,执行自动分页逻辑
        //这是mybatisplus提供的分页插件
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        //获取用户姓名信息
        String roleName = sysRoleQueryVo.getRoleName();
        //使用传进来的用户信息和querywrapper的模糊查询查询数据库信息
        QueryWrapper<SysRole> sysroleWrapper = new QueryWrapper<>();
        sysroleWrapper.like(roleName != null , "role_name", roleName);
        // page(E page, Wrapper<T> queryWrapper)
        Page<SysRole> page1 = sysRoleService.page(page, sysroleWrapper);
        return Result.ok(page1);*/

        //自定义方法
        //分页
        Page page = new Page(pageNum, pageSize);

        Page page1 = sysRoleService.findPage(page, sysRoleQueryVo);

        return Result.ok(page1);

    }

    //增加信息
    @Operation(summary = "添加角色")
    @PostMapping("/add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    public Result add(@Parameter(required = true, description = "角色信息", name = "sysRole")
                      @RequestBody SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }


    }

    //查询角色
    @Operation(summary = "查询角色")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        return Result.ok(sysRoleService.getById(id));
    }

    //修改角色
    @Operation(summary = "修改角色")
    @PostMapping("/update")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    public Result update(@Parameter(required = true, description = "角色信息", name = "sysRole")
                         @RequestBody SysRole sysRole) {
        boolean update = sysRoleService.updateById(sysRole);
        sysRole.setUpdateTime(null);
        if (update) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }

    //删除角色
    @Operation(summary = "删除角色")
    @DeleteMapping("/remove/{id}")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    public Result remove(@PathVariable Long id) {
        boolean remove = sysRoleService.removeById(id);
        if (remove) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //根据id列表批量删除角色
    @Operation(summary = "批量删除角色")
    @DeleteMapping("/batchRemove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean remove = sysRoleService.removeByIds(idList);
        if (remove) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @GetMapping("/getRolesByUserId/{userId}")
    @Operation(summary = "获取用户的角色信息")
    public Result getRolesByUserId(@PathVariable Long userId){
        Map map= sysRoleService.getRolesByUserId(userId);
        return Result.ok(map);
    }
    @PostMapping("/doAssignRole")
    @Operation(summary = "为用户分配角色")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    public Result doAssignRole(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssignRole(assginRoleVo);
        return Result.ok();
    }
}




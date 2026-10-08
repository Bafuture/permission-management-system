package com.atheli.servicesystem.controller;

import com.atheli.common.helper.JwtHelper;
import com.atheli.common.result.Result;
import com.atheli.common.result.ResultCodeEnum;
import com.atheli.common.util.MD5;
import com.atheli.common.util.IpUtil;
import com.atheli.model.system.SysUser;
import com.atheli.model.vo.LoginVo;
import com.atheli.servicesystem.service.AsyncLoginLogService;
import com.atheli.servicesystem.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/admin/system/index")
@RestController
@Tag(name = "后台登录管理")
public class indexController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AsyncLoginLogService asyncLoginLogService;
    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        // 1. 从前端VO获取账号密码
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();

        // 2. 根据用户名查询数据库用户
        SysUser sysUser = sysUserService.getUserByUserName(username);

        // 3. 校验：账号不存在 / 账号禁用 / 已逻辑删除
        String ipaddr = IpUtil.getIpAddress(request);
        if (sysUser == null || sysUser.getStatus() == 0 || sysUser.getIsDeleted() == 1) {
            asyncLoginLogService.recordLoginLog(username, 0, ipaddr, "账号不存在或已停用");
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        if (!MD5.encrypt(password).equals(sysUser.getPassword())) {
            asyncLoginLogService.recordLoginLog(username, 0, ipaddr, "密码不正确");
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        // 5. 登录成功，生成JWT令牌，携带用户ID，自带过期时间
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        asyncLoginLogService.recordLoginLog(username, 1, ipaddr, "登录成功");
        return Result.ok(map);
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public Result info(@RequestParam(required = false) String token) {
        Long userId = JwtHelper.getUserId(token);
        if (userId == null) {
            return Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        }
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        return Result.ok(sysUserService.getUserInfo(sysUser.getUsername()));
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}

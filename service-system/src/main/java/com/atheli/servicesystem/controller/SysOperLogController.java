package com.atheli.servicesystem.controller;

import com.atheli.common.result.Result;
import com.atheli.model.system.SysOperLog;
import com.atheli.servicesystem.service.SysOperLogService;
import com.atheli.model.vo.SysOperLogQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 *
 * @author qy
 *
 */
@Tag(name = "SysOperLog管理")
@RestController
@RequestMapping(value="/admin/system/sysOperLog")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysOperLogController {
	
	@Resource
	private SysOperLogService sysOperLogService;

	@Operation(summary = "获取分页列表")
	@GetMapping("{page}/{limit}")
	public Result index(
		@Parameter(name = "page", description = "当前页码", required = true)
		@PathVariable Long page,
	
		@Parameter(name = "limit", description = "每页记录数", required = true)
		@PathVariable Long limit,
	
		@Parameter(name = "sysOperLogVo", description = "查询对象", required = false)
                SysOperLogQueryVo sysOperLogQueryVo) {
		Page<SysOperLog> pageParam = new Page<>(page, limit);
		IPage<SysOperLog> pageModel = sysOperLogService.selectPage(pageParam, sysOperLogQueryVo);
		return Result.ok(pageModel);
	}

	@Operation(summary = "获取")
	@GetMapping("get/{id}")
	public Result get(@PathVariable Long id) {
		SysOperLog sysOperLog = sysOperLogService.getById(id);
		return Result.ok(sysOperLog);
	}

}


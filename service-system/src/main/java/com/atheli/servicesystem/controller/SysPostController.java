package com.atheli.servicesystem.controller;

import com.atheli.system.annotation.Log;
import com.atheli.system.enums.BusinessType;
import com.atheli.common.result.Result;
import com.atheli.model.system.SysPost;
import com.atheli.servicesystem.service.SysPostService;
import com.atheli.model.vo.SysPostQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

@Tag(name = "岗位管理")
@RestController
@RequestMapping(value="/admin/system/sysPost")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysPostController {
	
	@Resource
	private SysPostService sysPostService;

	@Operation(summary = "获取分页列表")
	@GetMapping("{page}/{limit}")
	public Result index(
		@Parameter(name = "page", description = "当前页码", required = true)
		@PathVariable Long page,
	
		@Parameter(name = "limit", description = "每页记录数", required = true)
		@PathVariable Long limit,
	
		@Parameter(name = "sysPostVo", description = "查询对象", required = false)
                SysPostQueryVo sysPostQueryVo) {
		Page<SysPost> pageParam = new Page<>(page, limit);
		IPage<SysPost> pageModel = sysPostService.selectPage(pageParam, sysPostQueryVo);
		return Result.ok(pageModel);
	}

	@Operation(summary = "获取")
	@GetMapping("get/{id}")
	public Result get(@PathVariable Long id) {
		SysPost sysPost = sysPostService.getById(id);
		return Result.ok(sysPost);
	}

	@GetMapping("findAll")
	public Result findAll() {
		return Result.ok(sysPostService.findAll());
	}

	@Log(title = "岗位管理", businessType = BusinessType.INSERT)
	@Operation(summary = "新增")
	@PostMapping("save")
	public Result save(@RequestBody SysPost sysPost) {
		sysPostService.save(sysPost);
		return Result.ok();
	}

	@Log(title = "岗位管理", businessType = BusinessType.UPDATE)
	@Operation(summary = "修改")
	@PutMapping("update")
	public Result updateById(@RequestBody SysPost sysPost) {
		sysPostService.updateById(sysPost);
		return Result.ok();
	}

	@Log(title = "岗位管理", businessType = BusinessType.DELETE)
	@Operation(summary = "删除")
	@DeleteMapping("remove/{id}")
	public Result remove(@PathVariable Long id) {
		sysPostService.removeById(id);
		return Result.ok();
	}

	@Operation(summary = "更新状态")
	@GetMapping("updateStatus/{id}/{status}")
	public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
		sysPostService.updateStatus(id, status);
		return Result.ok();
	}
	
}


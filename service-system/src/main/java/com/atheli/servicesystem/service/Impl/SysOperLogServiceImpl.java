package com.atheli.servicesystem.service.Impl;

import com.atheli.model.system.SysOperLog;
import com.atheli.servicesystem.mapper.SysOperLogMapper;
import com.atheli.servicesystem.service.SysOperLogService;
import com.atheli.model.vo.SysOperLogQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

	@Resource
	private SysOperLogMapper sysOperLogMapper;

	@Override
	public IPage<SysOperLog> selectPage(Page<SysOperLog> pageParam, SysOperLogQueryVo sysOperLogQueryVo) {

		return sysOperLogMapper.selectPage(pageParam, sysOperLogQueryVo);
	}
}

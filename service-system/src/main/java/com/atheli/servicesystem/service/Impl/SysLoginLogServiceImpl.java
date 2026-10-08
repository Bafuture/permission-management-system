package com.atheli.servicesystem.service.Impl;

import com.atheli.model.system.SysLoginLog;
import com.atheli.servicesystem.mapper.SysLoginLogMapper;
import com.atheli.servicesystem.service.SysLoginLogService;
import com.atheli.model.vo.SysLoginLogQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

	@Resource
	private SysLoginLogMapper sysLoginLogMapper;

	@Override
	public IPage<SysLoginLog> selectPage(Page<SysLoginLog> pageParam, SysLoginLogQueryVo sysLoginLogQueryVo) {

		return sysLoginLogMapper.selectPage(pageParam, sysLoginLogQueryVo);
	}


}

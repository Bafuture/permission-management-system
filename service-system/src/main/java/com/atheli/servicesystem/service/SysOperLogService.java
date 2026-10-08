package com.atheli.servicesystem.service;

import com.atheli.model.system.SysOperLog;
import com.atheli.model.vo.SysOperLogQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysOperLogService extends IService<SysOperLog> {

    IPage<SysOperLog> selectPage(Page<SysOperLog> pageParam, SysOperLogQueryVo sysOperLogQueryVo);
}

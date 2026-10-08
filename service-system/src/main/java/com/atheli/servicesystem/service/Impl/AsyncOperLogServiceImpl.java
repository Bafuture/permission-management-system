package com.atheli.servicesystem.service.Impl;

import com.atheli.servicesystem.service.AsyncOperLogService;
import com.atheli.model.system.SysOperLog;
import com.atheli.servicesystem.mapper.SysOperLogMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService {

    @Resource
    private SysOperLogMapper sysOperLogMapper;

    @Async
    @Override
    public void saveSysLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insert(sysOperLog);
    }
}

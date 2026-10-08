package com.atheli.servicesystem.service;

import com.atheli.model.system.SysOperLog;

public interface AsyncOperLogService {
    void saveSysLog(SysOperLog sysOperLog);
}

package com.atheli.servicesystem.service.Impl;

import com.atheli.model.system.SysLoginLog;
import com.atheli.servicesystem.service.AsyncLoginLogService;
import com.atheli.servicesystem.mapper.SysLoginLogMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Date;

@Service
public class AsyncLoginLogServiceImpl implements AsyncLoginLogService {

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param ipaddr ip
     * @param message 消息内容
     * @return
     */
    @Async
    @Override
    public void recordLoginLog(String username, Integer status, String ipaddr, String message) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setIpaddr(ipaddr);
        sysLoginLog.setMsg(message);
        // 日志状态
        sysLoginLog.setStatus(status);
        sysLoginLog.setAccessTime(new Date());
        sysLoginLogMapper.insert(sysLoginLog);
    }

}

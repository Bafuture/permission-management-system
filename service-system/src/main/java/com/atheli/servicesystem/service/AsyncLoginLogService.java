package com.atheli.servicesystem.service;

public interface AsyncLoginLogService {
    void recordLoginLog(String username, Integer status, String ipaddr, String message);
}

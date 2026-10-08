package com.atheli.servicesystem.aspect;

import com.alibaba.fastjson2.JSON;
import com.atheli.common.helper.JwtHelper;
import com.atheli.common.util.IpUtil;
import com.atheli.model.system.SysOperLog;
import com.atheli.servicesystem.service.AsyncOperLogService;
import com.atheli.system.annotation.Log;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class LogAspect {

    @Resource
    private AsyncOperLogService asyncOperLogService;

    @Around("@annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        Object result = null;
        Throwable error = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            error = throwable;
            throw throwable;
        } finally {
            saveLog(joinPoint, logAnnotation, result, error);
        }
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Log logAnnotation, Object result, Throwable error) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes == null ? null : attributes.getRequest();
        SysOperLog operLog = new SysOperLog();
        operLog.setTitle(logAnnotation.title());
        operLog.setBusinessType(logAnnotation.businessType().name());
        operLog.setMethod(truncate(joinPoint.getSignature().getDeclaringType().getSimpleName()
                + "." + joinPoint.getSignature().getName() + "()", 100));
        operLog.setRequestMethod(request == null ? null : request.getMethod());
        operLog.setOperatorType("1");
        operLog.setOperName(resolveOperName(request));
        operLog.setOperUrl(request == null ? null : truncate(request.getRequestURI(), 255));
        operLog.setOperIp(request == null ? null : truncate(IpUtil.getIpAddress(request), 128));
        operLog.setOperParam(truncate(resolveParams(joinPoint.getArgs()), 2000));
        operLog.setJsonResult(result == null ? null : truncate(JSON.toJSONString(result), 2000));
        operLog.setStatus(error == null ? 1 : 0);
        operLog.setErrorMsg(error == null ? null : truncate(error.getMessage(), 2000));
        operLog.setOperTime(new Date());
        asyncOperLogService.saveSysLog(operLog);
    }

    private String resolveOperName(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return JwtHelper.getUsername(request.getHeader("token"));
    }

    private String resolveParams(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        List<Object> params = Arrays.stream(args)
                .filter(arg -> !(arg instanceof ServletRequest) && !(arg instanceof ServletResponse))
                .collect(Collectors.toList());
        return JSON.toJSONString(params);
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}

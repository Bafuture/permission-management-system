package com.atheli.servicesystem;

import com.atheli.model.system.SysRole;
import com.atheli.servicesystem.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceSystemApplicationTests {
@Autowired
private SysRoleMapper sysRoleMapper;
//查询
    @Test
    void contextLoads() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_name","角色管理员");
        queryWrapper.like("role_name","角色管理员");
        System.out.println("结果"+sysRoleMapper.selectList(queryWrapper));

    }
//添加
    @Test
    void text02(){
        SysRole sysRole= new SysRole();
        sysRole.setDescription("角色管理系统");
        sysRole.setRoleCode("role_manager");
        sysRole.setRoleName("角色管理系统");
        int insert = sysRoleMapper.insert(sysRole);
        System.out.println(insert);
    }
    @Test
    public void text03(){
        int i = sysRoleMapper.deleteById(9);
        System.out.println(i);
    }
}

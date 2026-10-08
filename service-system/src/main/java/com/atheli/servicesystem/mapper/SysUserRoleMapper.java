package com.atheli.servicesystem.mapper;

import com.atheli.model.system.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 根据用户ID获取角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdByUserId(@Param("userId") Long userId);

    void batchInsert(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);
}


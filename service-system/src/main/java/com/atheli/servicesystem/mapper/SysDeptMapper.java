package com.atheli.servicesystem.mapper;

import com.atheli.model.system.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysDeptMapper extends BaseMapper<SysDept> {


}

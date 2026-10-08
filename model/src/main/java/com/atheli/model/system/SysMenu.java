package com.atheli.model.system;

import com.atheli.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(description = "菜单")
@TableName("sys_menu")
public class SysMenu extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

    @Schema(description = "所属上级菜单ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "菜单名称")
    @TableField("name")
    private String name;

    @Schema(description = "类型(1:菜单,2:按钮)")
    @TableField("type")
    private Integer type;

    @Schema(description = "路由地址")
    @TableField("path")
    private String path;

    @Schema(description = "前端组件路径")
    @TableField("component")
    private String component;

    @Schema(description = "权限标识")
    @TableField("perms")
    private String perms;

    @Schema(description = "菜单图标")
    @TableField("icon")
    private String icon;

    @Schema(description = "排序值")
    @TableField("sort_value")
    private Integer sortValue;

    @Schema(description = "状态(0:禁止,1:正常)")
    @TableField("status")
    private Integer status;

	// 下级列表
	@TableField(exist = false)
	private List<SysMenu> children;
	//是否选中
	@TableField(exist = false)
	private boolean isSelect;
}


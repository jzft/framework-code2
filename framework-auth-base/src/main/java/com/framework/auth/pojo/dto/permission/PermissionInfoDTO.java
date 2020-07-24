package com.framework.auth.pojo.dto.permission;

import com.framework.auth.pojo.entity.PermissionEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * date 2020/6/18 下午5:55
 *
 * @author casper
 **/
public class PermissionInfoDTO {

    /**
     * 权限id
     */
    @ApiModelProperty(name = "权限id")
    private Integer id;

    /**
     * 权限名称
     */
    @NotNull
    @NotBlank
    @ApiModelProperty(name = "权限名称", required = true)
    private String permission;

    /**
     * 权限拦截地址
     */
    @NotNull
    @NotBlank
    @ApiModelProperty(name = "权限拦截地址", required = true)
    private String url;

    /**
     * 权限标识符
     */
    @ApiModelProperty(name = "权限标识符")
    private String perm;

    /**
     * 父权限id
     */
    @NotNull
    @ApiModelProperty(name = "父权限id", required = true)
    private Integer parentId;

    /**
     * 系统类型
     */
    @ApiModelProperty(name = "系统类型")
    private String sysType;

    /**
     * 描述
     */
    @ApiModelProperty(name = "描述")
    private String description;

    /**
     * 图标
     */
    @ApiModelProperty(name = "图标")
    private String icon;

    /**
     * 排序序号
     */
    @ApiModelProperty(name = "排序序号", notes = "默认0")
    private Integer orderNum = 0;

    /**
     * 菜单类型
     */
    @ApiModelProperty(name = "菜单类型")
    private Integer menuType;

    /**
     * 是否可见
     */
    @ApiModelProperty(name = "是否可见", notes = "默认true")
    private boolean visible = true;

    /**
     * 是否缓存
     */
    @ApiModelProperty(name = "是否缓存", notes = "默认true")
    private boolean cache = true;

    /**
     * 是否可用
     */
    @ApiModelProperty(name = "是否可用", notes = "默认true")
    private boolean available = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public PermissionInfoDTO() {

    }

    public PermissionInfoDTO(PermissionEntity entity) {
        this.setId(entity.getId());
        this.setCache(entity.getCacheable());
        this.setDescription(entity.getDescription());
        this.setIcon(entity.getIcon());
        this.setMenuType(entity.getMenuType());
        this.setOrderNum(entity.getOrderNum());
        this.setParentId(entity.getParentId());
        this.setPerm(entity.getPerm());
        this.setPermission(entity.getPermission());
        this.setSysType(entity.getSysType());
        this.setUrl(entity.getUrl());
    }


}

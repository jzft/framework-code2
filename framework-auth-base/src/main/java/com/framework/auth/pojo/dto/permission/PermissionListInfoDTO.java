package com.framework.auth.pojo.dto.permission;

import com.framework.auth.pojo.entity.PermissionEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * date 2020/6/29 下午2:08
 *
 * @author casper
 **/
public class PermissionListInfoDTO {

    /**
     * 权限id
     */
    @ApiModelProperty(name = "权限id")
    private int id;

    /**
     * 权限名称
     */
    @ApiModelProperty(name = "权限名称", required = true)
    private String name;

    /**
     * 排序序号
     */
    @ApiModelProperty(name = "排序序号")
    private int orderNum;

    /**
     * 是否可用
     */
    @ApiModelProperty(name = "是否可用", notes = "默认true")
    private boolean available;


    public PermissionListInfoDTO() {
    }


    public PermissionListInfoDTO(PermissionEntity entity) {
        this.setId(entity.getId());
        this.setAvailable(entity.getAvailable());
        this.setName(entity.getPermission());
        this.setOrderNum(entity.getOrderNum());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

package com.framework.auth.pojo.dto.role;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * date 2020/6/18 下午6:23
 *
 * @author casper
 **/
public class RolePermissionInfoDTO {

    /**
     * 角色id
     */
    @ApiModelProperty(name = "角色id")
    @NotNull(message = "角色id不能为空")
    private Integer roleId;

    /**
     * 权限列表
     */
    @ApiModelProperty(name = "权限列表")
    private List<Integer> permissionId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(List<Integer> permissionId) {
        this.permissionId = permissionId;
    }
}

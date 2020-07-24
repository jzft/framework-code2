package com.framework.auth.pojo.dto.role;

import com.framework.auth.pojo.entity.RoleEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * date 2020/6/18 下午5:38
 *
 * @author casper
 **/
public class RoleInfoDTO {

    /**
     * 角色id,用于更新操作
     */
    @ApiModelProperty(name = "角色id，用于更新操作")
    private Integer id;

    /**
     * 角色名称
     */
    @ApiModelProperty(name = "角色名称")
    @NotNull(message = "角色名称不能为空")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 描述
     */
    @ApiModelProperty(name = "描述")
    private String desc;
    /**
     * 角色代码
     */
    @ApiModelProperty(name = "角色代码")
    @NotNull(message = "角色代码不能为空")
    @NotBlank(message = "角色代码不能为空")
    private String roleCode;

    /**
     * 权限ids
     */
    @ApiModelProperty(name = "权限ids")
    private List<Integer> permissionId;

    /**
     * 数据权限，1-本人，2-部门，4-指定
     */
    @ApiModelProperty(name = "数据权限，1-本人，2-部门，4-指定")
    private int dataPermissionType;

    /**
     * 数据权限的具体目标id列表
     */
    @ApiModelProperty(name = "数据权限的具体目标id列表")
    private List<Integer> dataPermissionTargets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<Integer> getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(List<Integer> permissionId) {
        this.permissionId = permissionId;
    }

    public int getDataPermissionType() {
        return dataPermissionType;
    }

    public void setDataPermissionType(int dataPermissionType) {
        this.dataPermissionType = dataPermissionType;
    }

    public List<Integer> getDataPermissionTargets() {
        return dataPermissionTargets;
    }

    public void setDataPermissionTargets(List<Integer> dataPermissionTargets) {
        this.dataPermissionTargets = dataPermissionTargets;
    }

    public static RoleInfoDTO build(RoleEntity entity) {
        RoleInfoDTO result = new RoleInfoDTO();
        result.setId(entity.getId());
        result.setRoleName(entity.getName());
        result.setDesc(entity.getDescription());
        result.setRoleCode(entity.getRoleCode());
        return result;
    }


}

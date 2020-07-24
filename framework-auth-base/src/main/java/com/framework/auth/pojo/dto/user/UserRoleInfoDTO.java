package com.framework.auth.pojo.dto.user;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * date 2020/6/18 下午5:47
 *
 * @author casper
 **/
public class UserRoleInfoDTO {

    /**
     * 用户id
     */
    @ApiModelProperty(name = "用户id")
    @NotNull(message = "用户id不能为空")
    private Integer userId;

    /**
     * 角色id集合
     */
    @ApiModelProperty(name = "角色id集合")
    private List<Integer> roleIds;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}

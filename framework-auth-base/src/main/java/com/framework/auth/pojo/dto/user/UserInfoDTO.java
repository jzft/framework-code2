package com.framework.auth.pojo.dto.user;

import com.framework.auth.pojo.entity.UserEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * date 2020/6/18 下午4:59
 *
 * @author casper
 **/
public class UserInfoDTO {

    /**
     * 用户id，用于更新操作
     */
    @ApiModelProperty(name = "用户id，用于更新操作")
    private Integer id;
    /**
     * 用户名称
     */
    @ApiModelProperty(name = "用户名称")
    @NotNull(message = "用户名称不能为空")
    @NotBlank(message = "用户名称不能为空")
    private String username;

    /**
     * 用户昵称
     */
    @ApiModelProperty(name = "用户昵称")
    private String nickName;

    /**
     * 登录密码
     */
    @ApiModelProperty(name = "登录密码")
    private String password;

    /**
     * 父id
     */
    @ApiModelProperty(name = "父id")
    private Integer parentId;

    /**
     * 类型
     */
    @ApiModelProperty(name = "类型，暂时没用")
    private Integer type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static UserInfoDTO build(UserEntity entity) {
        UserInfoDTO result = new UserInfoDTO();
        result.setId(entity.getId());
        result.setNickName(entity.getNickName());
        result.setUsername(entity.getUserName());
        result.setParentId(entity.getParentId());
        result.setType(entity.getType());
        return result;
    }

}

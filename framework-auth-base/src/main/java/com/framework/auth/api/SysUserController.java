package com.framework.auth.api;

import com.framework.auth.enums.ResultCodeEnum;
import com.framework.auth.pojo.dto.role.RoleInfoDTO;
import com.framework.auth.pojo.dto.user.UserInfoDTO;
import com.framework.auth.pojo.dto.user.UserInfoListDTO;
import com.framework.auth.pojo.dto.user.UserRoleInfoDTO;
import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.pojo.entity.UserEntity;
import com.framework.auth.pojo.vo.PageVo;
import com.framework.auth.pojo.vo.ResultVo;
import com.framework.auth.pojo.vo.ResultVo;
import com.framework.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

/**
 * 系统用户接口
 *
 * @author casper
 */
@RestController
@RequestMapping("/user")
@Api(description = "系统用户接口")
public class SysUserController {

    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);
    @Autowired
    private UserService userService;

    /**
     * 添加用户
     *
     * @param userInfoDTO 用户信息
     * @return 用户id
     */
    @PostMapping("register")
    @ApiOperation(value = "添加用户")
    public ResultVo<Integer> add(@Valid @RequestBody UserInfoDTO userInfoDTO, Errors errors) {
        if (StringUtils.isEmpty(userInfoDTO.getPassword())) {
            return ResultVo.build(ResultCodeEnum.PARAMETER_ERROR.getCode(), "密码不能为空");
        }
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(),ResultCodeEnum.SUCCESS.getMsg(),userService.saveOrUpdate(userInfoDTO));
    }

    /**
     * 更新用户信息
     *
     * @param userInfoDTO 用户信息
     * @return 用户id
     */
    @PutMapping("")
    @ApiOperation(value = "更新用户信息")
    public ResultVo<Integer> update(@RequestBody UserInfoDTO userInfoDTO, Errors errors) {
        /*校验参数*/
        if (userInfoDTO.getId() == null || userInfoDTO.getId() == 0) {
            return ResultVo.build(ResultCodeEnum.PARAMETER_ERROR.getCode(), "用户id不能为空或者0");
        }
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(),ResultCodeEnum.SUCCESS.getMsg(),userService.saveOrUpdate(userInfoDTO));
    }

    /**
     * 列表查询
     *
     * @return 列表数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public PageVo<UserInfoListDTO> list() {
        return PageVo.build(ResultCodeEnum.SUCCESS.getCode(),ResultCodeEnum.SUCCESS.getMsg(),0, 0, null);
    }

    /**
     * 查询用户详细信息
     *
     * @param id 用户id
     * @return 用户详情
     */
    @GetMapping()
    @ApiOperation(value = "查询用户详细信息")
    public ResultVo<UserInfoDTO> detail(@RequestParam Integer id) {
        UserEntity user = userService.getById(id);
        if (user == null) {
            return ResultVo.build(ResultCodeEnum.NOT_FOUND.getCode(), "用户不存在");
        }
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(),ResultCodeEnum.SUCCESS.getMsg(),UserInfoDTO.build(user));
    }

    /**
     * 重新绑定角色，旧的角色会清除
     *
     * @param userRoleInfoDTO 用户和角色信息
     * @return
     */
    @PostMapping("/role")
    @ApiOperation(value = "重新绑定角色，旧的角色会清除")
    public ResultVo<String> rebindingRoles(@RequestBody UserRoleInfoDTO userRoleInfoDTO, Errors errors) {
        userService.rebindingRoles(userRoleInfoDTO);
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(),ResultCodeEnum.SUCCESS.getMsg(),"success");
    }

    /**
     * 获取success用户的角色信息
     *
     * @param id 用户id
     * @return 角色列表
     */
    @GetMapping("/role")
    @ApiOperation(value = "获取用户的角色信息")
    public ResultVo<List<RoleInfoDTO>> roleInfo(@RequestParam Integer id) {
        UserEntity user = userService.getById(id);
        if (user == null) {
            return ResultVo.build(ResultCodeEnum.NOT_FOUND.getCode(), "用户不存在");
        }
        List<RoleEntity> roleEntities = userService.getUserRoles(id);
        List<RoleInfoDTO> result = new LinkedList<>();
        if (!CollectionUtils.isEmpty(roleEntities)) {
            for (RoleEntity roleEntity : roleEntities) {
                result.add(RoleInfoDTO.build(roleEntity));
            }
        }
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(),ResultCodeEnum.SUCCESS.getMsg(),result);
    }


}

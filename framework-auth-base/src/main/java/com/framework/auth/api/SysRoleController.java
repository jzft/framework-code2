package com.framework.auth.api;


import com.framework.auth.service.RoleService;
import com.framework.auth.enums.ResultCodeEnum;
import com.framework.auth.pojo.dto.role.RoleInfoDTO;
import com.framework.auth.pojo.dto.role.RolePermissionInfoDTO;
import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.pojo.vo.PageVo;
import com.framework.auth.pojo.vo.ResultVo;
import com.framework.auth.pojo.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

/**
 * 系统角色接口
 *
 * @author casper
 */
@RestController
@RequestMapping("/sys/role")
@Api(description = "系统角色接口")
public class SysRoleController {

    private static final Logger log = LoggerFactory.getLogger(SysRoleController.class);

    @Autowired
    private RoleService roleService;

    /**
     * 添加角色
     *
     * @param roleInfoDTO 角色信息
     * @return 角色id
     */
    @PostMapping()
    @ApiOperation(value = "添加角色")
    public ResultVo<Integer> add(@RequestBody RoleInfoDTO roleInfoDTO, Errors errors) {

        /*先判断名称有没有相同的存在数据库*/
        RoleEntity role = roleService.getByRoleName(roleInfoDTO.getRoleName());
        if (role != null) {
            return ResultVo.build(ResultCodeEnum.OPERATION_NOT_PERMIT.getCode(), String.format("角色[%s]已存在", roleInfoDTO.getRoleName()));
        }
        roleService.saveOrUpdate(roleInfoDTO);
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), roleService.saveOrUpdate(roleInfoDTO));
    }

    /**
     * 编辑角色
     *
     * @param roleInfoDTO 角色信息
     * @return 角色id
     */
    @PutMapping()
    @ApiOperation(value = "编辑角色")
    public ResultVo<Integer> update(@RequestBody RoleInfoDTO roleInfoDTO, Errors errors) {
        if (roleInfoDTO.getId() == null) {
            return ResultVo.build(ResultCodeEnum.PARAMETER_ERROR.getCode(), "");
        }
        /*先判断名称有没有相同的存在数据库*/
        RoleEntity role = roleService.getByRoleName(roleInfoDTO.getRoleName());
        if (role != null && !role.getId().equals(roleInfoDTO.getId())) {
            return ResultVo.build(ResultCodeEnum.OPERATION_NOT_PERMIT.getCode(), String.format("角色[%s]已存在", roleInfoDTO.getRoleName()));
        }
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), roleService.saveOrUpdate(roleInfoDTO));
    }

    /**
     * 分页查询角色信息
     *
     * @param index 页号
     * @param size  页大小
     * @return 角色列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页查询角色信息")
    public PageVo<RoleInfoDTO> list(int index, int size) {
        return PageVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), 0, 0, null);
    }


    /**
     * 查询角色详情
     *
     * @param roleId 角色id
     * @return 角色详情
     */
    @GetMapping()
    @ApiOperation(value = "查询角色详情")
    public ResultVo<RoleInfoDTO> detail(Integer roleId) {
        RoleEntity role = roleService.getById(roleId);
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), RoleInfoDTO.build(role));
    }

    /**
     * 重新绑定权限，旧权限会清除
     *
     * @param rolePermissionInfoDTO 角色和权限关系信息
     * @return
     */
    @PostMapping("/permission")
    @ApiOperation(value = "重新绑定权限，旧权限会清除")
    public ResultVo<String> rebindingPermission(@RequestBody RolePermissionInfoDTO rolePermissionInfoDTO, Errors errors) {

        roleService.rebindingPermissions(rolePermissionInfoDTO);
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), "success");
    }


    /**
     * 变更角色有效状态
     *
     * @param roleId    角色id
     * @param available 是否有效，1-有效，其他无效
     * @return
     */
    @PutMapping("/status")
    @ApiOperation(value = "变更角色有效状态")
    public ResultVo<String> updateRoleAvailableStatus(int roleId, int available) {
        RoleEntity roleEntity = roleService.getById(roleId);
        if(roleEntity == null){
            return ResultVo.build(ResultCodeEnum.ROLE_NOT_FOUND.getCode(),ResultCodeEnum.ROLE_NOT_FOUND.getMsg());
        }
        boolean isAvailable = available == 1;
        if(!roleEntity.getAvailable().equals(isAvailable)){
            roleEntity.setAvailable(isAvailable);
//            roleService.saveOrUpdate(roleEntity);
        }
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(),ResultCodeEnum.SUCCESS.getMsg());
    }


}

package com.framework.auth.api;


import com.framework.auth.enums.ResultCodeEnum;
import com.framework.auth.pojo.dto.permission.PermissionDetailInfoDTO;
import com.framework.auth.pojo.dto.permission.PermissionInfoDTO;
import com.framework.auth.pojo.dto.permission.PermissionListInfoDTO;
import com.framework.auth.pojo.entity.PermissionEntity;
import com.framework.auth.pojo.entity.PermissionEntityExample;
import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.pojo.entity.RolePermissionEntity;
import com.framework.auth.pojo.vo.BaseVo;
import com.framework.auth.pojo.vo.ResultVo;
import com.framework.auth.service.PermissionService;
import com.framework.auth.service.RolePermissionService;
import com.framework.auth.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统权限接口
 *
 * @author casper
 */
@RestController
@RequestMapping("/sys/perm")
@Api(description = "系统权限接口")
public class SysPermController {

    private static final Logger log = LoggerFactory.getLogger(SysPermController.class);

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RoleService roleService;


    /**
     * 添加权限
     *
     * @param permissionInfoDTO 权限信息
     * @return 权限id
     */
    @PostMapping()
    @ApiOperation(value = "添加权限")
    public ResultVo<Integer> add(@RequestBody PermissionInfoDTO permissionInfoDTO, Errors errors) {
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), permissionService.saveOrUpdate(permissionInfoDTO));
    }

    /**
     * 编辑权限
     *
     * @param permissionInfoDTO 权限信息
     * @return 权限id
     */
    @PutMapping()
    @ApiOperation(value = "编辑权限")
    public ResultVo<Integer> update(@RequestBody PermissionInfoDTO permissionInfoDTO, Errors errors) {
        if (permissionInfoDTO.getId() == null) {
            return ResultVo.build(ResultCodeEnum.PARAMETER_ERROR.getCode(), "权限id不能为空");
        }
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), permissionService.saveOrUpdate(permissionInfoDTO));
    }

    /**
     * 获取指定父级的子权限列表
     *
     * @param parentId  父权限id
     * @param available 是否有效,可不传,1-是，其余否
     * @return 权限列表
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父权限id", paramType = "int"),
            @ApiImplicitParam(name = "available", value = "是否有效,可不传,1-是，其余否", paramType = "int")
    })
    @GetMapping("/list")
    @ApiOperation(value = "获取指定父级的子权限列表")
    public ResultVo<List<PermissionListInfoDTO>> list(@RequestParam @NotNull Integer parentId, Integer available) {

        PermissionEntityExample example = new PermissionEntityExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        if (available != null) {
            example.createCriteria().andAvailableEqualTo(available == 1);
        }
        List<PermissionEntity> permissionEntities = permissionService.getListByExample(example);
        List<PermissionListInfoDTO> result = new ArrayList<>(permissionEntities.size());
        for (PermissionEntity entity : permissionEntities) {
            result.add(new PermissionListInfoDTO(entity));
        }
        return ResultVo.build(
                ResultCodeEnum.SUCCESS.getCode(),
                ResultCodeEnum.SUCCESS.getMsg(),
                result);
    }

    /**
     * 查询权限的详情
     *
     * @param id 权限id
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation(value = "查询权限的详情")
    public ResultVo<PermissionDetailInfoDTO> detail(@RequestParam int id) {

        PermissionEntity permissionEntity = permissionService.getById(id);
        if (permissionEntity == null) {
            return ResultVo.build(ResultCodeEnum.PERMISSION_NOT_FOUND.getCode(), ResultCodeEnum.PERMISSION_NOT_FOUND.getMsg());
        }
        PermissionDetailInfoDTO detailInfoDTO = new PermissionDetailInfoDTO(permissionEntity);
        /*查询关联的角色id*/
        List<RolePermissionEntity> rolePermissionList = rolePermissionService.getByPermissionId(detailInfoDTO.getId());
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), detailInfoDTO);
        }
        List<Integer> roleIds = rolePermissionList.stream().map(RolePermissionEntity::getRoleId).collect(Collectors.toList());
        /*查询角色信息*/
        List<RoleEntity> roleEntityList = roleService.getByRoleIds(roleIds);
        /*提取角色名称作为返回值*/
        List<String> roleNames = roleEntityList.stream().map(RoleEntity::getName).collect(Collectors.toList());
        detailInfoDTO.setRelativeRolesName(roleNames);
        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), detailInfoDTO);
    }


}

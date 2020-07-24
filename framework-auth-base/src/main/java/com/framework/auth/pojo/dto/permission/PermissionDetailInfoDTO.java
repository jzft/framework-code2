package com.framework.auth.pojo.dto.permission;

import com.framework.auth.pojo.entity.PermissionEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * date 2020/6/29 上午10:36
 *
 * @author casper
 **/
public class PermissionDetailInfoDTO extends PermissionInfoDTO {


    /**
     * 关联的角色的名称
     */
    private List<String> relativeRolesName;


    public PermissionDetailInfoDTO() {

    }

    public PermissionDetailInfoDTO(PermissionEntity entity) {
        super(entity);
    }

    public List<String> getRelativeRolesName() {
        return relativeRolesName;
    }

    public void setRelativeRolesName(List<String> relativeRolesName) {
        this.relativeRolesName = relativeRolesName;
    }
}

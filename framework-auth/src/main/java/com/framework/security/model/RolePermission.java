package com.framework.security.model;

/**
 * 
 * @ClassName:     RolePermission.java
 * @Description:   角色Permission实体类 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:06:14
 */
public class RolePermission extends BaseVO {
    private Integer id;

    private Integer rid;

    private Integer pid;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
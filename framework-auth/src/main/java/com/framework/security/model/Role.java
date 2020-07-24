package com.framework.security.model;

/**
 * 
 * @ClassName:     Role.java
 * @Description:   角色实体类 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:05:46
 */
public class Role extends BaseVO {
    private Integer id;

    private String name;
    
    private String roleCode;

    private Integer status = 1;

    private String type;
    
    /*private String uri;*/

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	/*public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}*/

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
}
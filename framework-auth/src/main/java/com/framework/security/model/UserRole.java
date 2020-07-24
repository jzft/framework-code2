package com.framework.security.model;

/**
 * 
 * @ClassName:     UserRole.java
 * @Description:   用户角色关联类 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:11:11
 */
public class UserRole extends BaseVO {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5913035402945964128L;

	private Integer id;

    private Integer uid;

    private Integer rid;

    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
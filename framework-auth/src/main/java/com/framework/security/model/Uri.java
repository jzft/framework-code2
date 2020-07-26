package com.framework.security.model;

/**
 * @author lyq
 * @version V1.0
 * @ClassName: Uri.java
 * @Description: 权限Uri类
 * @Date 2017年1月6日 上午9:08:07
 */
public class Uri {
    /**
     * id
     */
    private String id;

    /**
     * uri名称
     */
    private String name;
    /**
     *
     */
    private String uri;
    //uri类型
    private Integer type;

    private String perm;
    /**
     * 父级菜单
     */
    private Uri parentUri = null;

    private String icon;

    private Integer orderNum;

    private Integer menuType;

    private Boolean visible;

    private Boolean cache;


    public Uri() {

    }

    public Uri(String uri, Integer type) {
        this.uri = uri;
//		this.val = uri;
        this.type = type;
    }

    public Uri(String uri, Integer type, String name) {
        this.uri = uri;
//		this.val = uri;
        this.type = type;
        this.name = name;
    }

    public Uri(String id, String uri, Integer type, String name) {
        this.id = id;
        this.uri = uri;
//		this.val = uri;
        this.type = type;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
//		this.val = uri;
        this.uri = uri;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getParentUri() {
        return parentUri;
    }

    public void setParentUri(Uri parentUri) {
        this.parentUri = parentUri;
    }
//	public String getVal() {
//		return val;
//	}
//	public void setVal(String val) {
//		this.val = val;
//	}

    /**
     * @throws
     * @Title: load
     * @Description: permission加载
     * @param: @param permission
     * @return: void
     * @author lyq
     * @Date 2017年1月6日 上午9:09:13
     */
    public void load(Permission permission) {
//		this.setName(permission.getName());
//		this.setType(permission.getActionType());
//		this.setUri(permission.getUri());
//		this.setId(permission.getId()+"");
//        Permission parent = permission.getParent();
//        if(parent!=null){
//        	Uri parentUri = new Uri();
////        	parentUri.setName(parent.getName());
////        	parentUri.setType(parent.getActionType());
////        	parentUri.setUri(parent.getUri());
////        	parentUri.setId(parent.getId()+"");
//        	this.load(parent);
//        	this.setParentUri(parentUri);
//        }
        this.load(permission, this);
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    private void load(Permission permission, Uri uri) {
        uri.setName(permission.getName());
        uri.setType(permission.getMenuType());
        uri.setUri(permission.getUri());
        uri.setId(permission.getId().toString());
        uri.setPerm(permission.getPerm());
        uri.setCache(permission.getCache());
        uri.setIcon(permission.getIcon());
        uri.setMenuType(permission.getMenuType());
        uri.setOrderNum(permission.getOrderNum());
        uri.setVisible(permission.getVisible());
        Permission parent = permission.getParent();
        if (parent != null) {
            Uri parentUri = new Uri();
//        	parentUri.setName(parent.getName());
//        	parentUri.setType(parent.getActionType());
//        	parentUri.setUri(parent.getUri());
//        	parentUri.setId(parent.getId()+"");
            this.load(parent, parentUri);
            uri.setParentUri(parentUri);
        }
    }

}
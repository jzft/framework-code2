package com.framework.auth.enums;

/**
 * @author casper
 * @date 2020/6/18 下午5:25
 **/
public enum ResultCodeEnum {
    SUCCESS(1, "success"),
    PARAMETER_ERROR(-1, "parameter error"),
    NOT_FOUND(4404, "resource not found"),
    OPERATION_NOT_PERMIT(4403, "operation not permit"),

    /*==============权限===================*/
    PERMISSION_NOT_FOUND(4403,"权限不存在"),

    /*==============角色===================*/
    ROLE_NOT_FOUND(4403,"角色不存在"),
    
    /** 失败 */
    FAIL(-1,"失败"),
    LOGIN_FAIL(2,"登录失败"),

    /** 未登录 */
    UNAUTHEN (4401,"未登录"),

    /** 未授权，拒绝访问 */
    UNAUTHZ(4403,"未授权，拒绝访问"),

    /** session超时退出了登录 */
    SESSION_TIMOUT(4433,"登录超时"),

    /** 服务端异常 */
    SERVER_ERR(5500,"服务端异常 ");




    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态信息
     */
    private final String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

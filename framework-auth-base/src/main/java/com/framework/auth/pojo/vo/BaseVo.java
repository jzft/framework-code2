package com.framework.auth.pojo.vo;

import com.framework.auth.enums.ResultCodeEnum;

import io.swagger.annotations.ApiModelProperty;

/**
 * date 2020/6/10 下午12:46
 *
 * @author casper
 **/
public class BaseVo {

	
	private boolean succ = true;
    /**
     * 响应代码
     */
    @ApiModelProperty(name = "响应代码")
    private int code;

    /**
     * 响应信息
     */
    @ApiModelProperty(name = "响应信息")
    private String msg;

 
    public BaseVo() {
    }

    public BaseVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }



   public void setResultCodeEnum(ResultCodeEnum codeEnum){
	   this.code = codeEnum.getCode();
	   this.msg = codeEnum.getMsg();
   }

    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

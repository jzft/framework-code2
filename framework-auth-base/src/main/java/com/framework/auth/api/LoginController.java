package com.framework.auth.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.framework.auth.enums.ResultCodeEnum;
import com.framework.auth.pojo.vo.BaseVo;
import com.framework.auth.pojo.vo.LogoutVo;
import com.framework.auth.pojo.vo.UserInfoVo;
import com.framework.security.constant.AuthConstant;
import com.framework.security.model.User;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.utils.CaptchaUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/login")
@Api(description = "权限")
public class LoginController {

	@Value("${shiro.captchaSessionKey}")  
	private String captchaSessionKey = null;
    /**
     * 登出重定向
     *
     * @return
     */
//    @GetMapping("/toLogout")
	
	@ResponseBody
	@RequestMapping(value = "/toLogout")
    @ApiOperation(value = "登出重定向")
    public BaseVo toLogout() {
		BaseVo vo = new BaseVo();
		vo.setResultCodeEnum(ResultCodeEnum.UNAUTHEN);
        return vo;
    }

    /**
     * 登录失败重定向
     *
     * @return
     */
	
//    @GetMapping("/login")
	@ResponseBody
    @RequestMapping(value = "/login")
    @ApiOperation(value = "登录失败重定向")
    public BaseVo toLogin(HttpServletRequest request) {
		BaseVo vo = new BaseVo();
		String errorFail = (String)request.getAttribute(AuthConstant.AUTHENTICATIONEXCEPTION);
		if(StringUtils.isEmpty(errorFail)){
        vo.setMsg("请重新登录");
		}else{
			vo.setMsg(errorFail);
		}
        vo.setCode(ResultCodeEnum.FAIL.getCode());
        return vo;
    }

//    @GetMapping("/info")
	@ResponseBody
    @RequestMapping(value = "/info")
    @ApiOperation(value = "获取当前登录用户的信息")
    public UserInfoVo info() {
        UserInfoVo vo = new UserInfoVo();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute(AuthConstant.USER);
        vo.setRoles(user.getRoles());
        vo.setName(user.getUserName());
        vo.setNick(user.getNickName());
        try {
            vo.setPerms(user.getRoleUris());
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        vo.setResultCodeEnum( ResultCodeEnum.SUCCESS);
        return vo;
//        return ResultVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), vo);
    }


    /**
     * 无权限重定向
     *
     * @return
     */
//    @GetMapping("/notauth")
	@ResponseBody
    @RequestMapping(value = "notauth")
    @ApiOperation(value = "无权限重定向")
    public LogoutVo notauth() {
        LogoutVo vo = new LogoutVo();
        vo.setMsg(ResultCodeEnum.UNAUTHZ.getMsg());
        vo.setCode(ResultCodeEnum.UNAUTHZ.getCode());
        return vo;
//        return BaseVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), vo);
    }

    /**
     * 登出接口
     *
     * @return
     */
//    @GetMapping("/logout")
	@ResponseBody
    @RequestMapping(value = "logout")
    @ApiOperation(value = "登出接口")
    public LogoutVo logout() {
        LogoutVo vo = new LogoutVo();
        vo.setMsg(ResultCodeEnum.UNAUTHEN.getMsg());
        vo.setCode(ResultCodeEnum.UNAUTHEN.getCode());
        return vo;
//        return BaseVo.build(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(), vo);
    }

	@RequestMapping("/captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
	    GifCaptcha gifCaptcha = new GifCaptcha(130,48,4);
	    CaptchaUtil.out(gifCaptcha, request, response);
	    String verCode = gifCaptcha.text().toLowerCase();
	    request.getSession().setAttribute(captchaSessionKey,verCode);  //存入session 
	}
}
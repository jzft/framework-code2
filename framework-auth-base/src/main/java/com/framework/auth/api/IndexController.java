package com.framework.auth.api;


import com.framework.auth.pojo.vo.LoginVo;
import com.framework.auth.enums.ResultCodeEnum;
import com.framework.auth.pojo.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/index")
@Api(description = "index")
public class IndexController {

    /**
     * shiro验证成功后重定向的接口
     *
     * @param request
     * @param response
     * @return
     */
//    @GetMapping(value = "/index")
	@ResponseBody
    @RequestMapping(value = "/index")
    @ApiOperation(value = "shiro验证成功后重定向的接口")
    public LoginVo login(HttpServletRequest request, HttpServletResponse response) {
        LoginVo vo = new LoginVo();
        vo.setToken(SecurityUtils.getSubject().getSession().getId() + "");
        vo.setCode(ResultCodeEnum.SUCCESS.getCode());
        vo.setMsg(ResultCodeEnum.SUCCESS.getMsg());
        return vo;
    }
}
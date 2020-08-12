package com.framework.security;

import com.framework.cache.RedisHelper;
import com.framework.security.exception.MyAuthenticationException;
import com.framework.security.model.*;
import com.framework.security.service.SecurityPermissionService;
import com.framework.security.service.SecurityRoleService;
import com.framework.security.service.SecurityUserService;
import com.framework.spring.utils.SpringContextUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyq
 * @version V1.0
 * @ClassName: CustomAuthorizingRealm.java
 * @Description: 授权类
 * @Date 2017年1月5日 下午4:08:58
 */
public class CustomAuthorizingRealm extends AuthorizingRealm implements LoadAuthorization, Realm, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthorizingRealm.class);

    private boolean isPersistCache = false;
   

    //	   private boolean zkEnabled  = false;
    public CustomAuthorizingRealm() {
        super();
        //是否缓存权限一定要设置成false，这样登录的时候都会执行加载权限的方法
        this.setAuthorizationCachingEnabled(true);
        this.setAuthenticationCachingEnabled(true);
    }

    /*LoaderCacheManager cacheManager = null;*/

    @Autowired
    private SecurityUserService userService;
    @Autowired
    private SecurityRoleService roleService;
    @Autowired
    private SecurityPermissionService permissionService;


    /**
     * @throws
     * @Title: doGetAuthorizationInfo
     * @Description: 获取权限
     * @param: @param principals
     * @param: @return
     * @author lyq
     * @Date 2017年1月5日 下午4:09:37
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //logger.info("授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用");
        String username = (String) super.getAvailablePrincipal(principals);
        AuthorizationInfo info = doGetAuthorizationInfo(username);
        return info;
    }


    private AuthorizationInfo doGetAuthorizationInfo(String username) {
        AuthorizationInfo info = doGetAuthorizationInfo(username, true);
        return info;
    }

    private AuthorizationInfo doGetAuthorizationInfo(String username, boolean isLogin) {
        AuthorizationInfo info = null;
        if (isLogin) {
            info = load(username);
//			if(zkEnabled){
            ZkPromulgator zkWatcher = (ZkPromulgator) SpringContextUtil.getBean(ZkPromulgator.class, "zkRoleUriUpdateWatcher");
            if (zkWatcher.isZkEnabled()) {
                try {
                    //通知其他集群中的应用
                    zkWatcher.publish(username);
                } catch (Exception e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }
//			}

        }
        return info;
    }

    /**
     * @throws
     * @Title: load
     * @Description: 加载用户权限
     * @param: @param username
     * @param: @return
     * @author lyq
     * @Date 2017年1月5日 下午4:09:54
     */
    @Override
    public AuthorizationInfo load(String username) {
        Subject subject = SecurityUtils.getSubject();
        User user = null;
        List<Role> roles = null;
        user = (User) subject.getSession().getAttribute("user");
        if (user != null) {
            roles = user.getRoles();
        }
        if (user == null) {
            user = userService.findUserByUserNum(username);
            Integer userId = user.getId();
            if (user != null) {
                roles = roleService.queryRolesByUserId(userId);
                user.setRoles(roles);
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();


        List<Integer> roleIds = new ArrayList<Integer>();
        for (Role role : roles) {
            info.addRole(role.getName());
            roleIds.add(role.getId());
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(role.getId());
            List<Permission> perms = permissionService.queryPermissionByRoleId(list);
            if (!this.isPersistCache) {
                SecurityFilterUtil.delFilterRoleUri(role.getName());
            }
            if (CollectionUtils.isNotEmpty(perms)) {

                for (Permission permission : perms) {
                    RoleUri roleRri = new RoleUri();
                    roleRri.setRoleCode(role.getName());
                    Uri uri = new Uri();
                    uri.load(permission);
                    roleRri.setUri(uri);
                    try {
                        SecurityFilterUtil.addFilterRoleBean(roleRri);
                    } catch (Exception e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            //通过shiro拦截工厂的代理来修改工厂的重新生成拦截配置
            new ShiroFilterFactoryBeanProxy(SecurityFilterUtil.getShiroFilter()).reloadFilterChainResolver();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    @Autowired
    AESHashedMatcher hashedCredentialsMatcher;

    /**
     * @throws
     * @Title: doGetAuthenticationInfo
     * @Description: 登录认证
     * @param: @param token
     * @param: @return
     * @param: @throws MyAuthenticationException
     * @author lyq
     * @Date 2017年1月5日 下午4:10:21
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws MyAuthenticationException {
       
    	UsernamePasswordToken upt = (UsernamePasswordToken) token;
        AuthenticationInfo info = null;
        logger.info("doGetAuthenticationInfo start...");
        Subject subject = SecurityUtils.getSubject();
        
        if(token instanceof CaptchaToken){
        	CaptchaToken captchaToken = ((CaptchaToken) token);
        	String sessionCaptcha = (String)subject.getSession().getAttribute(captchaToken.getCaptchaSessionKey());
        	if(StringUtils.isEmpty(captchaToken.getCaptcha())||StringUtils.isEmpty(sessionCaptcha)||!StringUtils.equals(captchaToken.getCaptcha(), sessionCaptcha)){
        		//图形验证码不争取
        		throw new  MyAuthenticationException("验证码不正确。");
        	}
        }
        logger.info("doGetAuthenticationInfo subject.getPrincipal():" + subject.getPrincipal() + "。。。");

        String principal = upt.getUsername();
        // upt.setPassword("admin".toCharArray());
        logger.info("doGetAuthenticationInfo user:" + principal + "。。。");
        User user = userService.findUserByUserNum(principal);
        if (user == null) {
            throw new MyAuthenticationException("未知的用户名或密码错误！");
        }
        

        // String randompwdattr=SecurityUtils.getSubject().getSession().getAttribute("RANDOMPWDATTR")+"";
        String randompwdattr = RedisHelper.getStr("RANDOMPWDATTR:" + principal);
        if (StringUtils.isNotBlank(randompwdattr)) {
            ServletRequest request = ((WebSubject) SecurityUtils.getSubject()).getServletRequest();
            String pwd = new String(upt.getPassword());
            String sessionKey = request.getParameter("sessionKey");//微信
            String msgCode = request.getParameter("msgCode");//手机短信
            // ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();
            if (pwd != null && (randompwdattr.equals(sessionKey) || randompwdattr.equals(msgCode))) {
                try {
                    randompwdattr = hashedCredentialsMatcher.aesEncrypt(randompwdattr, hashedCredentialsMatcher.getEncryptKey());
                    user.setPwd(randompwdattr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        User sessionUser = new UserBuilder().id(user.getId()).status(user.getStatus())
                .nickName(user.getNickName()).userName(user.getUserName()).build();
        if (sessionUser.getStatus() < EnumStatus.VALID.getStatus()) {
            throw new AuthorizationException("您的帐号号已被限制登录，如需帮助请联系管理员！");
        }

        List<Role> roles = roleService.queryRolesByUserId(sessionUser.getId());
        sessionUser.setRoles(roles);

        subject.getSession().setAttribute("user", sessionUser);
        this.doGetAuthorizationInfo(principal);
        logger.info("doGetAuthenticationInfo end1...");
        info = new SimpleAuthenticationInfo(sessionUser.getUserName(), user.getPwd(), getName());
        logger.info("doGetAuthenticationInfo end2...");
        return info;
    }

    /**
     * @throws
     * @Title: clearCachedAuthorizationInfo
     * @Description: 清除用户授权信息缓存.
     * @param: @param principal
     * @return: void
     * @author lyq
     * @Date 2017年1月5日 下午4:10:35
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        super.clearCachedAuthorizationInfo(principals);
        super.clearCache(principals);
        super.clearCachedAuthenticationInfo(principals);
    }

    /**
     * @throws
     * @Title: hasRole
     * @Descriptiokn: 重写判断角色的方式
     * @param: @param roleIdentifier
     * @param: @param info
     * @param: @return
     * @author lyq
     * @Date 2017年1月5日 下午4:10:49
     */
    @Override
    protected boolean hasRole(String roleIdentifier, AuthorizationInfo info) {
        if (roleIdentifier != null) {
            String[] roles = StringUtils.split(roleIdentifier, "|");
            for (String roleId : roles) {
                boolean bool = info != null && info.getRoles() != null && info.getRoles().contains(StringUtils.trim(roleId));
                if (bool) {
                    return bool;
                }
            }
        }
        return info != null && info.getRoles() != null && info.getRoles().contains(roleIdentifier);
    }

    /**
     * @throws
     * @Title: clearAllCachedAuthorizationInfo
     * @Description: 清除所有用户授权信息缓存.
     * @param:
     * @return: void
     * @author lyq
     * @Date 2017年1月5日 下午4:11:03
     */
    public void clearAllCachedAuthorizationInfo() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


//		public boolean isZkEnabled() {
//			return zkEnabled;
//		}
//
//
//		public void setZkEnabled(boolean zkEnabled) {
//			this.zkEnabled = zkEnabled;
//		}


    public void setIsPersistCache(boolean isPersistCache) {
        this.isPersistCache = isPersistCache;
        this.setAuthorizationCachingEnabled(isPersistCache);
        this.setAuthenticationCachingEnabled(isPersistCache);
    }


	/**
     * 是否是Ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            //LoggerUtils.debug(CLAZZ, "当前请求为Ajax请求");
            return Boolean.TRUE;
        }
        //LoggerUtils.debug(CLAZZ, "当前请求非Ajax请求");
        return Boolean.FALSE;
    }
}
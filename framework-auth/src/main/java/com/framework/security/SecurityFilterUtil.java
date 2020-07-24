package com.framework.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.mgt.NamedFilterList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.framework.SpringContextUtil;
import com.framework.security.model.RoleUri;
import com.framework.security.model.Uri;
import com.framework.security.service.SecurityRoleService;


/**
 * @author Administrator
 * @version V1.0
 * @ClassName: SecurityFilterUtil.java
 * @Description: 安全过滤器
 * @Date 2017年1月5日 下午5:02:20
 */
public class SecurityFilterUtil {

    static String REDIS_ROLE_URIS = "role_uris:";
    static Integer REDIS_ROLE_URIS_EXPIRESECONDS = 24 * 60 * 60;
    public static RoleUriMap roleUriMap = new RoleUriMap();
    static Map<String, NamedFilterList> filterChains = null;

    /**
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @throws
     * @Title: addFilterRoleBean
     * @Description: 往shiro拦截器里面增加角色
     * @param: @param roleRri
     * @return: void
     * @author join
     * @Date 2017年1月5日 下午5:01:39
     */
    public static void addFilterRoleBean(RoleUri roleRri) throws JsonParseException, JsonMappingException, IOException {
        String uri = StringUtils.trim(roleRri.getUri().getUri());
        String roleCode = StringUtils.trim(roleRri.getRoleCode());
        try {
            if (StringUtils.isEmpty(uri) || StringUtils.isEmpty(roleCode)) {
                throw new RuntimeException("角色 uri 跟 roleRole 不能为空 ;uri:" + uri + ";roleCode:" + roleCode);
            }
            String filterAuthc = getFilterChainDefinitionMap().get(uri);
            //角色rolesStr格式：roles[role1 | role2]
            if (StringUtils.isEmpty(filterAuthc)) {
                getFilterChainDefinitionMap().put(uri, "roles[" + roleCode + "]");
                putRoleUriMap(roleRri);
                return;
            }
            if (getFilterChainDefinitionMap().containsKey(uri)) {
                if (StringUtils.startsWith(filterAuthc, "roles")) {
                    int strLength = StringUtils.length(StringUtils.trim(filterAuthc));
                    if (strLength > 0 && strLength < 2) {
                        throw new RuntimeException("角色加载错误:" + filterAuthc);
                    }
                    String filterRoles = StringUtils.trim(StringUtils.substringBetween(filterAuthc, "[", "]"));
                    String otherAuthc = StringUtils.trim(StringUtils.substringAfter(filterAuthc, "]"));
                    String roleCodeStr = roleCode;
                    if (StringUtils.isNotEmpty(filterRoles)) {
                        //uri中拦截器已存在的角色
                        String[] fiterRoleArr = StringUtils.split(filterRoles, "|");
                        //是否增加角色
                        boolean addRoleBool = true;
                        for (String roleStr : fiterRoleArr) {
                            if (StringUtils.equals(roleCode, StringUtils.trim(roleStr))) {
                                addRoleBool = false;
                                break;
                            }
                        }
                        if (addRoleBool) {
                            roleCodeStr = filterRoles + "|" + roleCode;
                            getFilterChainDefinitionMap().put(uri, "roles[" + roleCodeStr + "]");
                            putRoleUriMap(roleRri);
                        }
                    } else {
                        getFilterChainDefinitionMap().put(uri, "roles[" + roleCodeStr + "]");
                        putRoleUriMap(roleRri);
                    }
                    if (StringUtils.isNoneEmpty(otherAuthc)) {
                        getFilterChainDefinitionMap().put(uri, "roles[" + roleCodeStr + "]," + filterAuthc);
                        putRoleUriMap(roleRri);
                    }
                } else {
                    getFilterChainDefinitionMap().put(uri, "roles[" + roleCode + "]," + filterAuthc);
                    putRoleUriMap(roleRri);
                }
            } else {
                getFilterChainDefinitionMap().put(uri, "roles[" + roleCode + "]");
                putRoleUriMap(roleRri);
            }
        } finally {

        }

    }

    /**
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @throws
     * @Title: delFilterRoleUri
     * @Description: 删除拦截器中uri的角色
     * @param: @param roleCode
     * @param: @param uri
     * @return: void
     * @author join
     * @Date 2017年1月5日 下午5:02:56
     */
    public static void delFilterRoleUri(String roleCode, String uri) throws JsonParseException, JsonMappingException, IOException {
        roleCode = StringUtils.trim(roleCode);
        uri = StringUtils.trim(uri);
        if (containRoleUriMap(roleCode, uri)) {
            Map<String, Uri> uris = roleUriMap.get(roleCode);
            uris.remove(uri);
            roleUriMap.put(roleCode, uris);
            String filterAuthc = getFilterChainDefinitionMap().get(uri);
            int strLength = StringUtils.length(StringUtils.trim(filterAuthc));
            if (StringUtils.isEmpty(StringUtils.trim(filterAuthc))) {
                return;
            }
            if (strLength > 0 && strLength < 2) {
                throw new RuntimeException("角色加载错误:" + filterAuthc);
            }
            String filterRoles = StringUtils.trim(StringUtils.substringBetween(filterAuthc, "[", "]"));
            String otherAuthc = StringUtils.trim(StringUtils.remove(StringUtils.substringAfterLast(filterAuthc, "]"), ","));
            if (StringUtils.isEmpty(StringUtils.trim(filterRoles))) {
                return;
            } else {

                String[] fiterRoleArr = StringUtils.split(filterRoles, "|");
                String newFilterRole = "";
                for (String role : fiterRoleArr) {
                    if (!StringUtils.equals(roleCode, StringUtils.trim(role))) {
                        if (StringUtils.isEmpty(newFilterRole)) {
                            newFilterRole = role;
                        } else {
                            newFilterRole = newFilterRole + "|" + role;
                        }
                    }
                }
                if (StringUtils.isEmpty(newFilterRole)) {
                    getFilterChainDefinitionMap().remove(uri);
                    if (StringUtils.isNoneEmpty(otherAuthc)) {
                        getFilterChainDefinitionMap().put(uri, otherAuthc);
                    }
                } else {

                    newFilterRole = "roles[" + newFilterRole + "]";
                    if (StringUtils.isNoneEmpty(otherAuthc)) {
                        newFilterRole = newFilterRole + "," + otherAuthc;
                    }
                    getFilterChainDefinitionMap().put(uri, newFilterRole);
                }
            }
        }
    }

    public static void delFilterRoleUri(String roleCode) {
        try {
            Map<String, Uri> urls = roleUriMap.get(roleCode);

            if (MapUtils.isNotEmpty(urls)) {
                urls.forEach((url, v) -> {
                    try {
                        delFilterRoleUri(roleCode, url);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		roleUriMap.put(roleCode, new HashMap<>());
    }

    /**
     * @throws
     * @Title: getFilterChainDefinitionMap
     * @Description: 获取shiro拦截器中的角色url配置
     * @param: @return
     * @return: Map<String, String>
     * @author join
     * @Date 2017年1月5日 下午5:03:11
     */
    public static Map<String, String> getFilterChainDefinitionMap() {
        return getShiroFilter().getFilterChainDefinitionMap();
    }

    private static Boolean putRoleUriMap(RoleUri roleUri) throws JsonParseException, JsonMappingException, IOException {
        String roleCode = roleUri.getRoleCode();
        String uri = roleUri.getUri().getUri();
        Map<String, Uri> uris = roleUriMap.get(StringUtils.trim(roleCode));
        if (uris == null) {
            uris = new HashMap<String, Uri>();
            roleUriMap.put(roleCode, uris);
        }
        if (uris.containsKey(StringUtils.trim(uri))) {
            return false;
        } else {
            uris.put(uri, roleUri.getUri());
            roleUriMap.put(roleCode, uris);
            return true;
        }
    }

    /**
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @throws
     * @Title: containRoleUriMap
     * @Description: 拦截器中角色roleCode是否存在uri
     * @param: @param roleCode
     * @param: @param uri
     * @param: @return
     * @return: boolean
     * @author join
     * @Date 2017年1月5日 下午5:03:28
     */
    public static boolean containRoleUriMap(String roleCode, String uri) throws JsonParseException, JsonMappingException, IOException {
        roleCode = StringUtils.trim(roleCode);
        if (roleCode == null) {
            return false;
        }
        Map<String, Uri> uris = roleUriMap.get(roleCode);
        if (uris == null) {
            return false;
        }
        return uris.containsKey(StringUtils.trim(uri));
    }

    public static ShiroFilterFactoryBean getShiroFilter() {
        ShiroFilterFactoryBean factory = ShiroFilterFactoryBean.instance;
        return factory;
    }


    private static SecurityRoleService getRoleService() {
        return SpringContextUtil.getBean(SecurityRoleService.class);
    }

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		/*RedisTemplate t = new RedisTemplate();
		org.springframework.data.redis.connection.jedis.JedisConnectionFactory connectionFactory = new org.springframework.data.redis.connection.jedis.JedisConnectionFactory();
		
		InputStream is = SecurityFilterUtil.class.getClassLoader().getResourceAsStream("hostinfo.properties");
		Properties prop = new Properties();
		String hostname = "";
		String password = "";
		String port = "";
		try {
			prop.load(is);
			hostname = prop.getProperty("hostinfo.hostname");
			System.out.println("hostname:"+hostname);
			password = prop.getProperty("hostinfo.password");
			System.out.println("password:"+password);
			port = prop.getProperty("hostinfo.port");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		connectionFactory.setPassword(password);
		connectionFactory.setHostName(hostname);
		connectionFactory.setPort(Integer.parseInt(port));
		
		connectionFactory.afterPropertiesSet();
		t.setConnectionFactory(connectionFactory );
		connectionFactory.setPoolConfig(new redis.clients.jedis.JedisPoolConfig());
		t.afterPropertiesSet();
		RedisHelper.redisTemplate = t;
		
		new RoleUriMap().put("admin", new Uri("/system/*",2));
		Map<String,Uri> hhe = new RoleUriMap().get("admin");
		Object aa = hhe.get("/system/*");
		System.out.println(hhe.get("/system/*").getUri());*/
    }

}

package org.cloudxue.springcloud.common.context;

import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.common.dto.UserDTO;
import org.cloudxue.springcloud.common.util.JsonUtil;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @ClassName SessionHolder
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/25 下午5:03
 * @Version 1.0
 **/
public class SessionHolder {
    //session id 本地线程变量
    private static final ThreadLocal<String> sidLocal = new NamedThreadLocal<>("sidLocal");
    //session id store prefix 本地线程变量
    private static final ThreadLocal<String> sessionIDStore = new NamedThreadLocal<>("sessionIDStore");
    //session userIdentifier(这里为 user id) 本地线程变量
    private static final ThreadLocal<String> userIdentifierLocal = new NamedThreadLocal<>("userIdentiferLocal");
    //session 用户信息 本地线程变量
    private static final ThreadLocal<UserDTO> sessionUserLocal = new NamedThreadLocal<>("sessionUserLocal");
    //session 本地线程变量
    private static final ThreadLocal<HttpSession> sessionLocal = new NamedThreadLocal<>("sessionLocal");
    //request 本地线程变量
    private static final ThreadLocal<HttpServletRequest> requestLocal = new NamedThreadLocal<>("requestLocal");

    public static final String G_USER = "USER-CACHE";

    /**
     * 保存Request
     * @param request
     */
    public static void setRequest(HttpServletRequest request) {
        if (null != requestLocal.get()) {
            requestLocal.remove();
        }

        requestLocal.set(request);
    }

    /**
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = requestLocal.get();
        Assert.notNull(request, "request 未设置");
        return request;
    }

    public static void setSession(HttpSession session) {
        sessionLocal.set(session);
    }

    public static HttpSession getSession () {
        HttpSession session = sessionLocal.get();
        Assert.notNull(session, "session 未设置");
        return session;
    }

    public static UserDTO getSessionUser () {
        UserDTO userDTO = sessionUserLocal.get();
        if (null == userDTO) {
            HttpSession session = getSession();
            String valueString = (String) session.getAttribute(G_USER);
            userDTO = JsonUtil.jsonToPojo(valueString, UserDTO.class);
            sessionUserLocal.set(userDTO);
        }
        Assert.notNull(userDTO, "session 未设置");
        return userDTO;
    }

    /**
     * 清除线程局部变量
     */
    public static void cleanData() {
        sessionUserLocal.remove();
        sessionLocal.remove();
        sessionIDStore.remove();
        userIdentifierLocal.remove();
        requestLocal.remove();
        sidLocal.remove();
    }

    /**
     * 获取session 中的userid
     *
     * @return userid
     */
    public static String getUserId() {
        UserDTO userDTO = getSessionUser();

        Assert.notNull(userDTO, "session user is null");

        return String.valueOf(userDTO.getUserId());
    }


    /**
     * 获取 session id 的存储前缀
     *
     * @return getSessionIDStore
     */
    public static String getSessionIDStore() {
        if (null != sessionIDStore.get()) {
            return sessionIDStore.get();
        }
        return SessionConstants.SESSION_STORE;
    }

    public static void setSessionIDStore(String type) {
        sessionIDStore.set(type);
    }


    public static String getAuthHeader() {
        return SessionConstants.AUTHORIZATION_HEAD;
    }

    public static void put(String key, String s) {
        getSession().setAttribute(key, s);
    }

    public static String get(String key) {
        return (String) getSession().getAttribute(key);
    }



    public static String getUserIdentifier() {
        return userIdentifierLocal.get();
    }

    public static void setUserIdentifier(String userIdentifier) {
        userIdentifierLocal.set(userIdentifier);
    }

    public static String getSid() {
        return sidLocal.get();
    }

    public static void setSid(String sid) {
        sidLocal.set(sid);
    }
}

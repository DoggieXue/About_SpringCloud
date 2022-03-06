package org.cloudxue.springcloud.base.core;

import lombok.Data;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.common.context.SessionHolder;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName CustomedSessionIdResolver
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/2/7 下午10:03
 * @Version 1.0
 **/
@Data
public class CustomedSessionIdResolver implements HttpSessionIdResolver {

    private static final String HEADER_AUTHENTICATION_INFO = "Authentication-Info";

    /**
     * The nickname of the header to obtain the session id from.
     */
    public CustomedSessionIdResolver() {
    }

    /**
     * 查找 session id，找到的id 必须在 redis 中存在
     * @param request 请求
     * @return  session id 列表
     */
    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        String sid = SessionHolder.getSid();
        return (sid != null) ? Collections.singletonList(sid) : Collections.emptyList();
    }

    @Override
    public void setSessionId(HttpServletRequest request,
                             HttpServletResponse response,
                             String sessionId) {
        //不需要返回sessionId到前端
        response.setHeader(SessionConstants.USER_IDENTIFIER, "" );
        //response.setHeader(this.headerName, sessionId);
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(SessionConstants.USER_IDENTIFIER, "" );
    }
}

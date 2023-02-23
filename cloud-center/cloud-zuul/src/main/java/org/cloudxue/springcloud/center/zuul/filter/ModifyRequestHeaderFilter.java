package org.cloudxue.springcloud.center.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ModifyRequestHeaderFilter
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/27 上午11:13
 * @Version 1.0
 **/
@Component
@Slf4j
public class ModifyRequestHeaderFilter extends ZuulFilter {

    /**
     * 根据条件去判断是否需要路由，是否需要执行该过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        /**
         * 如果请求已经被其他过滤器终止，则本过滤器也不做处理
         */
        if (!ctx.sendZuulResponse()) {
            return false;
        }
        /**
         * 存在用户端认证令牌
         */
        String token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);
        if (!StringUtils.isEmpty(token)) {
            return true;
        }

        /**
         * 存在管理端认证令牌
         */
        token = request.getHeader(SessionConstants.ADMIN_AUTHORIZATION_HEAD);
        if (!StringUtils.isEmpty(token)) {
            return false;
        }
        return false;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 调用上游微服务之前，修改请求头 加上USER-ID属性
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        //认证成功，请求的 USER-ID 属性会被设置到请求头中
        String identifier = (String)request.getAttribute(SessionConstants.USER_IDENTIFIER);
        if (StringUtils.isNotBlank(identifier)) {
            ctx.addZuulRequestHeader(SessionConstants.USER_IDENTIFIER, identifier);
        }
        return null;
    }
}

package org.cloudxue.springcloud.center.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName DemoFilter
 * @Description 自定义过滤器：黑名单过滤出
 * @Author xuexiao
 * @Date 2022/1/23 上午11:47
 * @Version 1.0
 **/
@Slf4j
public class DemoFilter extends ZuulFilter {

    /**
     * 实例所用黑名单：实际场景需要从数据库或其他来源获取
     */
    static final List<String> blackList = Arrays.asList("foo","bar","test");

    /**
     * 过滤的执行类型
     * @return
     */
    @Override
    public String filterType() {
        /**
         * pre:路由前
         * route:路由中
         * post:路由后
         * error:异常时
         */
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 过滤的执行次序：值越小，优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 返回过滤器是否生效的boolean值，返回true表示生效，返回false表示不生效。
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        /**
         * 如果请求已经被其他过滤器终止，本过滤器就不处理
         */
        if (!ctx.sendZuulResponse()) {
            return false;
        }
        /**
         * 获取请求
         */
        HttpServletRequest request = ctx.getRequest();
        /**
         * 返回true，表示需要执行过滤器的run方法
         */
        if (request.getRequestURI().startsWith("/ZuulFilter/demo")) {
            return true;
        }
        /**
         * 返回false，表示跳过此过滤器，不执行run方法
         */
        return false;
    }

    /**
     * 具体过滤逻辑
     * 通过请求中的用户名参数，判断是否在黑名单中
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String username = request.getParameter("username");
        if (null != username && blackList.contains(username)) {
            log.info(username + " is forbidden: " + request.getRequestURL().toString());
            /**
             * 终止后续访问流程
             */
            ctx.setSendZuulResponse(false);

            try {
                ctx.getResponse().setContentType("text/html;charset=utf-8");
                ctx.getResponse().getWriter().write("对不起，您已经进入黑名单");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}

package org.cloudxue.springcloud.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName BusinessException
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/26 下午2:32
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * 默认错误编码
     */
    private static final int DEFAULT_CODE = -1;
    /**
     * 默认错误提示
     */
    private static final String DEFAULT_MSG = "业务异常";

    @Builder.Default
    private int errCode = DEFAULT_CODE;
    @Builder.Default
    private String errMsg = DEFAULT_MSG;

    public BusinessException() {
        super(DEFAULT_MSG);
    }

    public BusinessException(String errMsg) {
        super(errMsg);
    }

    public BusinessException(String errMsg, Throwable e) {
        super(errMsg, e);
    }

    public BusinessException(Throwable e) {
        super(e);
    }

    /**
     * 带格式设置异常消息
     * @param format 格式
     * @param objects 替换的对象
     * @return
     */
    public BusinessException setDetail(String format, Object... objects) {
        format = StringUtils.replace(format, "{}", "%s");
        this.errMsg = String.format(format, objects);
        return this;
    }
}
